package com.payback.imagesearchapp

import com.payback.core.model.ThemeMode
import com.payback.preference.usecase.ISettingPreference
import com.payback.testutil.returns
import com.payback.testutil.shouldEqual
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Rafiqul Hasan
 */
@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {
	@get:Rule
	val dispatcherRule = com.payback.testutil.MainDispatcherRule()

	@Mock
	lateinit var mockPreference: ISettingPreference

	private lateinit var sutMainViewModel: MainViewModel

	@Before
	fun setUp() {
		sutMainViewModel = MainViewModel(mockPreference)
	}

	@Test
	fun `get Selected ThemeMode should return correct ThemeData`() = runTest {
		//Arrange
		successSaveThemeData()

		//act
		sutMainViewModel.getSelectedTheme()

		//Assert
		val response = sutMainViewModel.selectedTheme.first()
		response shouldEqual ThemeMode.DARK
	}

	private fun successSaveThemeData() {
		runBlocking {
			mockPreference.getSelectedThemeMode(ThemeMode.DEFAULT) returns ThemeMode.DARK
		}
	}
}
