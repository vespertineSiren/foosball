package com.example.foosball.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.foosball.models.Person
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single

@Dao
interface PersonDao : BaseDao<Person> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllPersons(matchesList: List<Person>): Maybe<MutableList<Long>>

    @Query("SELECT * FROM persons")
    fun getAllPersons(): Single<List<Person>>

    @Query("SELECT * FROM persons WHERE id = :personID")
    fun getPersonById(personID: Int): Single<Person>

    @Query("SELECT * FROM persons")
    fun getAllPersonsFlowable(): Flowable<List<Person>>
}
