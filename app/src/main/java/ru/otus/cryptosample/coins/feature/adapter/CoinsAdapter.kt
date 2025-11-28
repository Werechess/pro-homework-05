package ru.otus.cryptosample.coins.feature.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.otus.cryptosample.coins.feature.CoinCategoryState
import ru.otus.cryptosample.coins.feature.adapter.CoinsAdapterItem.CarouselItems
import ru.otus.cryptosample.coins.feature.adapter.CoinsAdapterItem.CategoryHeader
import ru.otus.cryptosample.coins.feature.adapter.CoinsAdapterItem.CoinItem
import ru.otus.cryptosample.databinding.ItemCarouselBinding
import ru.otus.cryptosample.databinding.ItemCategoryHeaderBinding
import ru.otus.cryptosample.databinding.ItemCoinBinding

class CoinsAdapter : ListAdapter<CoinsAdapterItem, RecyclerView.ViewHolder>(CoinDiffUtil) {

    companion object {
        const val VIEW_TYPE_CATEGORY = 0
        const val VIEW_TYPE_COIN = 1
        const val VIEW_TYPE_CAROUSEL = 2

        val sharedPool = RecyclerView.RecycledViewPool()
    }

    fun setData(categories: List<CoinCategoryState>) {
        val adapterItems = mutableListOf<CoinsAdapterItem>()

        categories.forEach { category ->
            adapterItems.add(CategoryHeader(category.name))
            when (category.coins.size) {
                in 0..10 -> {
                    category.coins.forEach { coin ->
                        adapterItems.add(CoinItem(coin))
                    }
                }

                else -> adapterItems.add(CarouselItems(category.coins))
            }
        }

        submitList(adapterItems)
    }

    override fun getItemCount(): Int = currentList.size

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is CategoryHeader -> VIEW_TYPE_CATEGORY
            is CoinItem -> VIEW_TYPE_COIN
            is CarouselItems -> VIEW_TYPE_CAROUSEL
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_CATEGORY -> CategoryHeaderViewHolder(
                ItemCategoryHeaderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            VIEW_TYPE_COIN -> {
                val binding =
                    ItemCoinBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                val params = binding.root.layoutParams
                    ?: RecyclerView.LayoutParams(
                        RecyclerView.LayoutParams.WRAP_CONTENT,
                        RecyclerView.LayoutParams.WRAP_CONTENT
                    )
                val screenWidth = parent.resources.displayMetrics.widthPixels
                params.width = (screenWidth / 2) - 16

                binding.root.layoutParams = params
                CoinViewHolder(binding)
            }

            VIEW_TYPE_CAROUSEL -> {
                CarouselViewHolder(
                    ItemCarouselBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ),
                    sharedPool
                )
            }

            else -> throw IllegalArgumentException("Unknown view type: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is CategoryHeader -> {
                (holder as CategoryHeaderViewHolder).bind(item.categoryName)
            }

            is CoinItem -> {
                (holder as CoinViewHolder).bind(item.coin)
            }

            is CarouselItems -> {
                (holder as CarouselViewHolder).bind(item.coins)
            }
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: List<Any>
    ) {
        when (val item = getItem(position)) {
            is CategoryHeader -> {
                (holder as CategoryHeaderViewHolder).bind(item.categoryName)
            }

            is CoinItem -> {
                if (payloads.isNotEmpty()) {
                    (holder as CoinViewHolder).bind(item.coin, payloads)
                } else {
                    (holder as CoinViewHolder).bind(item.coin)
                }
            }

            is CarouselItems -> {
                (holder as CarouselViewHolder).bind(item.coins)
            }
        }
    }
}

object CoinDiffUtil : DiffUtil.ItemCallback<CoinsAdapterItem>() {

    override fun areItemsTheSame(
        oldItem: CoinsAdapterItem,
        newItem: CoinsAdapterItem
    ): Boolean = when (oldItem) {
        is CategoryHeader if newItem is CategoryHeader -> oldItem.categoryName == newItem.categoryName
        is CoinItem if newItem is CoinItem -> oldItem.coin.id == newItem.coin.id
        is CarouselItems if newItem is CarouselItems -> oldItem.coins.size == newItem.coins.size
        else -> false
    }

    override fun areContentsTheSame(oldItem: CoinsAdapterItem, newItem: CoinsAdapterItem): Boolean =
        oldItem == newItem

    override fun getChangePayload(oldItem: CoinsAdapterItem, newItem: CoinsAdapterItem): Any? {
        if (oldItem is CoinItem && newItem is CoinItem) {
            val diffBundle = Bundle()
            if (oldItem.coin.highlight != newItem.coin.highlight) {
                diffBundle.putBoolean("highlight", newItem.coin.highlight)
            }
            return if (diffBundle.size() > 0) diffBundle else null
        } else return super.getChangePayload(oldItem, newItem)
    }
}