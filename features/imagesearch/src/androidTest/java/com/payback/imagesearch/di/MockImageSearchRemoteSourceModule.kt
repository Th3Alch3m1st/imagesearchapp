package com.payback.imagesearch.di


import com.payback.imagesearch.data.remote.ImageSearchRemoteSource
import com.payback.imagesearch.fakeremotesource.FakeImageSearchRemoteSourceImpl
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
    replaces = [ImageSearchRemoteSourceModule::class]
)
abstract class MockImageSearchRemoteSourceModule {
    @Singleton
    @Binds
    abstract fun provideFakeImageSearchRemoteSource(impl: FakeImageSearchRemoteSourceImpl): ImageSearchRemoteSource
}