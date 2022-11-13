package com.example.foosball.di

import androidx.appcompat.app.AppCompatActivity
import dagger.android.HasAndroidInjector
import javax.inject.Inject
import dagger.android.DispatchingAndroidInjector
import android.os.Bundle
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.internal.Beta

@Beta
abstract class FragmentInjectActivity : AppCompatActivity(), HasAndroidInjector {
    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        beforeOnCreate()
        super.onCreate(savedInstanceState)
    }

    protected abstract fun beforeOnCreate()
    override fun androidInjector(): AndroidInjector<Any> = androidInjector

}