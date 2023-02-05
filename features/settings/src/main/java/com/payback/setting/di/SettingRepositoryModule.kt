package com.payback.setting.di

import com.payback.setting.data.SettingRepositoryImpl
import com.payback.setting.domain.repository.ISettingRepository
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
abstract class SettingRepositoryModule {

	@Binds
	@Singleton
	abstract fun provideSettingUseCaseImpl(useCaseImpl: SettingRepositoryImpl): ISettingRepository
}