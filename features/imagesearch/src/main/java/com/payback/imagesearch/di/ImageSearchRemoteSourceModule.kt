package com.payback.imagesearch.di

import com.payback.imagesearch.data.remote.ImageSearchRemoteSource
import com.payback.imagesearch.data.remote.ImageSearchRemoteSourceImpl
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
abstract class ImageSearchRemoteSourceModule {
	@Binds
	@Singleton
	abstract fun provideImageSearchRemoteSource(sourceImpl: ImageSearchRemoteSourceImpl): ImageSearchRemoteSource
}