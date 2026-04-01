package com.civicsready.data.repository

import com.civicsready.data.assets.OfficialAssetLoader
import com.civicsready.data.assets.ZipLookupResult
import com.civicsready.data.local.ProgressDao
import com.civicsready.data.local.ProgressEntity
import com.civicsready.data.preferences.UserPreferences
import com.civicsready.domain.model.ALL_QUESTIONS
import com.civicsready.domain.model.QUESTIONS_6520
import com.civicsready.domain.model.CivicsQuestion
import com.civicsready.domain.model.DynamicAnswerType
import com.civicsready.domain.model.FederalOfficials
import com.civicsready.domain.model.Officials
import com.civicsready.domain.model.QuestionProgress
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CivicsRepository @Inject constructor(
    private val progressDao: ProgressDao,
    private val userPrefs: UserPreferences,
    private val assetLoader: OfficialAssetLoader
) {
    // ── Officials ────────────────────────────────────────────────────────────

    val officialsFlow: Flow<Officials> = combine(
        userPrefs.stateCode,
        userPrefs.stateName,
        userPrefs.governor,
        userPrefs.senator1,
        userPrefs.senator2
    ) { stateCode, stateName, governor, senator1, senator2 ->
        Officials(stateCode, stateName, governor, senator1, senator2)
    }.combine(
        combine(userPrefs.representative, userPrefs.stateCapital, userPrefs.zipCode) {
            rep, capital, zip -> Triple(rep, capital, zip)
        }
    ) { officials, (rep, capital, zip) ->
        officials.copy(representative = rep, stateCapital = capital, zipCode = zip)
    }

    val is6520Mode: Flow<Boolean>    = userPrefs.is6520Mode
    val isOrderedMode: Flow<Boolean> = userPrefs.isOrderedMode

    /** Resolves a zip code and persists results. Returns null if zip not found. */
    suspend fun resolveZipCode(zip: String): ZipLookupResult? {
        val result = assetLoader.lookup(zip) ?: return null
        userPrefs.saveOfficials(
            zipCode       = zip,
            stateCode     = result.stateCode,
            stateName     = result.stateName,
            governor      = result.governor,
            senator1      = result.senator1,
            senator2      = result.senator2,
            representative = result.representative,
            stateCapital  = result.stateCapital
        )
        return result
    }

    suspend fun set6520Mode(enabled: Boolean)    = userPrefs.set6520Mode(enabled)
    suspend fun setOrderedMode(enabled: Boolean) = userPrefs.setOrderedMode(enabled)

    // ── Questions ────────────────────────────────────────────────────────────

    /** Returns questions with dynamic answers merged from user location data. */
    suspend fun getQuestions(for6520Only: Boolean = false): List<CivicsQuestion> {
        val base = if (for6520Only) QUESTIONS_6520 else ALL_QUESTIONS
        val officials = officialsFlow.first()
        return base.map { q -> q.withResolvedAnswers(officials) }
    }

    private fun CivicsQuestion.withResolvedAnswers(officials: Officials): CivicsQuestion {
        val dynamic = dynamicAnswerType ?: return this
        val resolved: List<String> = when (dynamic) {
            DynamicAnswerType.GOVERNOR       -> listOf(officials.governor).filterNotBlank()
            DynamicAnswerType.SENATOR        -> listOf(officials.senator1, officials.senator2).filterNotBlank()
            DynamicAnswerType.REPRESENTATIVE -> listOf(officials.representative).filterNotBlank()
            DynamicAnswerType.STATE_CAPITAL  -> listOf(officials.stateCapital).filterNotBlank()
            DynamicAnswerType.PRESIDENT      -> listOf(FederalOfficials.PRESIDENT)
            DynamicAnswerType.VICE_PRESIDENT -> listOf(FederalOfficials.VICE_PRESIDENT)
            DynamicAnswerType.SPEAKER_OF_HOUSE -> listOf(FederalOfficials.SPEAKER_OF_HOUSE)
            DynamicAnswerType.CHIEF_JUSTICE  -> listOf(FederalOfficials.CHIEF_JUSTICE)
        }
        return if (resolved.isEmpty()) this
        else copy(acceptableAnswers = resolved)
    }

    private fun List<String>.filterNotBlank() = filter { it.isNotBlank() }

    // ── Progress ─────────────────────────────────────────────────────────────

    val allProgressFlow: Flow<Map<Int, QuestionProgress>> = progressDao.getAllProgress().map { list ->
        list.associate { entity ->
            entity.questionId to QuestionProgress(
                questionId        = entity.questionId,
                totalAttempts     = entity.totalAttempts,
                correctAttempts   = entity.correctAttempts,
                lastAttemptTimestamp = entity.lastAttemptTimestamp
            )
        }
    }

    suspend fun recordAnswer(questionId: Int, correct: Boolean) {
        progressDao.recordAnswer(questionId, correct, System.currentTimeMillis())
    }
}
