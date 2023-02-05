package com.payback.imagesearch.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.payback.core.mapper.Mapper
import com.payback.imagesearch.data.dto.ImageResponse
import com.payback.imagesearch.data.remote.ImageSearchRemoteSource
import com.payback.core.model.ImageUIModel
import com.payback.imagesearch.data.paging.ImageSearchPagingSource
import com.payback.imagesearch.domain.repository.ImageSearchRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Created by Rafiqul Hasan
 */
class ImageSearchPagingRepositoryImpl @Inject constructor(
	private val remote: ImageSearchRemoteSource,
	private val mapper: Mapper<ImageResponse, ImageUIModel>
) : ImageSearchRepository {
	override fun searchImage(query: String, pageSize: Int): Flow<PagingData<ImageUIModel>> {
		return Pager(
			config = PagingConfig(
				pageSize = pageSize,
				prefetchDistance = 10,
				initialLoadSize = 10
			),
			initialKey = 1,
			pagingSourceFactory = { ImageSearchPagingSource(remote, query, mapper) }
		).flow
	}
}