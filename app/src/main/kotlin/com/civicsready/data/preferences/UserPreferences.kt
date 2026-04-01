package com.civicsready.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

@Singleton
class UserPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private object Keys {
        val ZIP_CODE        = stringPreferencesKey("zip_code")
        val STATE_CODE      = stringPreferencesKey("state_code")
        val STATE_NAME      = stringPreferencesKey("state_name")
        val GOVERNOR        = stringPreferencesKey("governor")
        val SENATOR1        = stringPreferencesKey("senator1")
        val SENATOR2        = stringPreferencesKey("senator2")
        val REPRESENTATIVE  = stringPreferencesKey("representative")
        val STATE_CAPITAL   = stringPreferencesKey("state_capital")
        val IS_6520_MODE    = booleanPreferencesKey("is_6520_mode")
        val IS_ORDERED_MODE = booleanPreferencesKey("is_ordered_mode")
    }

    val zipCode: Flow<String>         = context.dataStore.data.map { it[Keys.ZIP_CODE] ?: "" }
    val stateCode: Flow<String>       = context.dataStore.data.map { it[Keys.STATE_CODE] ?: "" }
    val stateName: Flow<String>       = context.dataStore.data.map { it[Keys.STATE_NAME] ?: "" }
    val governor: Flow<String>        = context.dataStore.data.map { it[Keys.GOVERNOR] ?: "" }
    val senator1: Flow<String>        = context.dataStore.data.map { it[Keys.SENATOR1] ?: "" }
    val senator2: Flow<String>        = context.dataStore.data.map { it[Keys.SENATOR2] ?: "" }
    val representative: Flow<String>  = context.dataStore.data.map { it[Keys.REPRESENTATIVE] ?: "" }
    val stateCapital: Flow<String>    = context.dataStore.data.map { it[Keys.STATE_CAPITAL] ?: "" }
    val is6520Mode: Flow<Boolean>     = context.dataStore.data.map { it[Keys.IS_6520_MODE] ?: false }
    val isOrderedMode: Flow<Boolean>  = context.dataStore.data.map { it[Keys.IS_ORDERED_MODE] ?: false }

    suspend fun saveOfficials(
        zipCode: String,
        stateCode: String,
        stateName: String,
        governor: String,
        senator1: String,
        senator2: String,
        representative: String,
        stateCapital: String
    ) {
        context.dataStore.edit { prefs ->
            prefs[Keys.ZIP_CODE]       = zipCode
            prefs[Keys.STATE_CODE]     = stateCode
            prefs[Keys.STATE_NAME]     = stateName
            prefs[Keys.GOVERNOR]       = governor
            prefs[Keys.SENATOR1]       = senator1
            prefs[Keys.SENATOR2]       = senator2
            prefs[Keys.REPRESENTATIVE] = representative
            prefs[Keys.STATE_CAPITAL]  = stateCapital
        }
    }

    suspend fun set6520Mode(enabled: Boolean) {
        context.dataStore.edit { it[Keys.IS_6520_MODE] = enabled }
    }

    suspend fun setOrderedMode(enabled: Boolean) {
        context.dataStore.edit { it[Keys.IS_ORDERED_MODE] = enabled }
    }
}
