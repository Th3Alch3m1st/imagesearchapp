package com.payback.imagesearch.data.paging

import androidx.paging.PagingSource
import com.payback.core.mapper.Mapper
import com.payback.core.network.RequestException
import com.payback.core.network.Resource
import com.payback.imagesearch.data.api.ImageSearchApiTest
import com.payback.imagesearch.data.dto.ImageResponse
import com.payback.imagesearch.data.dto.ImageSearchResponse
import com.payback.imagesearch.data.remote.ImageSearchRemoteSource
import com.payback.core.model.ImageUIModel
import com.payback.imagesearch.mapper.ImageResponseToImageUIModelMapper
import com.payback.imagesearch.utils.TestUtils.getImageSearchPagingData
import com.payback.testutil.returns
import com.payback.testutil.shouldEqual
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor

/**
 * Created by Rafiqul Hasan
 */
@RunWith(MockitoJUnitRunner::class)
class ImageSearchPagingSourceTest {
	@Mock
	lateinit var mockRemoteSource: ImageSearchRemoteSource

	private lateinit var sutPagingSource: ImageSearchPagingSource

	private lateinit var imageSearchResponseOne: Resource<ImageSearchResponse>
	private lateinit var imageSearchResponseTwo: Resource<ImageSearchResponse>
	private lateinit var imageSearchResponseEnd: Resource<ImageSearchResponse>

	private lateinit var mapper: Mapper<ImageResponse, ImageUIModel>

	@Before
	fun setup() {
		mapper = ImageResponseToImageUIModelMapper()
		sutPagingSource = ImageSearchPagingSource(
			mockRemoteSource,
			ImageSearchApiTest.QUERY,
			mapper
		)

		imageSearchResponseOne = getImageSearchPagingData(ImageSearchApiTest.PAGE_1_DATA)
		imageSearchResponseTwo = getImageSearchPagingData(ImageSearchApiTest.PAGE_2_DATA)
		imageSearchResponseEnd = getImageSearchPagingData(ImageSearchApiTest.PAGE_END_DATA)
	}

	@Test
	fun `check argument pass correctly for first paging in searchImages fun`() {
		runBlocking {
			//arrange
			pageOneDataSuccess()
			val acString = argumentCaptor<String>()
			val acInt = argumentCaptor<Int>()

			//act
			val refreshRequest: PagingSource.LoadParams.Refresh<Int> =
				PagingSource.LoadParams.Refresh(
					key = null,
					loadSize = ImageSearchApiTest.PAGE_LIMIT,
					placeholdersEnabled = false
				)
			sutPagingSource.load(refreshRequest)
			Mockito.verify(mockRemoteSource)
				.searchImages(acString.capture(), acInt.capture(), acInt.capture())

			//verify
			acString.firstValue shouldEqual ImageSearchApiTest.QUERY
			acInt.firstValue shouldEqual ImageSearchApiTest.PAGE_NO_1
			acInt.secondValue shouldEqual ImageSearchApiTest.PAGE_LIMIT

		}
	}

	@Test
	fun `check argument pass correctly for second paging in searchImages fun`() {
		runBlocking {
			//arrange
			pageOneDataSuccess()
			val acString = argumentCaptor<String>()
			val acInt = argumentCaptor<Int>()

			//act
			val refreshRequest: PagingSource.LoadParams.Refresh<Int> =
				PagingSource.LoadParams.Refresh(
					key = 2,
					loadSize = ImageSearchApiTest.PAGE_LIMIT,
					placeholdersEnabled = false
				)
			sutPagingSource.load(refreshRequest)
			Mockito.verify(mockRemoteSource)
				.searchImages(acString.capture(), acInt.capture(), acInt.capture())

			//verify
			acString.firstValue shouldEqual ImageSearchApiTest.QUERY
			acInt.firstValue shouldEqual ImageSearchApiTest.PAGE_NO_2
			acInt.secondValue shouldEqual ImageSearchApiTest.PAGE_LIMIT

		}
	}

	@Test
	fun `check argument pass correctly for third paging in searchImages fun`() {
		runBlocking {
			//arrange
			pageOneDataSuccess()
			val acString = argumentCaptor<String>()
			val acInt = argumentCaptor<Int>()

			//act
			val refreshRequest: PagingSource.LoadParams.Refresh<Int> =
				PagingSource.LoadParams.Refresh(
					key = 3,
					loadSize = ImageSearchApiTest.PAGE_LIMIT,
					placeholdersEnabled = false
				)
			sutPagingSource.load(refreshRequest)


			//verify
			Mockito.verify(mockRemoteSource)
				.searchImages(acString.capture(), acInt.capture(), acInt.capture())
			acString.firstValue shouldEqual ImageSearchApiTest.QUERY
			acInt.firstValue shouldEqual ImageSearchApiTest.PAGE_END
			acInt.secondValue shouldEqual ImageSearchApiTest.PAGE_LIMIT
		}
	}

