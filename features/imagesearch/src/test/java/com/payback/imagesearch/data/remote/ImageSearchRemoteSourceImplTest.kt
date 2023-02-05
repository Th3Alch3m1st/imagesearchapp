package com.payback.imagesearch.data.remote

import com.payback.core.network.Resource
import com.payback.imagesearch.data.api.ImageSearchApi
import com.payback.imagesearch.data.api.ImageSearchApiTest
import com.payback.imagesearch.utils.TestUtils
import com.payback.imagesearch.utils.TestUtils.getOkHttpClient
import com.payback.testutil.shouldEqual
import com.squareup.moshi.Moshi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Created by Rafiqul Hasan
 */
@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class ImageSearchRemoteSourceImplTest {
	@get:Rule
	val mockWebServer = MockWebServer()

	private lateinit var api: ImageSearchApi
	private lateinit var sutImageSearchRemoteSourceImpl: ImageSearchRemoteSourceImpl

	@Before
	fun setUp() {
		val moshi = Moshi.Builder()
			.build()

		api = Retrofit.Builder()
			.baseUrl(mockWebServer.url("/"))
			.addConverterFactory(MoshiConverterFactory.create(moshi))
			.client(getOkHttpClient())
			.build()
			.create(ImageSearchApi::class.java)

		sutImageSearchRemoteSourceImpl = ImageSearchRemoteSourceImpl(api, UnconfinedTestDispatcher())
	}

	@After
	fun shutDown() {
		mockWebServer.shutdown()
	}

	@Test
	fun `searchImage for first call should return first paging Data`() {
		runBlocking {
			//Arrange
			mockWebServer.enqueue(TestUtils.mockResponse(ImageSearchApiTest.PAGE_1_DATA))

			// Act
			val response = sutImageSearchRemoteSourceImpl.searchImages(ImageSearchApiTest.QUERY,ImageSearchApiTest.PAGE_NO_1,ImageSearchApiTest.PAGE_LIMIT)

			// Assert
			(response as Resource.Success).data.totalHits shouldEqual ImageSearchApiTest.TOTAL_ITEM
			response.data.hits?.size shouldEqual ImageSearchApiTest.PAGE_LIMIT
			response.data.hits?.get(0)?.id shouldEqual ImageSearchApiTest.PAGE_1_0_INDEX_ID
		}
	}

	@Test
	fun `searchImage for second call should return second paging Data`() {
		runBlocking {
			//Arrange
			mockWebServer.enqueue(TestUtils.mockResponse(ImageSearchApiTest.PAGE_2_DATA))

			// Act
			val response = sutImageSearchRemoteSourceImpl.searchImages(ImageSearchApiTest.QUERY,ImageSearchApiTest.PAGE_NO_2,ImageSearchApiTest.PAGE_LIMIT)

			// Assert
			(response as Resource.Success).data.totalHits shouldEqual ImageSearchApiTest.TOTAL_ITEM
			response.data.hits?.size shouldEqual ImageSearchApiTest.PAGE_LIMIT
			response.data.hits?.get(0)?.id shouldEqual ImageSearchApiTest.PAGE_2_0_INDEX_ID
		}
	}

	@Test
	fun `searchImage for end call should return end paging Data`() {
		runBlocking {
			//Arrange
			mockWebServer.enqueue(TestUtils.mockResponse(ImageSearchApiTest.PAGE_END_DATA))

			// Act
			val response = sutImageSearchRemoteSourceImpl.searchImages(ImageSearchApiTest.QUERY,ImageSearchApiTest.PAGE_END,ImageSearchApiTest.PAGE_LIMIT)

			// Assert
			(response as Resource.Success).data.totalHits shouldEqual ImageSearchApiTest.TOTAL_ITEM
			response.data.hits?.size shouldEqual ImageSearchApiTest.PAGE_END_SIZE
			response.data.hits?.get(0)?.id shouldEqual ImageSearchApiTest.PAGE_END_0_INDEX_ID
		}
	}
}