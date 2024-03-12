package com.rizrmdhn.storyapp.di

import com.rizrmdhn.core.domain.usecase.StoryInteractor
import com.rizrmdhn.core.domain.usecase.StoryUseCase
import com.rizrmdhn.storyapp.ui.StoryAppViewModel
import com.rizrmdhn.storyapp.ui.screen.addWithLocation.AddScreenWithLocationViewModel
import com.rizrmdhn.storyapp.ui.screen.detail.DetailScreenViewModel
import com.rizrmdhn.storyapp.ui.screen.home.HomeScreenViewModel
import com.rizrmdhn.storyapp.ui.screen.login.LoginScreenViewModel
import com.rizrmdhn.storyapp.ui.screen.register.RegisterScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val useCaseModule = module {
    factory<StoryUseCase> { StoryInteractor(get()) }
}

val viewModelModule = module {
    viewModel { LoginScreenViewModel() }
    viewModel { RegisterScreenViewModel() }
    viewModel { HomeScreenViewModel(get()) }
    viewModel { DetailScreenViewModel(get()) }
    viewModel { AddScreenWithLocationViewModel(get()) }
    viewModel { StoryAppViewModel(get()) }
}