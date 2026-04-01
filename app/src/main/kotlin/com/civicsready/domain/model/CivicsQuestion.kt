package com.civicsready.domain.model

data class CivicsQuestion(
    val id: Int,
    val section: Section,
    val text: String,
    val acceptableAnswers: List<String>,
    val minimumAnswersRequired: Int = 1,
    val dynamicAnswerType: DynamicAnswerType? = null,
    val isFor6520: Boolean = false
)

enum class Section(val displayName: String) {
    PRINCIPLES_OF_GOVERNMENT("Principles of American Government"),
    SYSTEM_OF_GOVERNMENT("System of Government"),
    RIGHTS_AND_RESPONSIBILITIES("Rights and Responsibilities"),
    COLONIAL_AND_INDEPENDENCE("Colonial Period and Independence"),
    EIGHTEEN_HUNDREDS("1800s"),
    RECENT_HISTORY("Recent American History"),
    SYMBOLS("Symbols"),
    HOLIDAYS("Holidays")
}

enum class DynamicAnswerType {
    GOVERNOR,
    SENATOR,
    REPRESENTATIVE,
    STATE_CAPITAL,
    PRESIDENT,
    VICE_PRESIDENT,
    SPEAKER_OF_HOUSE,
    CHIEF_JUSTICE
}
