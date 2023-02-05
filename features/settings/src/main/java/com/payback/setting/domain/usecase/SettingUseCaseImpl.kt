package com.payback.setting.domain.usecase

import com.payback.setting.domain.model.ThemeModel
import com.payback.setting.domain.repository.ISettingRepository
import javax.inject.Inject

/**
 * Created By Rafiqul Hasan
 */
class SettingUseCaseImpl @Inject constructor(private val repository: ISettingRepository) :
	ISettingUseCase {
	override fun getAvailableThemeMode(): List<ThemeModel> {
		return repository.getAvailableThemeMode()
	}
}