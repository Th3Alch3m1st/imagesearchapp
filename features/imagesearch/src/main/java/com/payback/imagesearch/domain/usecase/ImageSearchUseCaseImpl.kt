package com.payback.imagesearch.domain.usecase

import androidx.paging.PagingData
import com.payback.core.model.ImageUIModel
import com.payback.imagesearch.domain.repository.ImageSearchRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Created by Rafiqul Hasan
 */
class ImageSearchUseCaseImpl @Inject constructor(
	private val repository: ImageSearchRepository
) : ImageSearchUseCase {
	override fun searchImage(query: String, pageSize: Int): Flow<PagingData<ImageUIModel>> {
		return repository.searchImage(query, pageSize)
	}
}