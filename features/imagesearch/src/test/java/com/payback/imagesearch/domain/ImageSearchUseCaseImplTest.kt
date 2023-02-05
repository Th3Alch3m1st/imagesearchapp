package com.payback.imagesearch.domain

import androidx.paging.PagingData
import com.payback.core.mapper.Mapper
import com.payback.core.network.Resource
import com.payback.imagesearch.data.api.ImageSearchApiTest
import com.payback.imagesearch.data.dto.ImageResponse
import com.payback.core.model.ImageUIModel
import com.payback.imagesearch.domain.repository.ImageSearchRepository
import com.payback.imagesearch.domain.usecase.ImageSearchUseCaseImpl
import com.payback.imagesearch.mapper.ImageResponseToImageUIModelMapper
import com.payback.imagesearch.utils.TestUtils
import com.payback.testutil.returns
import com.payback.testutil.shouldEqual
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
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
class ImageSearchUseCaseImplTest {
	@Mock
	lateinit var mockRepository: ImageSearchRepository

	private lateinit var sutUseCase: ImageSearchUseCaseImpl

	private lateinit var imageSearchResponseOne: Flow<PagingData<ImageUIModel>>
	private lateinit var emptyResponse: Flow<PagingData<ImageUIModel>>

	private lateinit var mapper: Mapper<ImageResponse, ImageUIModel>

	@Before
	fun setup() {
		sutUseCase = ImageSearchUseCaseImpl(mockRepository)

		mapper = ImageResponseToImageUIModelMapper()

		imageSearchResponseOne = flowOf(
			PagingData.from(
				(TestUtils.getImageSearchPagingData(ImageSearchApiTest.PAGE_1_DATA) as Resource.Success).data.hits?.map(
					mapper::map
				).orEmpty()
			)
		)
		emptyResponse = flowOf(
			PagingData.from(listOf())
		)
	}

	@Test
	fun `check argument pass correctly for first paging in searchImages fun`() {
		runBlocking {
			//arrange
			pageOneDataSuccess()
			val acString = argumentCaptor<String>()
			val acInt = argumentCaptor<Int>()

			//act
			sutUseCase.searchImage(ImageSearchApiTest.QUERY, ImageSearchApiTest.PAGE_LIMIT)

			//verify
			Mockito.verify(mockRepository)
				.searchImage(acString.capture(), acInt.capture())
			acString.firstValue shouldEqual ImageSearchApiTest.QUERY
			acInt.firstValue shouldEqual ImageSearchApiTest.PAGE_LIMIT
		}
	}

	@Test
	fun `on Success first paging data and it should return first paging data`() {
		runBlocking {
			//arrange
			pageOneDataSuccess()

			//act
			val result =
				sutUseCase.searchImage(ImageSearchApiTest.QUERY, ImageSearchApiTest.PAGE_LIMIT)
					.first()

			//verify
			result shouldEqual imageSearchResponseOne.first()
		}
	}

	@Test
	fun `on empty response_paging source should return empty paging data`() {
		runBlocking {
			//arrange
			emptyListResponse()

			//act
			val result =
				sutUseCase.searchImage(ImageSearchApiTest.QUERY, ImageSearchApiTest.PAGE_LIMIT)
					.first()

			//verify
			result shouldEqual emptyResponse.first()
		}
	}

	private fun pageOneDataSuccess() {
		runBlocking {
			mockRepository.searchImage(any(), any()) returns imageSearchResponseOne
		}
	}

	private fun emptyListResponse() {
		runBlocking {
			mockRepository.searchImage(any(), any()) returns emptyResponse
		}
	}
}