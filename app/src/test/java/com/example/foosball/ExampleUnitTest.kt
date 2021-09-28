package com.example.foosball

import com.example.foosball.models.Match
import com.example.foosball.models.Person
import org.junit.Assert.*
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun test_Person_createDistinctPersonFromMatches() {
        val testMatchList = Match.createInitialList()
        val distinctPersonFromMatches = Person.createDistinctPersonFromMatches(testMatchList)

        val testAmos = Person.createTestAmos()
        val testDiego = Person.createTestDiego()
        val testJoel = Person.createTestJoel()
        val testTim = Person.createTestTim()

        val foundAmos = distinctPersonFromMatches.find { it.userID == testAmos.userID }
        assertNotNull(foundAmos)
        assertTrue(testAmos.userID == foundAmos!!.userID)

        val foundDiego = distinctPersonFromMatches.find { it.userID == testDiego.userID }
        assertNotNull(foundDiego)
        assertTrue(testDiego.userID == foundDiego!!.userID)

        val foundJoel = distinctPersonFromMatches.find { it.userID == testJoel.userID }
        assertNotNull(foundJoel)
        assertTrue(testJoel.userID == foundJoel!!.userID)

        val foundTim = distinctPersonFromMatches.find { it.userID == testTim.userID }
        assertNotNull(foundTim)
        assertTrue(testTim.userID == foundTim!!.userID)
    }

    @Test
    fun test_Person_validatePersonStats() {
        val testMatchList = Match.createInitialList()
        val testDiego = Person.createTestDiego().apply {
            winTotal = 0
            loseTotal = 0
            tieTotal = 0
            matchTotal = 0
        }
        testDiego.validatePersonStats(testMatchList)

        val correctDiego = Person.createTestDiego()

        assertTrue(testDiego.winTotal == correctDiego.winTotal)
        assertTrue(testDiego.loseTotal == correctDiego.loseTotal)
        assertTrue(testDiego.tieTotal == correctDiego.tieTotal)
        assertTrue(testDiego.matchTotal == correctDiego.matchTotal)
    }

    @Test
    fun test_Match_winner_Win_getter() {
        val testAmos = Person.createTestAmos()
        val testDiego = Person.createTestDiego()
        val testMatch = Match(
            testAmos,
            10,
            testDiego,
            2
        )

        assertSame(testAmos, testMatch.winner)
    }

    @Test
    fun test_Match_winner_Tie_getter() {
        val testAmos = Person.createTestAmos()
        val testDiego = Person.createTestDiego()
        val testMatch = Match(
            testAmos,
            0,
            testDiego,
            0
        )

        assertNull(testMatch.winner)
    }

    @Test
    fun test_Match_validatePersonsNewMatch() {
        val expectedAmos = Person.createTestAmos().apply {
            winTotal = 0
            loseTotal = 1
            tieTotal = 0
            matchTotal = 1
        }

        val expectedDiego = Person.createTestDiego().apply {
            winTotal = 1
            loseTotal = 0
            tieTotal = 0
            matchTotal = 1
        }

        val testAmos = Person.createTestAmos().apply {
            winTotal = 0
            loseTotal = 0
            tieTotal = 0
            matchTotal = 0
        }
        val testDiego = Person.createTestDiego().apply {
            winTotal = 0
            loseTotal = 0
            tieTotal = 0
            matchTotal = 0
        }

        val testMatch = Match(
            testAmos,
            3,
            testDiego,
            10
        )

        testMatch.validatePersonsNewMatch(testAmos, testDiego)

        assertEquals(expectedAmos.winTotal, testMatch.personPrimary.winTotal)
        assertEquals(expectedAmos.loseTotal, testMatch.personPrimary.loseTotal)
        assertEquals(expectedAmos.tieTotal, testMatch.personPrimary.tieTotal)
        assertEquals(expectedAmos.matchTotal, testMatch.personPrimary.matchTotal)

        assertEquals(expectedDiego.winTotal, testMatch.personSecondary.winTotal)
        assertEquals(expectedDiego.loseTotal, testMatch.personSecondary.loseTotal)
        assertEquals(expectedDiego.tieTotal, testMatch.personSecondary.tieTotal)
        assertEquals(expectedDiego.matchTotal, testMatch.personSecondary.matchTotal)
    }
}
