package com.payback.core.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by Rafiqul Hasan
 */
@Parcelize
data class ImageUIModel(
	val id: Int,
	val thumbnail: String,
	val imageUrl: String,
	val sizeRatio:Double,
	val tags: List<String>,
	val numberOfLikes: Int,
	val numberOfDownloads: Int,
	val numberOfComments: Int,
	val username: String,
	val userImage: String
): Parcelable