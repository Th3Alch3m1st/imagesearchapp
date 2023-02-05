package com.payback.setting.di

import com.payback.setting.domain.usecase.ISettingUseCase
import com.payback.setting.domain.usecase.SettingUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created By Rafiqul Hasan
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class SettingUseCaseModule {

	@Binds
	@Singleton
	abstract fun provideSettingUseCaseImpl(useCaseImpl: SettingUseCaseImpl): ISettingUseCase
}