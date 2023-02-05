package com.payback.imagesearch.domain.usecase

import androidx.paging.PagingData
import com.payback.core.model.ImageUIModel
import kotlinx.coroutines.flow.Flow

/**
 * Created by Rafiqul Hasan
 */
interface ImageSearchUseCase {
	fun searchImage(query:String, pageSize: Int): Flow<PagingData<ImageUIModel>>
}