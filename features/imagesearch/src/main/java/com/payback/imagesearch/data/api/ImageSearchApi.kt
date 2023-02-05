package com.payback.imagesearch.data.api

import com.payback.imagesearch.data.dto.ImageSearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

/**
 * Created by Rafiqul Hasan
 */
interface ImageSearchApi {
	@GET("api/")
	suspend fun searchImages(@QueryMap queryMap: Map<String, String>): Response<ImageSearchResponse>
}