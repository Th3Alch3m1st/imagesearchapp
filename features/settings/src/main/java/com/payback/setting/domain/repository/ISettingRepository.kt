package com.payback.setting.domain.repository

import com.payback.setting.domain.model.ThemeModel

/**
 * Created By Rafiqul Hasan
 */
interface ISettingRepository {
	fun getAvailableThemeMode(): List<ThemeModel>
}