package com.payback.imagesearch.utils

import com.payback.core.network.Resource
import com.payback.imagesearch.data.api.ImageSearchApiTest
import com.payback.imagesearch.data.dto.ImageSearchResponse
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import java.io.IOException
import java.util.concurrent.AbstractExecutorService
import java.util.concurrent.ExecutorService
import java.util.concurrent.TimeUnit

/**
 * Created By Rafiqul Hasan
 */
object TestUtils {
    @Throws(IOException::class)
    private fun readFileToString(contextClass: Class<*>, fileName: String): String {
        contextClass.getResourceAsStream(fileName)
            ?.bufferedReader().use {
                val jsonString = it?.readText() ?: ""
                it?.close()
                return jsonString
            }
    }

    @Throws(IOException::class)
    fun mockResponse(fileName: String): MockResponse {
        return MockResponse().setChunkedBody(
            readFileToString(TestUtils::class.java, "/$fileName"),
            32
        )
    }

    fun getImageSearchPagingData(fileName: String): Resource<ImageSearchResponse> {
        val moshi = Moshi.Builder()
            .build()
        val jsonAdapter: JsonAdapter<ImageSearchResponse> = moshi.adapter(ImageSearchResponse::class.java)
        val jsonString = readFileToString(TestUtils::class.java, "/$fileName")
        return Resource.Success(jsonAdapter.fromJson(jsonString)!!)
    }

    fun getQueryMap(pageNo: Int, pageLimit: Int = ImageSearchApiTest.PAGE_LIMIT): Map<String, String> {
        return mapOf(
            "key" to "1234-1234567",
            "q" to ImageSearchApiTest.QUERY,
            "page" to pageNo.toString(),
            "per_page" to pageLimit.toString(),
        )
    }


    fun getOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .dispatcher(Dispatcher(immediateExecutorService()))
            .retryOnConnectionFailure(true).build()
    }

    private fun immediateExecutorService(): ExecutorService {
        return object : AbstractExecutorService() {
            override fun shutdown() {
            }

            override fun shutdownNow(): List<Runnable>? {
                return null
            }

            override fun isShutdown(): Boolean {
                return false
            }

            override fun isTerminated(): Boolean {
                return false
            }

            @Throws(InterruptedException::class)
            override fun awaitTermination(l: Long, timeUnit: TimeUnit): Boolean {
                return false
            }

            override fun execute(runnable: Runnable) {
                runnable.run()
            }
        }
    }
}