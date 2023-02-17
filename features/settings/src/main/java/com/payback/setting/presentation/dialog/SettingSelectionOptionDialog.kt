package com.payback.setting.presentation.dialog

import android.os.Bundle
import android.view.View
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs
import com.payback.core.dialog.BaseBottomSheetDialog
import com.payback.core.util.gone
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.payback.feature.setting.R
import com.payback.feature.setting.databinding.DialogBottomSheetSettingOptionSelectionBinding
import com.payback.feature.setting.databinding.LayoutUnitItemBinding

/**
 * Created By Rafiqul Hasan
 */
class SettingSelectionOptionDialog : BaseBottomSheetDialog<DialogBottomSheetSettingOptionSelectionBinding>() {
	companion object {
		const val REQUEST_KEY = "UNIT_INFO"
		const val ARG_SELECTED_SETTING = "SELECTED_SETTING"
		const val ARG_TYPE = "ARG_TYPE"
		const val TYPE_THEME = "THEME"
	}

	private val args: SettingSelectionOptionDialogArgs by navArgs()

	override val layoutResourceId: Int
		get() = R.layout.dialog_bottom_sheet_setting_option_selection

	override val bottomSheetBehavior: Int
		get() = BottomSheetBehavior.STATE_HALF_EXPANDED

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		initView()
	}

	private fun initView() {
		for (i in 0 until args.list.size) {
			val binding = LayoutUnitItemBinding.inflate(layoutInflater)
			binding.tvSelectedSetting.text = args.list[i]
			if (i == args.list.size - 1) {
				binding.divider.gone()
			}
			binding.root.setOnClickListener {
				setFragmentResult(
					REQUEST_KEY,
					Bundle().apply {
						putString(ARG_TYPE, args.type)
						putString(ARG_SELECTED_SETTING, args.list[i])
					}
				)
				dismiss()
			}
			dataBinding.llContainer.addView(binding.root)
		}
	}
}