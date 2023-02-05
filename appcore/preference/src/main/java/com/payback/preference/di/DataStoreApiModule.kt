package com.payback.preference.di

import com.payback.preference.datastore.PreferenceDataStoreAPIImpl
import com.payback.preference.datastore.domain.IPreferenceDataStoreAPI
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
abstract class DataStoreApiModule {

	@Binds
	@Singleton
	abstract fun provideWeatherInfoRemoteSource(sourceImpl: PreferenceDataStoreAPIImpl): IPreferenceDataStoreAPI
}