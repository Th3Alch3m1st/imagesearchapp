package com.payback.imagesearch.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.payback.core.mapper.Mapper
import com.payback.core.network.Resource
import com.payback.imagesearch.data.dto.ImageResponse
import com.payback.imagesearch.data.remote.ImageSearchRemoteSource
import com.payback.core.model.ImageUIModel
import java.lang.Exception

/**
 * Created by Rafiqul Hasan
 */
class ImageSearchPagingSource(
	private val remoteSource: ImageSearchRemoteSource,
	private val query: String,
	private val mapper: Mapper<ImageResponse, ImageUIModel>
) : PagingSource<Int, ImageUIModel>() {

	override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ImageUIModel> {
		return try {
			val pageNumber = params.key ?: 1
			val response = remoteSource.searchImages(
				query = query,
				pageNumber = pageNumber,
				pageLimit = params.loadSize
			)
			if (response is Resource.Success) {
				LoadResult.Page(
					data = response.data.hits?.map(mapper::map).orEmpty(),
					prevKey = if(pageNumber == 1) null else pageNumber-1,
					nextKey = calculateNextSearchParameter(
						pageNumber,
						params.loadSize,
						response.data.totalHits ?: 0
					)
				)
			} else {
				LoadResult.Error((response as Resource.Error).exception)
			}
		} catch (e: Exception) {
			LoadResult.Error(e)
		}
	}

	override fun getRefreshKey(state: PagingState<Int, ImageUIModel>): Int? {
		// Try to find the page key of the closest page to anchorPosition, from
		// either the prevKey or the nextKey, but you need to handle nullability
		// here:
		//  * prevKey == null -> anchorPage is the first page.
		//  * nextKey == null -> anchorPage is the last page.
		//  * both prevKey and nextKey null -> anchorPage is the initial page, so
		//    just return null.
		return state.anchorPosition?.let { anchorPosition ->
			val anchorPage = state.closestPageToPosition(anchorPosition)
			anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
		}
	}

	private fun calculateNextSearchParameter(pageNumber: Int, pageLimit: Int, total: Int): Int? {
		val currentCount = pageNumber * pageLimit
		return if (currentCount < total) {
			pageNumber + 1
		} else {
			null
		}
	}
}