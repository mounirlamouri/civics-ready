package com.civicsready.domain.model

import java.time.LocalDate

data class Officials(
    val stateCode: String = "",
    val stateName: String = "",
    val governor: String = "",
    val senator1: String = "",
    val senator2: String = "",
    val representative: String = "",
    val stateCapital: String = "",
    val zipCode: String = ""
) {
    val isResolved: Boolean get() = stateCode.isNotBlank() && stateName.isNotBlank() && governor.isNotBlank()
}

// Federal officials — updated Feb 2026
object FederalOfficials {
    const val PRESIDENT = "Donald Trump"
    const val VICE_PRESIDENT = "JD Vance"
    const val SPEAKER_OF_HOUSE = "Mike Johnson"
    const val CHIEF_JUSTICE = "John Roberts"
    const val LAST_UPDATED = "February 2026"
    val LAST_UPDATED_DATE: LocalDate = LocalDate.of(2026, 2, 22)

    fun isStale(): Boolean = LAST_UPDATED_DATE.plusMonths(6).isBefore(LocalDate.now())
}
