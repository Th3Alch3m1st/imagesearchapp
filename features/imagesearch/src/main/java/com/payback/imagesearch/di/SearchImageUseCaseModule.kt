package com.payback.imagesearch.di

import com.payback.imagesearch.domain.usecase.ImageSearchUseCase
import com.payback.imagesearch.domain.usecase.ImageSearchUseCaseImpl
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
abstract class SearchImageUseCaseModule {

	@Binds
	@Singleton
	abstract fun provideSearchImageUseCaseImpl(useCaseImpl: ImageSearchUseCaseImpl): ImageSearchUseCase
}