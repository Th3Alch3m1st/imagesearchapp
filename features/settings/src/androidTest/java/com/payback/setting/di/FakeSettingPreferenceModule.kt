package com.payback.setting.di

import com.payback.preference.di.SettingPreferenceModule
import com.payback.preference.usecase.ISettingPreference
import com.payback.setting.fake.FakeSettingPreferenceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

/**
 * Created By Rafiqul Hasan
 */
@Module
@TestInstallIn(
	components = [SingletonComponent::class],
	replaces = [SettingPreferenceModule::class]
)
abstract class FakeSettingPreferenceModule {

	@Binds
	@Singleton
	abstract fun provideSettingPreference(sourceImpl: FakeSettingPreferenceImpl): ISettingPreference
}