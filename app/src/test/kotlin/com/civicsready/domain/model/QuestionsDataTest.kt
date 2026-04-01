package com.civicsready.domain.model
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class QuestionsDataTest {

    // ── Count and uniqueness ──────────────────────────────────────────────────

    @Test
    fun `ALL_QUESTIONS contains exactly 128 questions`() {
        assertEquals(128, ALL_QUESTIONS.size)
    }

    @Test
    fun `all question IDs are unique`() {
        val ids = ALL_QUESTIONS.map { it.id }
        assertEquals("Duplicate IDs found", ids.size, ids.toSet().size)
    }

    @Test
    fun `question IDs are sequential from 1 to 128`() {
        val ids = ALL_QUESTIONS.map { it.id }.sorted()
        assertEquals((1..128).toList(), ids)
    }

    @Test
    fun `every question has non-blank text`() {
        ALL_QUESTIONS.forEach { q ->
            assertTrue("Q${q.id} has blank text", q.text.isNotBlank())
        }
    }

    @Test
    fun `every question has at least one acceptable answer`() {
        ALL_QUESTIONS.forEach { q ->
            assertTrue("Q${q.id} has no acceptable answers", q.acceptableAnswers.isNotEmpty())
        }
    }

    // ── 65/20 special pool ────────────────────────────────────────────────────

    @Test
    fun `QUESTIONS_6520 contains exactly 20 questions`() {
        assertEquals(20, QUESTIONS_6520.size)
    }

    @Test
    fun `all QUESTIONS_6520 entries have isFor6520 set to true`() {
        QUESTIONS_6520.forEach { q ->
            assertTrue("Q${q.id} should be isFor6520", q.isFor6520)
        }
    }

    @Test
    fun `QUESTIONS_6520 contains the correct question IDs`() {
        val expected = setOf(2, 7, 12, 20, 30, 36, 38, 39, 44, 52, 61, 66, 74, 78, 86, 94, 113, 115, 121, 126)
        val actual = QUESTIONS_6520.map { it.id }.toSet()
        assertEquals(expected, actual)
    }

    @Test
    fun `non-6520 questions have isFor6520 set to false`() {
        val expected6520Ids = setOf(2, 7, 12, 20, 30, 36, 38, 39, 44, 52, 61, 66, 74, 78, 86, 94, 113, 115, 121, 126)
        ALL_QUESTIONS.filter { it.id !in expected6520Ids }.forEach { q ->
            assertTrue("Q${q.id} should NOT be isFor6520", !q.isFor6520)
        }
    }

    // ── Multi-answer requirements ─────────────────────────────────────────────

    @Test
    fun `Q10 requires 2 answers`() {
        assertEquals(2, ALL_QUESTIONS.first { it.id == 10 }.minimumAnswersRequired)
    }

    @Test
    fun `Q48 requires 2 answers`() {
        assertEquals(2, ALL_QUESTIONS.first { it.id == 48 }.minimumAnswersRequired)
    }

    @Test
    fun `Q65 requires 3 answers`() {
        assertEquals(3, ALL_QUESTIONS.first { it.id == 65 }.minimumAnswersRequired)
    }

    @Test
    fun `Q67 requires 2 answers`() {
        assertEquals(2, ALL_QUESTIONS.first { it.id == 67 }.minimumAnswersRequired)
    }

    @Test
    fun `Q69 requires 2 answers`() {
        assertEquals(2, ALL_QUESTIONS.first { it.id == 69 }.minimumAnswersRequired)
    }

    @Test
    fun `Q81 requires 5 answers`() {
        assertEquals(5, ALL_QUESTIONS.first { it.id == 81 }.minimumAnswersRequired)
    }

    @Test
    fun `Q126 requires 3 answers`() {
        assertEquals(3, ALL_QUESTIONS.first { it.id == 126 }.minimumAnswersRequired)
    }

    @Test
    fun `all other questions require 1 answer`() {
        val multiAnswerIds = setOf(10, 48, 65, 67, 69, 81, 126)
        ALL_QUESTIONS.filter { it.id !in multiAnswerIds }.forEach { q ->
            assertEquals("Q${q.id} should require 1 answer", 1, q.minimumAnswersRequired)
        }
    }

    // ── Dynamic answer types ──────────────────────────────────────────────────

    @Test
    fun `Q23 (US Senator) has SENATOR dynamic type`() {
        assertEquals(DynamicAnswerType.SENATOR, ALL_QUESTIONS.first { it.id == 23 }.dynamicAnswerType)
    }

    @Test
    fun `Q29 (US Representative) has REPRESENTATIVE dynamic type`() {
        assertEquals(DynamicAnswerType.REPRESENTATIVE, ALL_QUESTIONS.first { it.id == 29 }.dynamicAnswerType)
    }

    @Test
    fun `Q30 (Speaker) has SPEAKER_OF_HOUSE dynamic type`() {
        assertEquals(DynamicAnswerType.SPEAKER_OF_HOUSE, ALL_QUESTIONS.first { it.id == 30 }.dynamicAnswerType)
    }

    @Test
    fun `Q38 (President) has PRESIDENT dynamic type`() {
        assertEquals(DynamicAnswerType.PRESIDENT, ALL_QUESTIONS.first { it.id == 38 }.dynamicAnswerType)
    }

    @Test
    fun `Q39 (Vice President) has VICE_PRESIDENT dynamic type`() {
        assertEquals(DynamicAnswerType.VICE_PRESIDENT, ALL_QUESTIONS.first { it.id == 39 }.dynamicAnswerType)
    }

    @Test
    fun `Q57 (Chief Justice) has CHIEF_JUSTICE dynamic type`() {
        assertEquals(DynamicAnswerType.CHIEF_JUSTICE, ALL_QUESTIONS.first { it.id == 57 }.dynamicAnswerType)
    }

    @Test
    fun `Q61 (Governor) has GOVERNOR dynamic type`() {
        assertEquals(DynamicAnswerType.GOVERNOR, ALL_QUESTIONS.first { it.id == 61 }.dynamicAnswerType)
    }

    @Test
    fun `Q62 (State capital) has STATE_CAPITAL dynamic type`() {
        assertEquals(DynamicAnswerType.STATE_CAPITAL, ALL_QUESTIONS.first { it.id == 62 }.dynamicAnswerType)
    }

    @Test
    fun `exactly 8 questions have dynamic answer types`() {
        val dynamicIds = setOf(23, 29, 30, 38, 39, 57, 61, 62)
        val actualDynamic = ALL_QUESTIONS.filter { it.dynamicAnswerType != null }.map { it.id }.toSet()
        assertEquals(dynamicIds, actualDynamic)
    }

    // ── Sections ──────────────────────────────────────────────────────────────

    @Test
    fun `all questions have a valid section`() {
        val validSections = Section.values().toSet()
        ALL_QUESTIONS.forEach { q ->
            assertTrue("Q${q.id} has invalid section", q.section in validSections)
        }
    }

    @Test
    fun `QUESTIONS_6520 is a subset of ALL_QUESTIONS`() {
        val allIds = ALL_QUESTIONS.map { it.id }.toSet()
        QUESTIONS_6520.forEach { q ->
            assertTrue("Q${q.id} from QUESTIONS_6520 not in ALL_QUESTIONS", q.id in allIds)
        }
    }

    @Test
    fun `Q81 (13 original states) has at least 13 acceptable answers`() {
        val q81 = ALL_QUESTIONS.first { it.id == 81 }
        assertTrue("Q81 should have at least 13 acceptable answers", q81.acceptableAnswers.size >= 13)
    }

    @Test
    fun `Q65 has at least 3 acceptable answers`() {
        val q65 = ALL_QUESTIONS.first { it.id == 65 }
        assertTrue("Q65 should have at least 3 acceptable answers", q65.acceptableAnswers.size >= 3)
    }

    // ── Multi-answer questions have enough acceptable answers ────────────────

    @Test
    fun `Q10 has at least 2 acceptable answers`() {
        val q = ALL_QUESTIONS.first { it.id == 10 }
        assertTrue("Q10 requires ${q.minimumAnswersRequired} but has ${q.acceptableAnswers.size}", q.acceptableAnswers.size >= q.minimumAnswersRequired)
    }

    @Test
    fun `Q48 has at least 2 acceptable answers`() {
        val q = ALL_QUESTIONS.first { it.id == 48 }
        assertTrue("Q48 requires ${q.minimumAnswersRequired} but has ${q.acceptableAnswers.size}", q.acceptableAnswers.size >= q.minimumAnswersRequired)
    }

    @Test
    fun `Q67 has at least 2 acceptable answers`() {
        val q = ALL_QUESTIONS.first { it.id == 67 }
        assertTrue("Q67 requires ${q.minimumAnswersRequired} but has ${q.acceptableAnswers.size}", q.acceptableAnswers.size >= q.minimumAnswersRequired)
    }

    @Test
    fun `Q69 has at least 2 acceptable answers`() {
        val q = ALL_QUESTIONS.first { it.id == 69 }
        assertTrue("Q69 requires ${q.minimumAnswersRequired} but has ${q.acceptableAnswers.size}", q.acceptableAnswers.size >= q.minimumAnswersRequired)
    }

    @Test
    fun `Q126 has at least 3 acceptable answers`() {
        val q = ALL_QUESTIONS.first { it.id == 126 }
        assertTrue("Q126 requires ${q.minimumAnswersRequired} but has ${q.acceptableAnswers.size}", q.acceptableAnswers.size >= q.minimumAnswersRequired)
    }

    // ── Answer quality ──────────────────────────────────────────────────────

    @Test
    fun `no acceptable answer has leading or trailing whitespace`() {
        ALL_QUESTIONS.forEach { q ->
            q.acceptableAnswers.forEach { answer ->
                assertEquals(
                    "Q${q.id} answer \"$answer\" has leading/trailing whitespace",
                    answer.trim(),
                    answer
                )
            }
        }
    }

    // ── Section coverage ────────────────────────────────────────────────────

    @Test
    fun `every Section enum value has at least one question`() {
        val sectionsWithQuestions = ALL_QUESTIONS.map { it.section }.toSet()
        Section.values().forEach { section ->
            assertTrue(
                "Section ${section.name} has no questions",
                section in sectionsWithQuestions
            )
        }
    }
}
