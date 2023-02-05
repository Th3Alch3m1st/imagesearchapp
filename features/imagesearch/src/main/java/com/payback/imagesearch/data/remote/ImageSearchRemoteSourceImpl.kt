package com.payback.imagesearch.data.remote

import com.payback.core.BuildConfig
import com.payback.core.di.qualifiers.IoDispatcher
import com.payback.core.network.BaseSource
import com.payback.core.network.Resource
import com.payback.imagesearch.data.api.ImageSearchApi
import com.payback.imagesearch.data.dto.ImageSearchResponse
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

/**
 * Created By Rafiqul Hasan
 */
class ImageSearchRemoteSourceImpl @Inject constructor(
	private val api: ImageSearchApi,
	@IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ImageSearchRemoteSource, BaseSource() {
	override suspend fun searchImages(query: String, pageNumber: Int, pageLimit: Int): Resource<ImageSearchResponse> {
		val queryMap = mapOf(
			"key" to BuildConfig.AUTH_TOKEN,
			"q" to query,
			"page" to pageNumber.toString(),
			"per_page" to pageLimit.toString(),
		)
		return safeApiCall(ioDispatcher){
			api.searchImages(queryMap)
		}
	}
}