	@Test
	fun `on Success first paging data and it should return first paging data`() {
		runBlocking {
			//arrange
			pageOneDataSuccess()

			//act
			val refreshRequest: PagingSource.LoadParams.Refresh<Int> =
				PagingSource.LoadParams.Refresh(
					key = ImageSearchApiTest.PAGE_NO_1,
					loadSize = ImageSearchApiTest.PAGE_LIMIT,
					placeholdersEnabled = false
				)
			val result = sutPagingSource.load(refreshRequest)

			//verify
			result shouldEqual PagingSource.LoadResult.Page(
				data = (imageSearchResponseOne as Resource.Success).data.hits?.map(mapper::map)
					.orEmpty(),
				prevKey = null,
				nextKey = ImageSearchApiTest.PAGE_NO_2
			)
		}
	}

	@Test
	fun `on Success second paging data and it should return second paging data`() {
		runBlocking {
			//arrange
			pageTwoDataSuccess()

			//act
			val refreshRequest: PagingSource.LoadParams.Refresh<Int> =
				PagingSource.LoadParams.Refresh(
					key = ImageSearchApiTest.PAGE_NO_2,
					loadSize = ImageSearchApiTest.PAGE_LIMIT,
					placeholdersEnabled = false
				)
			val result = sutPagingSource.load(refreshRequest)

			//verify
			result shouldEqual PagingSource.LoadResult.Page(
				data = (imageSearchResponseTwo as Resource.Success).data.hits?.map(mapper::map)
					.orEmpty(),
				prevKey = ImageSearchApiTest.PAGE_NO_1,
				nextKey = ImageSearchApiTest.PAGE_END
			)
		}
	}

	@Test
	fun `on Success end paging data and it should return end paging data`() {
		runBlocking {
			//arrange
			pageEndDataSuccess()

			//act
			val refreshRequest: PagingSource.LoadParams.Refresh<Int> =
				PagingSource.LoadParams.Refresh(
					key = ImageSearchApiTest.PAGE_END,
					loadSize = ImageSearchApiTest.PAGE_LIMIT,
					placeholdersEnabled = false
				)
			val result = sutPagingSource.load(refreshRequest)

			//verify
			result shouldEqual PagingSource.LoadResult.Page(
				data = (imageSearchResponseEnd as Resource.Success).data.hits?.map(mapper::map)
					.orEmpty(),
				prevKey = ImageSearchApiTest.PAGE_NO_2,
				nextKey = null
			)
		}
	}

	@Test
	fun `on empty response_paging source should return empty paging data`() {
		runBlocking {
			//arrange
			emptyListResponse()

			//act
			val refreshRequest: PagingSource.LoadParams.Refresh<Int> =
				PagingSource.LoadParams.Refresh(
					key = ImageSearchApiTest.PAGE_NO_1,
					loadSize = ImageSearchApiTest.PAGE_LIMIT,
					placeholdersEnabled = false
				)
			val result = sutPagingSource.load(refreshRequest)

			//verify
			result shouldEqual PagingSource.LoadResult.Page(
				data = listOf(),
				prevKey = null,
				nextKey = null
			)
		}
	}

	@Test
	fun `on error response_paging source should return error paging data`() {
		runBlocking {
			//arrange
			errorResponse()

			//act
			val refreshRequest: PagingSource.LoadParams.Refresh<Int> =
				PagingSource.LoadParams.Refresh(
					key = ImageSearchApiTest.PAGE_NO_1,
					loadSize = ImageSearchApiTest.PAGE_LIMIT,
					placeholdersEnabled = false
				)
			val result = sutPagingSource.load(refreshRequest)

			//verify
			assertThat(result).isInstanceOf(PagingSource.LoadResult.Error::class.java)
			(result as PagingSource.LoadResult.Error).throwable.message shouldEqual ImageSearchApiTest.ERROR_RESPONSE
		}
	}

	private fun pageOneDataSuccess() {
		runBlocking {
			mockRemoteSource.searchImages(any(), any(), any()) returns imageSearchResponseOne
		}
	}

	private fun pageTwoDataSuccess() {
		runBlocking {
			mockRemoteSource.searchImages(any(), any(), any()) returns imageSearchResponseTwo
		}
	}

	private fun pageEndDataSuccess() {
		runBlocking {
			mockRemoteSource.searchImages(any(), any(), any()) returns imageSearchResponseEnd
		}
	}

	private fun emptyListResponse() {
		runBlocking {
			mockRemoteSource.searchImages(any(), any(), any()) returns Resource.Success(
				ImageSearchResponse(listOf(), 0, 0)
			)
		}
	}

	private fun errorResponse() {
		runBlocking {
			mockRemoteSource.searchImages(any(), any(), any()) returns Resource.Error(
				RequestException(ImageSearchApiTest.ERROR_RESPONSE),
				0
			)
		}
	}
}