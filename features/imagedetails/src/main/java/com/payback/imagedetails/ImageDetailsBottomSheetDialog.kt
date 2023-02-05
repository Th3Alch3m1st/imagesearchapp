package com.payback.imagedetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.payback.core.databinding.LayoutChipBinding
import com.payback.core.dialog.BaseBottomSheetDialog
import com.payback.feature.imagedetails.R
import com.payback.feature.imagedetails.databinding.BottomSheetDialogImageDetailsBinding

/**
 * Created by Rafiqul Hasan
 */
class ImageDetailsBottomSheetDialog : BaseBottomSheetDialog<BottomSheetDialogImageDetailsBinding>(){
	companion object{
		const val ARG_KEY = "imageInfo"
	}
	private val navArgs:ImageDetailsBottomSheetDialogArgs by navArgs()

	override val layoutResourceId: Int
		get() = R.layout.bottom_sheet_dialog_image_details

	override val bottomSheetBehavior: Int
		get() = BottomSheetBehavior.STATE_EXPANDED

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		dataBinding.imageInfo = navArgs.imageInfo
		initView()
	}

	private fun initView() {
		dataBinding.ivCloseBtn.setOnClickListener {
			dismiss()
		}

		for (tag in navArgs.imageInfo.tags) {
			val bindingChip = LayoutChipBinding.inflate(LayoutInflater.from(requireContext()))
			with(bindingChip) {
				chip.text = tag
				chip.isClickable = false
				dataBinding.cgTags.addView(chip)
			}
		}
	}
}