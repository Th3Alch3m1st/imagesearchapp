package com.payback.imagesearch.data.remote

import com.payback.core.network.Resource
import com.payback.imagesearch.data.dto.ImageSearchResponse


/**
 * Created By Rafiqul Hasan
 */
interface ImageSearchRemoteSource {
	suspend fun searchImages(query: String, pageNumber: Int, pageLimit: Int): Resource<ImageSearchResponse>
}