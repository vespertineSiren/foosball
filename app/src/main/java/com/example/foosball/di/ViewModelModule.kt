package com.example.foosball.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.foosball.ui.edit.EditViewModel
import com.example.foosball.ui.main.MainViewModel
import com.example.foosball.ui.person.PersonViewModel
import com.example.foosball.ui.stats.StatsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(
        factory: ViewModelFactory
    ): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelMapKey(MainViewModel::class)
    abstract fun bindMainViewModel(vm: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelMapKey(StatsViewModel::class)
    internal abstract fun bindStatsViewModel(viewModel: StatsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelMapKey(PersonViewModel::class)
    internal abstract fun bindPersonViewModel(viewModel: PersonViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelMapKey(EditViewModel::class)
    internal abstract fun bindEditViewModel(viewModel: EditViewModel): ViewModel
}
