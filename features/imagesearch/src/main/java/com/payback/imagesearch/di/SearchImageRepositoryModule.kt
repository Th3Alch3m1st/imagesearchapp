package com.payback.imagesearch.di

import com.payback.imagesearch.data.repository.ImageSearchPagingRepositoryImpl
import com.payback.imagesearch.domain.repository.ImageSearchRepository
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
abstract class SearchImageRepositoryModule {

	@Binds
	@Singleton
	abstract fun provideSearchImagePagingRepositoryImpl(useCaseImpl: ImageSearchPagingRepositoryImpl): ImageSearchRepository
}