package com.payback.imagesearch.mapper

import com.payback.core.mapper.Mapper
import com.payback.imagesearch.data.dto.ImageResponse
import com.payback.core.model.ImageUIModel

/**
 * Created by Rafiqul Hasan
 */
class ImageResponseToImageUIModelMapper : Mapper<ImageResponse, ImageUIModel> {
	override fun map(input: ImageResponse): ImageUIModel {
		val previewWidth = input.previewWidth ?: 0
		val previewHeight = input.previewHeight ?: 0
		val sizeRatio = if (previewHeight > 0) {
			1.0 * previewWidth / previewHeight
		} else {
			0.0
		}
		return ImageUIModel(
			id = input.id ?: 0,
			thumbnail = input.previewURL ?: "",
			imageUrl = input.largeImageURL ?: "",
			sizeRatio = sizeRatio,
			tags = if (input.tags.isNullOrEmpty()) listOf() else input.tags.split(",")
				.map { it.trim() },
			numberOfLikes = input.likes ?: 0,
			numberOfComments = input.comments ?: 0,
			numberOfDownloads = input.downloads ?: 0,
			username = input.user ?: "",
			userImage = input.userImageURL ?: ""
		)
	}
}