package com.example.foosball.di

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModel
import com.example.foosball.ui.edit.EditFragment
import com.example.foosball.ui.edit.EditViewModel
import com.example.foosball.ui.person.PersonFragment
import com.example.foosball.ui.person.PersonViewModel
import com.example.foosball.ui.stats.StatsFragment
import com.example.foosball.ui.stats.StatsViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class FragmentBuilder {

    @Binds
    abstract fun bindFragmentFactory(
        factory: DaggerFragmentFactory
    ): FragmentFactory

    @ContributesAndroidInjector
    abstract fun bindContribStatFragment(): StatsFragment

    @ContributesAndroidInjector
    abstract fun bindContribPersonFragment(): PersonFragment

    @ContributesAndroidInjector
    abstract fun bindContribEditFragment(): EditFragment

    @Binds
    @IntoMap
    @FragmentMapKey(StatsFragment::class)
    abstract fun bindStatsFragment(fragment: StatsFragment): Fragment

    @Binds
    @IntoMap
    @FragmentMapKey(PersonFragment::class)
    abstract fun bindPersonFragment(fragment: PersonFragment): Fragment

    @Binds
    @IntoMap
    @FragmentMapKey(EditFragment::class)
    abstract fun bindEditFragment(fragment: EditFragment): Fragment
}

@Module
abstract class StatsModule {
    @Binds
    @IntoMap
    @ViewModelMapKey(StatsViewModel::class)
    abstract fun bindStatsViewModel(viewModel: StatsViewModel): ViewModel
}

@Module
abstract class PersonModule {
    @Binds
    @IntoMap
    @ViewModelMapKey(PersonViewModel::class)
    abstract fun bindPersonViewModel(viewModel: PersonViewModel): ViewModel
}

@Module
abstract class EditModule {
    @Binds
    @IntoMap
    @ViewModelMapKey(EditViewModel::class)
    abstract fun bindEditViewModel(viewModel: EditViewModel): ViewModel
}
