package com.example.foosball.di

import com.example.foosball.ui.main.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
internal abstract class MainBuilder {
    @ContributesAndroidInjector(
        modules = [
            ViewModelModule::class,
            FragmentBuilder::class
        ]
    )
    abstract fun bindMainActivity(): MainActivity
}
