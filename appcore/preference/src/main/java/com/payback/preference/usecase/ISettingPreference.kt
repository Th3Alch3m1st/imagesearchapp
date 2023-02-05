package com.payback.preference.usecase

import com.payback.core.model.ThemeMode

/**
 * Created By Rafiqul Hasan
 */
interface ISettingPreference {
	suspend fun getSelectedThemeMode(default: ThemeMode): ThemeMode
	suspend fun saveThemeModePreference(themeMode: ThemeMode)
}