package com.payback.imagesearch.fakeremotesource

import com.payback.core.network.RequestException
import com.payback.core.network.Resource
import com.payback.imagesearch.data.dto.ImageResponse
import com.payback.imagesearch.data.dto.ImageSearchResponse
import com.payback.imagesearch.data.remote.ImageSearchRemoteSource
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Rafiqul Hasan
 */
@Singleton
class FakeImageSearchRemoteSourceImpl @Inject constructor() : ImageSearchRemoteSource {
	companion object {
		const val TRIGGERED_EMPTY_RESPONSE = "emptyasdg"
		const val MSG_ERROR = "Invalid Token"
		const val MSG_EMPTY = "Nothing Found, try again with different query"

	}

	var isTestError = false
	var isRetryTest = false

	override suspend fun searchImages(
		query: String,
		pageNumber: Int,
		pageLimit: Int
	): Resource<ImageSearchResponse> {

		if (isTestError) {
			return Resource.Error(RequestException(MSG_ERROR), 0)
		} else if (query.equals(TRIGGERED_EMPTY_RESPONSE,true)) {
			return Resource.Success(ImageSearchResponse())
		} else {
			val response = if (isRetryTest) {
				ImageSearchResponse(
					total = 100,
					totalHits = 100,
					hits = getImages(pageLimit)
				)
			} else {
				ImageSearchResponse(
					total = pageLimit,
					totalHits = pageLimit,
					hits = getImages(pageLimit)
				)
			}
			return Resource.Success(response)
		}
	}


	private fun getImages(pageLimit: Int): List<ImageResponse> {
		val list = mutableListOf<ImageResponse>()
		for (i in 0 until pageLimit) {
			val image = if (i % 5 == 0) {
				getImage(
					"https://cdn.pixabay.com/photo/2015/11/16/22/39/balloons-1046658_150.jpg",
					150,
					100
				)
			} else if (i % 5 == 1) {
				getImage(
					"https://cdn.pixabay.com/photo/2017/01/16/19/40/mountains-1985027_150.jpg",
					150,
					90
				)
			} else if (i % 5 == 2) {
				getImage(
					"https://cdn.pixabay.com/photo/2017/03/29/11/29/nepal-2184940_150.jpg",
					151,
					70
				)
			} else if (i % 5 == 3) {
				getImage(
					"https://cdn.pixabay.com/photo/2018/03/10/17/16/woman-3214594_150.jpg",
					150,
					86
				)
			} else {
				getImage(
					"https://cdn.pixabay.com/photo/2017/05/20/20/22/clouds-2329680_150.jpg",
					150,
					84
				)
			}
			list.add(image)
		}
		return list
	}

	var id = 0
	private fun getImage(image: String, previewWidth: Int, previewHeight: Int): ImageResponse {
		id++
		return ImageResponse(
			id = id,
			pageURL = "https://pixabay.com/photos/balloons-heart-sky-clouds-love-1046658/",
			type = "photo",
			tags = "balloons, heart, sky",
			previewURL = image,
			previewWidth = previewWidth,
			previewHeight = previewHeight,
			webformatURL = "https://pixabay.com/get/ga0a14f0265692049b5c8d5143f0bed74e509546268f14a78a89bd18d1cbd7de7c55c37af6207592b59bc09d24f9ea962686ef82bb927dba01e3ea736d126fe92_640.jpg",
			webformatWidth = 640,
			webformatHeight = 426,
			largeImageURL = "https://pixabay.com/get/g9526c88103216b27b2b4cabb602f4456147edd342915387d83e5fa18eb471b58ce4a50e0220c4ecf2f5ca8997d3250df846eee10fdfe5b514b474c1efd46e960_1280.jpg",
			imageWidth = 5184,
			imageHeight = 3456,
			imageSize = 2964496,
			views = 1246508,
			downloads = 565795,
			collections = 1556,
			likes = 1904,
			comments = 364,
			userId = 1553824,
			user = "Peggy_Marco",
			userImageURL = "https://cdn.pixabay.com/user/2020/01/07/15-43-03-893_250x250.jpg"
		)
	}
}