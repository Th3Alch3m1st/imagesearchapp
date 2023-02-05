package com.payback.imagesearch.presentation

import androidx.paging.PagingData
import com.payback.core.mapper.Mapper
import com.payback.core.network.Resource
import com.payback.imagesearch.data.api.ImageSearchApiTest
import com.payback.imagesearch.data.dto.ImageResponse
import com.payback.core.model.ImageUIModel
import com.payback.imagesearch.domain.usecase.ImageSearchUseCase
import com.payback.imagesearch.mapper.ImageResponseToImageUIModelMapper
import com.payback.imagesearch.utils.TestUtils
import com.payback.testutil.MainDispatcherRule
import com.payback.testutil.returns
import com.payback.testutil.shouldEqual
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
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
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ImageSearchViewModelTest {
	@get:Rule
	val dispatcherRule = MainDispatcherRule()

	@Mock
	lateinit var mockUseCase: ImageSearchUseCase

	private lateinit var sutViewModel: ImageSearchViewModel
	private lateinit var mapper: Mapper<ImageResponse, ImageUIModel>
	private lateinit var imageSearchResponseOne: Flow<PagingData<ImageUIModel>>
	private lateinit var imageSearchResponseTwo: Flow<PagingData<ImageUIModel>>
	private lateinit var emptyResponse: Flow<PagingData<ImageUIModel>>

	@Before
	fun setUp() {
		sutViewModel = ImageSearchViewModel(useCase = mockUseCase)

		mapper = ImageResponseToImageUIModelMapper()

		imageSearchResponseOne = flowOf(
			PagingData.from(
				(TestUtils.getImageSearchPagingData(ImageSearchApiTest.PAGE_1_DATA) as Resource.Success).data.hits?.map(
					mapper::map
				).orEmpty()
			)
		)

		imageSearchResponseTwo = flowOf(
			PagingData.from(
				(TestUtils.getImageSearchPagingData(ImageSearchApiTest.PAGE_2_DATA) as Resource.Success).data.hits?.map(
					mapper::map
				).orEmpty()
			)
		)
		emptyResponse = flowOf(
			PagingData.from(listOf())
		)
	}

	@Test
	fun `check argument pass correctly for first paging in searchImages fun`() = runTest {
		//Arrange
		pageOneDataSuccess()
		val acString = argumentCaptor<String>()
		val acInt = argumentCaptor<Int>()

		//act
		//this should trigger
		sutViewModel.imageSearchResultTest.first()

		//verify
		Mockito.verify(mockUseCase).searchImage(acString.capture(), acInt.capture())

		acString.firstValue shouldEqual ImageSearchViewModel.DEFAULT_QUERY
		acInt.firstValue shouldEqual ImageSearchViewModel.PAGE_SIZE
	}

	@Test
	fun `searchImage should return correct paging Data`() = runTest {
		//Arrange
		pageOneDataSuccess()

		//Act
		sutViewModel.searchImage(ImageSearchApiTest.QUERY)


		//Verify
		val result  = sutViewModel.imageSearchResultTest.first()
		result shouldEqual imageSearchResponseOne.first()
	}

	@Test
	fun `searchImage called two times and it should return correct paging Dat and use case was called two times`() = runTest{
		//search first time
		//Arrange
		pageOneDataSuccess()
		val acString = argumentCaptor<String>()
		val acInt = argumentCaptor<Int>()

		//Act
		sutViewModel.searchImage(ImageSearchApiTest.QUERY)
		sutViewModel.imageSearchResultTest.first()

		//wait
		Thread.sleep(100)

		//search second time
		//Arrange
		pageTwoDataSuccess()
		sutViewModel.searchImage(ImageSearchApiTest.QUERY)

		//Act
		val result  = sutViewModel.imageSearchResultTest.first()

		//Verify
		Mockito.verify(mockUseCase, Mockito.times(2)).searchImage(acString.capture(),acInt.capture())
		result shouldEqual imageSearchResponseTwo.first()
	}

	@Test
	fun `searchImage with random query for empty result and should return empty paging Data`() = runTest {
		//Arrange
		emptyListResponse()

		//Act
		sutViewModel.searchImage(ImageSearchApiTest.QUERY)


		//Verify
		val result  = sutViewModel.imageSearchResultTest.first()
		result shouldEqual emptyResponse.first()
	}

	private fun pageOneDataSuccess() {
		runBlocking {
			mockUseCase.searchImage(any(), any()) returns imageSearchResponseOne
		}
	}

	private fun pageTwoDataSuccess() {
		runBlocking {
			mockUseCase.searchImage(any(), any()) returns imageSearchResponseTwo
		}
	}

	private fun emptyListResponse() {
		runBlocking {
			mockUseCase.searchImage(any(), any()) returns emptyResponse
		}
	}
}