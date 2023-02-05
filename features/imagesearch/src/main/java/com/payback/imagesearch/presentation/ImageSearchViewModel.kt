package com.payback.imagesearch.presentation

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.payback.imagesearch.domain.usecase.ImageSearchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Rafiqul Hasan
 */
@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ImageSearchViewModel @Inject constructor(
	private val useCase: ImageSearchUseCase
) : ViewModel() {
	companion object {
		const val PAGE_SIZE = 10
		const val DEFAULT_QUERY = "fruits"
	}

	private val searchActionStateFlow = MutableSharedFlow<String>()
	private val searchQueryFlow = searchActionStateFlow
		.map {
			it.trim()
		}.filterNot {
			it.isEmpty()
		}.distinctUntilChanged()
		.onStart { emit(DEFAULT_QUERY) }

	val imageSearchResult = searchQueryFlow
		.flatMapLatest {
			useCase.searchImage(it, PAGE_SIZE)
		}.cachedIn(viewModelScope)

	//cachedIn throw exception during unit test; cachedIn only for save paging state to survive orientation
	@VisibleForTesting
	val imageSearchResultTest = searchQueryFlow
		.flatMapLatest {
			useCase.searchImage(it, PAGE_SIZE)
		}

	fun searchImage(query: String) {
		viewModelScope.launch {
			searchActionStateFlow.emit(query)
		}
	}
}