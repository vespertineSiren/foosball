package com.example.foosball.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.foosball.models.Match
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single

@Dao
interface MatchDao : BaseDao<Match> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllMatches(matchesList: List<Match>): Maybe<MutableList<Long>>

    @Query("SELECT * FROM matches ORDER BY id DESC")
    fun getAllMatches(): Single<List<Match>>

    @Query("SELECT * FROM matches ORDER BY id DESC")
    fun getAllMatchesFlowable(): Flowable<List<Match>>
}
