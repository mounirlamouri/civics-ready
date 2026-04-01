package com.civicsready.domain.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class OfficialsTest {

    @Test
    fun `isResolved is false for default Officials`() {
        assertFalse(Officials().isResolved)
    }

    @Test
    fun `isResolved is false when stateCode is blank`() {
        assertFalse(Officials(stateCode = "").isResolved)
        assertFalse(Officials(stateCode = "   ").isResolved)
    }

    @Test
    fun `isResolved is true when stateCode, stateName, and governor are set`() {
        assertTrue(Officials(stateCode = "CA", stateName = "California", governor = "Gavin Newsom").isResolved)
        assertTrue(Officials(stateCode = "TX", stateName = "Texas", governor = "Greg Abbott").isResolved)
    }

    @Test
    fun `isResolved is false when stateName or governor is blank`() {
        assertFalse(Officials(stateCode = "NY", stateName = "", governor = "Gov").isResolved)
        assertFalse(Officials(stateCode = "NY", stateName = "New York", governor = "").isResolved)
        assertFalse(Officials(stateCode = "NY", stateName = "", governor = "").isResolved)
    }

    @Test
    fun `FederalOfficials contains expected values`() {
        assertEquals("Donald Trump", FederalOfficials.PRESIDENT)
        assertEquals("JD Vance", FederalOfficials.VICE_PRESIDENT)
        assertEquals("Mike Johnson", FederalOfficials.SPEAKER_OF_HOUSE)
        assertEquals("John Roberts", FederalOfficials.CHIEF_JUSTICE)
    }

    @Test
    fun `Officials copy preserves all fields`() {
        val original = Officials(
            stateCode = "CA",
            stateName = "California",
            governor = "Gavin Newsom",
            senator1 = "Alex Padilla",
            senator2 = "Adam Schiff",
            representative = "Nancy Pelosi",
            stateCapital = "Sacramento",
            zipCode = "94102"
        )
        val copy = original.copy(governor = "New Governor")
        assertEquals("CA", copy.stateCode)
        assertEquals("New Governor", copy.governor)
        assertEquals("Sacramento", copy.stateCapital)
        assertTrue(copy.isResolved)
    }
}
