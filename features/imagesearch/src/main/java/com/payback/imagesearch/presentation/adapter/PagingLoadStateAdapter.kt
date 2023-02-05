package com.payback.imagesearch.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.payback.feature.imagesearch.R
import com.payback.feature.imagesearch.databinding.ItemNetworkStateBinding

/**
 * Created By Rafiqul Hasan
 */
class PagingLoadStateAdapter<T : Any, VH : RecyclerView.ViewHolder>(
    private val adapter: PagingDataAdapter<T, VH>
) : LoadStateAdapter<PagingLoadStateAdapter.NetworkStateItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState) =
        NetworkStateItemViewHolder(
            ItemNetworkStateBinding.bind(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_network_state, parent, false)
            )
        ) { adapter.retry() }

    override fun onBindViewHolder(holder: NetworkStateItemViewHolder, loadState: LoadState) =
        holder.bind(loadState)

    class NetworkStateItemViewHolder(
        private val binding: ItemNetworkStateBinding,
        private val retryCallback: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.btnPagingRetry.setOnClickListener { retryCallback() }
        }

        fun bind(loadState: LoadState) {
            with(binding) {
                progressBarRv.isVisible = loadState is LoadState.Loading
                btnPagingRetry.isVisible = loadState is LoadState.Error
                tvPagingErrorMsg.isVisible =
                    !(loadState as? LoadState.Error)?.error?.message.isNullOrBlank()
                tvPagingErrorMsg.text = (loadState as? LoadState.Error)?.error?.message
            }
        }
    }
}