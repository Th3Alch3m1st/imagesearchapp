package com.payback.imagesearch.data.api

import com.payback.imagesearch.utils.TestUtils
import com.payback.imagesearch.utils.TestUtils.getOkHttpClient
import com.payback.imagesearch.utils.TestUtils.getQueryMap
import com.payback.testutil.shouldEqual
import com.squareup.moshi.Moshi
import kotlinx.coroutines.runBlocking
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
@RunWith(JUnit4::class)
class ImageSearchApiTest {
	companion object {
		const val QUERY = "Blue Sky"
		const val ERROR_RESPONSE = "Invalid Token"

		const val PAGE_1_DATA = "page1.json"
		const val PAGE_2_DATA = "page2.json"
		const val PAGE_END_DATA = "pageEnd.json"

		const val PAGE_NO_1 = 1
		const val PAGE_NO_2 = 2
		const val PAGE_END = 3

		const val PAGE_1_0_INDEX_ID = 1046658
		const val PAGE_2_0_INDEX_ID = 2184940
		const val PAGE_END_0_INDEX_ID = 414199

		const val PAGE_LIMIT = 10
		const val PAGE_END_SIZE = 7
		const val TOTAL_ITEM = 27
	}

	@get:Rule
	val mockWebServer = MockWebServer()

	private lateinit var sutImageSearchApi: ImageSearchApi

	@Before
	fun setUp() {
		val moshi = Moshi.Builder()
			.build()

		sutImageSearchApi = Retrofit.Builder()
			.baseUrl(mockWebServer.url("/"))
			.addConverterFactory(MoshiConverterFactory.create(moshi))
			.client(getOkHttpClient())
			.build()
			.create(ImageSearchApi::class.java)
	}

	@After
	fun shutDown() {
		mockWebServer.shutdown()
	}

	@Test
	fun `searchImage for first call should return first paging Data`() {
		runBlocking {
			//Arrange
			mockWebServer.enqueue(TestUtils.mockResponse(PAGE_1_DATA))

			// Act
			val response = sutImageSearchApi.searchImages(getQueryMap(PAGE_NO_1))

			// Assert
			response.body()?.totalHits shouldEqual TOTAL_ITEM
			response.body()?.hits?.size shouldEqual PAGE_LIMIT
			response.body()?.hits?.get(0)?.id shouldEqual PAGE_1_0_INDEX_ID
		}
	}
	@Test
	fun `searchImage for second call should return second paging Data`() {
		runBlocking {
			//Arrange
			mockWebServer.enqueue(TestUtils.mockResponse(PAGE_2_DATA))

			// Act
			val response = sutImageSearchApi.searchImages(getQueryMap(PAGE_NO_2))

			// Assert
			response.body()?.totalHits shouldEqual TOTAL_ITEM
			response.body()?.hits?.size shouldEqual PAGE_LIMIT
			response.body()?.hits?.get(0)?.id shouldEqual PAGE_2_0_INDEX_ID
		}
	}

	@Test
	fun `searchImage for end call should return end paging Data`() {
		runBlocking {
			//Arrange
			mockWebServer.enqueue(TestUtils.mockResponse(PAGE_END_DATA))

			// Act
			val response = sutImageSearchApi.searchImages(getQueryMap(PAGE_END))

			// Assert
			response.body()?.totalHits shouldEqual TOTAL_ITEM
			response.body()?.hits?.size shouldEqual PAGE_END_SIZE
			response.body()?.hits?.get(0)?.id shouldEqual PAGE_END_0_INDEX_ID
		}
	}
}