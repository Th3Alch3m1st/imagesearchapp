package com.payback.imagesearch.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.payback.core.databinding.LayoutChipBinding
import com.payback.core.util.setSafeOnClickListener
import com.payback.feature.imagesearch.databinding.SingleItemImageBinding
import com.payback.core.model.ImageUIModel

/**
 * Created By Rafiqul Hasan
 */
class ImagesAdapter(val onItemClicked: (ImageUIModel) -> Unit?) :
	PagingDataAdapter<ImageUIModel, ImagesAdapter.ImageViewHolder>(ImageComparator) {

	private var itemViewWidth: Int = 0
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
		return ImageViewHolder(
			SingleItemImageBinding.inflate(
				LayoutInflater.from(parent.context),
				parent,
				false
			)
		)
	}

	override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
		val imageItem = getItem(position)
		itemViewWidth = holder.itemView.width
		imageItem?.let { item ->
			holder.bind(item, itemViewWidth)
		}

		holder.binding.ivImage.setSafeOnClickListener {
			imageItem?.let {
				onItemClicked(imageItem)
			}
		}
	}

	inner class ImageViewHolder(val binding: SingleItemImageBinding) :
		RecyclerView.ViewHolder(binding.root) {
		fun bind(imageUIModel: ImageUIModel, width: Int) = with(binding) {
			if (width > 0 && imageUIModel.sizeRatio > 0.0) {
				binding.ivImage.layoutParams.run {
					this.width = width
					this.height = (width / imageUIModel.sizeRatio).toInt()
				}
			}
			imageUrl = imageUIModel.thumbnail
			userImage = imageUIModel.userImage
			chipGroup.removeAllViews()
			chipGroup.invalidate()
			for (tag in imageUIModel.tags.take(2)) {
				val bindingChip =
					LayoutChipBinding.inflate(LayoutInflater.from(binding.root.context))
				with(bindingChip) {
					chip.text = tag
					chip.isClickable = false
					chipGroup.addView(chip)
				}
			}

			chip.text = imageUIModel.username
		}
	}

	private object ImageComparator : DiffUtil.ItemCallback<ImageUIModel>() {
		override fun areItemsTheSame(oldItem: ImageUIModel, newItem: ImageUIModel) =
			oldItem.id == newItem.id

		override fun areContentsTheSame(oldItem: ImageUIModel, newItem: ImageUIModel) =
			oldItem == newItem
	}
}