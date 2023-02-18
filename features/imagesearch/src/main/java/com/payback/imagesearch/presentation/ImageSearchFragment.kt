package com.payback.imagesearch.presentation

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.payback.core.fragment.BaseFragment
import com.payback.core.model.ImageUIModel
import com.payback.core.util.DebouncingQueryTextListener
import com.payback.core.util.gone
import com.payback.core.util.launchAndCollectIn
import com.payback.core.util.show
import com.payback.feature.imagesearch.R
import com.payback.feature.imagesearch.databinding.FragmentImageSearchBinding
import com.payback.imagedetails.ImageDetailsBottomSheetDialog
import com.payback.imagesearch.presentation.adapter.ImagesAdapter
import com.payback.imagesearch.presentation.adapter.PagingLoadStateAdapter
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by Rafiqul Hasan
 */
@AndroidEntryPoint
class ImageSearchFragment : BaseFragment<FragmentImageSearchBinding>() {
	private val viewModel: ImageSearchViewModel by viewModels()

	override val layoutResourceId: Int
		get() = R.layout.fragment_image_search

	private lateinit var imagesAdapter: ImagesAdapter
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		//init recyclerview adapter
		imagesAdapter = ImagesAdapter { imageInfo ->
			openImageDetailsFragment(imageInfo)
		}
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		setupToolbar()
		setupMenu()
		initSearchView()
		initAdapterAndRecyclerView()
		initObserver()
	}


	private fun setupToolbar() {
		dataBinding.toolbar.title = getString(R.string.title_image_search)
		fragmentCommunicator?.setActionBar(dataBinding.toolbar, false)
	}

	private fun setupMenu() {
		(requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
			override fun onPrepareMenu(menu: Menu) {

			}

			override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
				menuInflater.inflate(R.menu.menu, menu)
			}

			override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
				when (menuItem.itemId) {
					R.id.action_setting -> {
						findNavController().navigate(R.id.action_fragment_image_search_to_setting)
					}
					else -> {

					}
				}
				return true
			}
		}, viewLifecycleOwner, Lifecycle.State.RESUMED)
	}

	private fun initSearchView() {
		with(dataBinding) {
			searchView.setOnQueryTextListener(
				DebouncingQueryTextListener(
					this@ImageSearchFragment.lifecycle
				) { newText ->
					viewModel.searchImage(newText)
				}
			)
			searchView.clearFocus()
		}
	}

	private fun initAdapterAndRecyclerView() {
		with(dataBinding) {
			rvSearchResult.apply {
				layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
			}

			with(imagesAdapter) {
				rvSearchResult.adapter = withLoadStateFooter(
					footer = PagingLoadStateAdapter(imagesAdapter)
				)
				if (itemCount > 0) {
					viewEmpty.root.gone()
				}
			}
		}

	}

	private fun initObserver() {
		viewModel.imageSearchResult.launchAndCollectIn(
			viewLifecycleOwner,
			Lifecycle.State.STARTED
		) {
			imagesAdapter.submitData(it)
		}

		imagesAdapter.loadStateFlow.launchAndCollectIn(
			viewLifecycleOwner,
			Lifecycle.State.STARTED
		) { loadState ->
			//for initial loading dialog
			val isLoading = loadState.refresh is LoadState.Loading
			if (isLoading) {
				fragmentCommunicator?.showLoader()
			} else {
				fragmentCommunicator?.hideLoader()
			}

			//check if first page response is empty
			val isListEmpty =
				loadState.refresh is LoadState.NotLoading && imagesAdapter.itemCount == 0
			if (isListEmpty) {
				dataBinding.viewEmpty.root.show()
				dataBinding.viewEmpty.tvTitle.text = getString(R.string.msg_nothing_found)
				dataBinding.viewEmpty.btnTryAgain.gone()
			} else {
				dataBinding.viewEmpty.root.gone()
			}

			// Show the error result if initial load fails.
			val isInitialLoadOrRefreshFail = loadState.source.refresh is LoadState.Error
			if (isInitialLoadOrRefreshFail) {
				val error = (loadState.refresh as LoadState.Error).error
				showHideErrorUI(
					error.message ?: getString(R.string.msg_unknown_error),
					getString(com.payback.core.R.string.retry)
				) {
					imagesAdapter.retry()
				}
			}

		}
	}

	private fun showHideErrorUI(
		title: String,
		buttonText: String,
		clickListener: () -> Unit
	) {
		dataBinding.viewEmpty.root.show()
		dataBinding.viewEmpty.tvTitle.text = title
		dataBinding.viewEmpty.btnTryAgain.text = buttonText
		dataBinding.viewEmpty.btnTryAgain.setOnClickListener {
			clickListener.invoke()
		}
	}

	private fun openImageDetailsFragment(imageInfo: ImageUIModel) {
		val bundle = Bundle().apply {
			putParcelable(ImageDetailsBottomSheetDialog.ARG_KEY, imageInfo)
		}

		findNavController().navigate(R.id.action_fragment_image_search_to_image_details, bundle)
	}
}