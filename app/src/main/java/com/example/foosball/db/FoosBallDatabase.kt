package com.example.foosball.db

import android.content.Context
import androidx.room.*
import com.example.foosball.models.Match
import com.example.foosball.models.Person
import com.google.gson.GsonBuilder

@Database(
    entities = [
        Person::class,
        Match::class
    ],
    version = 4,
    exportSchema = false
)
@TypeConverters(DbConverters::class)
abstract class FoosBallDatabase : RoomDatabase() {

    abstract fun personDao(): PersonDao

    abstract fun matchDao(): MatchDao

    companion object {
        @Volatile
        private var INSTANCE: FoosBallDatabase? = null

        fun getDatabase(context: Context): FoosBallDatabase {
            if (INSTANCE == null) {
                INSTANCE = createDatabase(context)
            }
            return INSTANCE!!
        }

        private fun createDatabase(context: Context): FoosBallDatabase {

            return Room.databaseBuilder(context, FoosBallDatabase::class.java, "local-db")
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}

class DbConverters {

    @TypeConverter
    fun stringToPerson(data: String): Person {
        return GsonBuilder().create().fromJson(data, Person::class.java)
    }

    @TypeConverter
    fun personToString(person: Person): String {
        return GsonBuilder().create().toJson(person)
    }
}
