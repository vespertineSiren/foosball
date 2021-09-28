package com.example.foosball.di

import android.app.Application
import com.example.foosball.db.FoosBallDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    fun provideFoosBallDatabase(application: Application): FoosBallDatabase =
        FoosBallDatabase.getDatabase(application)
}
