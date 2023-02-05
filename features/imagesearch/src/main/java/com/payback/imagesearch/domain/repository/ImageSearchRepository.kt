package com.payback.imagesearch.domain.repository

import androidx.paging.PagingData
import com.payback.core.model.ImageUIModel
import kotlinx.coroutines.flow.Flow

/**
 * Created by Rafiqul Hasan
 */
interface ImageSearchRepository {
	fun searchImage(query:String, pageSize: Int): Flow<PagingData<ImageUIModel>>
}