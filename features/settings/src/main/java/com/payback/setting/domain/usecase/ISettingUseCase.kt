package com.payback.setting.domain.usecase

import com.payback.setting.domain.model.ThemeModel

/**
 * Created By Rafiqul Hasan
 */
interface ISettingUseCase {
	fun getAvailableThemeMode(): List<ThemeModel>
}