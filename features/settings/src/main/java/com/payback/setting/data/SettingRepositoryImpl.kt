package com.payback.setting.data

import com.payback.core.model.ThemeMode
import com.payback.setting.domain.model.ThemeModel
import com.payback.setting.domain.repository.ISettingRepository
import javax.inject.Inject

/**
 * Created By Rafiqul Hasan
 */
class SettingRepositoryImpl @Inject constructor() :
	ISettingRepository {
	override fun getAvailableThemeMode(): List<ThemeModel> {
		return listOf(
			ThemeModel("System", themeMode = ThemeMode.DEFAULT),
			ThemeModel("Light", themeMode = ThemeMode.LIGHT),
			ThemeModel("Dark", themeMode = ThemeMode.DARK),
		)
	}
}