package com.payback.core.util

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.chip.Chip
import com.payback.core.R
import com.payback.core.glide.GlideApp

/**
 * Created By Rafiqul Hasan
 */

@BindingAdapter("image_url")
fun ImageView.loadImage(url: String?) {
	GlideApp.with(this)
		.load(url)
		.placeholder(R.drawable.placeholder)
		.error(R.drawable.placeholder)
		.into(this)
}

@BindingAdapter("avatar_url")
fun Chip.loadAvatar(url: String?) {
	if (url.isNullOrEmpty()) {
		chipIcon = ContextCompat.getDrawable(this.context, R.drawable.ic_user)
	} else {
		this.chipIcon = ContextCompat.getDrawable(this.context, R.drawable.ic_user)
		GlideApp.with(this)
			.load(url)
			.placeholder(R.drawable.ic_user)
			.error(R.drawable.ic_user)
			.circleCrop()
			.into(object : CustomTarget<Drawable>() {

				override fun onResourceReady(
					resource: Drawable,
					transition: Transition<in Drawable>?
				) {
					this@loadAvatar.chipIcon = resource
				}

				override fun onLoadCleared(placeholder: Drawable?) {
					this@loadAvatar.chipIcon = placeholder
				}

			}).onLoadFailed(ContextCompat.getDrawable(this.context, R.drawable.ic_user))
	}
}