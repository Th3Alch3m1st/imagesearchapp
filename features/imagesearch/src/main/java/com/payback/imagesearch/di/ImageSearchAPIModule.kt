package com.payback.imagesearch.di


import com.payback.core.mapper.Mapper
import com.payback.imagesearch.data.api.ImageSearchApi
import com.payback.imagesearch.data.dto.ImageResponse
import com.payback.core.model.ImageUIModel
import com.payback.imagesearch.mapper.ImageResponseToImageUIModelMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Created By Rafiqul Hasan
 */
@Module
@InstallIn(SingletonComponent::class)
object  ImageSearchAPIModule {
	@Provides
	@Singleton
	fun provideImageSearchApi(retrofit: Retrofit): ImageSearchApi {
		return retrofit.create(ImageSearchApi::class.java)
	}
	@Provides
	@Singleton
	fun provideMapper(): Mapper<ImageResponse, ImageUIModel> {
		return ImageResponseToImageUIModelMapper()
	}
}