package com.example.foosball.db

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Maybe

@Dao
interface BaseDao<in T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(type: T): Maybe<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg type: T)

    @Delete
    fun delete(type: T): Completable

    @Update
    fun update(type: T): Maybe<Int>

    @Update
    fun updateAll(type: List<T>): Maybe<Int>
}
