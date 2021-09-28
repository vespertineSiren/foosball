package com.example.foosball.models

import androidx.room.Entity
import androidx.room.PrimaryKey

//  userID is just for testing. 
@Entity(tableName = "persons")
data class Person(
    val name: String,
    var winTotal: Int = 0,
    var loseTotal: Int = 0,
    var tieTotal: Int = 0,
    var matchTotal: Int = 0,
    var userID: Int = 0,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
) {

    val nameIDString: String
        get() = "$name ($id)"

    fun validatePersonStats(matchList: List<Match>) {
        if (userID == 0 && id != 0) userID = id

        val filteredMatchList = matchList.filter {
            (it.personPrimary.userID == this.userID) ||
                (it.personSecondary.userID == this.userID)
        }
        //  Reset stats
        this.winTotal = 0
        this.loseTotal = 0
        this.tieTotal = 0
        this.matchTotal = 0

        filteredMatchList.forEach { match ->

            matchTotal++
            match.winner?.let { winner ->
                if (winner.userID == this.userID) this.winTotal++
                else loseTotal++
            }
        }

        this.tieTotal = this.matchTotal - (this.loseTotal + this.winTotal)
    }

    companion object {

        fun createDistinctPersonFromMatches(matchList: List<Match>): List<Person> {
            val personSet = mutableSetOf<Person>()
            matchList.forEach {
                personSet.apply {
                    add(it.personPrimary)
                    add(it.personSecondary)
                }
            }
            return personSet.toList()
        }

        // Amos, win = 6, lose = 6, tie = , match = 12
        fun createTestAmos() = Person("Amos", 6, 6, 0, 12, 1)

        // Diego win = 5,  match = 8
        fun createTestDiego() = Person("Diego", 5, 3, 0, 8, 2)

        //  Joel match = 3
        fun createTestJoel() = Person("Joel", 1, 2, 0, 3, 2)

        //  Tim match = 5
        fun createTestTim() = Person("Tim", 2, 3, 0, 5, 4)

        fun createTestPersonList(): MutableList<Person> =
            mutableListOf(
                createTestAmos(),
                createTestDiego(),
                createTestJoel(),
                createTestTim()
            )
    }
}

/*
13 - Amos,4,Diego,5
12 - Amos,1,Diego,5
11 - Amos,2,Diego,5
10 - Amos,0,Diego,5
9 - Amos,6,Diego,5
8 - Amos,5,Diego,2
7 - Amos,4,Diego,0
6 - Joel,4,Diego,5
5 - Tim,4,Amos,5
4 - Tim,5,Amos,2
3 - Amos,3,Tim,5
2 - Amos,5,Tim,3
1 - Amos,5,Joel,4
0 - Joel,5,Tim,2
*/
