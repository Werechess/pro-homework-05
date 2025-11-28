package ru.otus.cryptosample.coins.feature.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.otus.cryptosample.coins.feature.CoinState
import ru.otus.cryptosample.databinding.ItemCoinBinding

class CarouselAdapter : ListAdapter<CoinState, CoinViewHolder>(CarouselDiffUtil) {

    override fun getItemCount(): Int = currentList.size

    override fun getItemViewType(position: Int): Int = CoinsAdapter.VIEW_TYPE_COIN

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinViewHolder {
        val binding = ItemCoinBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val params = binding.root.layoutParams
            ?: RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.WRAP_CONTENT,
                RecyclerView.LayoutParams.WRAP_CONTENT
            )
        val screenWidth = parent.resources.displayMetrics.widthPixels
        params.width = (screenWidth / 2) - 16

        binding.root.layoutParams = params
        return CoinViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CoinViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onBindViewHolder(holder: CoinViewHolder, position: Int, payloads: List<Any>) {
        holder.bind(getItem(position), payloads)
    }
}

object CarouselDiffUtil : DiffUtil.ItemCallback<CoinState>() {
    override fun areItemsTheSame(old: CoinState, new: CoinState) = old.id == new.id
    override fun areContentsTheSame(old: CoinState, new: CoinState) = old == new
}