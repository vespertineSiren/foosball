package com.example.foosball.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "matches")
data class Match(
    var personPrimary: Person,
    var personPrimaryScore: Int,
    var personSecondary: Person,
    var personSecondaryScore: Int,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
) {
    val winner: Person?
        get() = when {
            personPrimaryScore > personSecondaryScore -> {
                personPrimary
            }
            personPrimaryScore < personSecondaryScore -> {
                personSecondary
            }
            else -> {
                null
            }
        }

    fun validatePersonInMatch(person: Person) {
        if (this.personPrimary.userID == person.userID) {
            this.personPrimary = person
        } else if (this.personSecondary.userID == person.userID) {
            this.personSecondary = person
        }
    }

    fun validatePersonsNewMatch(first: Person, second: Person) {
        first.matchTotal++
        second.matchTotal++

        this.winner?.userID?.let {
            if (it == first.userID) {
                first.winTotal++
                second.loseTotal++
            } else {
                first.loseTotal++
                second.winTotal++
            }
            first.tieTotal = first.matchTotal - (first.loseTotal + first.winTotal)
            second.tieTotal = second.matchTotal - (second.loseTotal + second.winTotal)
        }
    }

    companion object {

        fun createRoomReadyList(matchList: List<Match>, personList: List<Person>) {
            personList.forEach { person ->
                person.validatePersonStats(matchList)
            }
            matchList.forEach { match ->
                personList.forEach { person ->
                    match.validatePersonInMatch(person)
                }
            }
        }

        fun createInitialList(): List<Match> {

            val personAmos = Person(name = "Amos", userID = 1)
            val personDiego = Person(name = "Diego", userID = 2)
            val personJoel = Person(name = "Joel", userID = 3)
            val personTim = Person(name = "Tim", userID = 4)

            return listOf(
                Match(personJoel, 5, personTim, 2),
                Match(personAmos, 5, personJoel, 4),
                Match(personAmos, 5, personTim, 3),
                Match(personAmos, 3, personTim, 5),
                Match(personTim, 5, personAmos, 2),
                Match(personTim, 4, personAmos, 5),
                Match(personJoel, 4, personDiego, 5),
                Match(personAmos, 4, personDiego, 0),
                Match(personAmos, 5, personDiego, 2),
                Match(personAmos, 6, personDiego, 5),
                Match(personAmos, 0, personDiego, 5),
                Match(personAmos, 2, personDiego, 5),
                Match(personAmos, 1, personDiego, 5),
                Match(personAmos, 4, personDiego, 5)
            )
        }
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
