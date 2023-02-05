package com.payback.imagesearch.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ImageSearchResponse(

	@Json(name="hits")
	val hits: List<ImageResponse>? = null,

	@Json(name="total")
	val total: Int? = null,

	@Json(name="totalHits")
	val totalHits: Int? = null
)

@JsonClass(generateAdapter = true)
data class ImageResponse(

	@Json(name="webformatHeight")
	val webformatHeight: Int? = null,

	@Json(name="imageWidth")
	val imageWidth: Int? = null,

	@Json(name="previewHeight")
	val previewHeight: Int? = null,

	@Json(name="webformatURL")
	val webformatURL: String? = null,

	@Json(name="userImageURL")
	val userImageURL: String? = null,

	@Json(name="previewURL")
	val previewURL: String? = null,

	@Json(name="comments")
	val comments: Int? = null,

	@Json(name="type")
	val type: String? = null,

	@Json(name="imageHeight")
	val imageHeight: Int? = null,

	@Json(name="tags")
	val tags: String? = null,

	@Json(name="previewWidth")
	val previewWidth: Int? = null,

	@Json(name="downloads")
	val downloads: Int? = null,

	@Json(name="collections")
	val collections: Int? = null,

	@Json(name="user_id")
	val userId: Int? = null,

	@Json(name="largeImageURL")
	val largeImageURL: String? = null,

	@Json(name="pageURL")
	val pageURL: String? = null,

	@Json(name="id")
	val id: Int? = null,

	@Json(name="imageSize")
	val imageSize: Int? = null,

	@Json(name="webformatWidth")
	val webformatWidth: Int? = null,

	@Json(name="user")
	val user: String? = null,

	@Json(name="views")
	val views: Int? = null,

	@Json(name="likes")
	val likes: Int? = null
)
