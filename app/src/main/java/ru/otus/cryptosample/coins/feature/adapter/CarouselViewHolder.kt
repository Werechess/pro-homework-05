package ru.otus.cryptosample.coins.feature.adapter

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.otus.cryptosample.coins.feature.CoinState
import ru.otus.cryptosample.databinding.ItemCarouselBinding

class CarouselViewHolder(
    binding: ItemCarouselBinding,
    sharedPool: RecyclerView.RecycledViewPool
) : RecyclerView.ViewHolder(binding.root) {

    private val carouselAdapter = CarouselAdapter()

    init {
        binding.carouselRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = carouselAdapter
            setRecycledViewPool(sharedPool)
            setHasFixedSize(true)
            isNestedScrollingEnabled = false
            itemAnimator = CustomItemAnimator()
        }
    }

    fun bind(coins: List<CoinState>) {
        carouselAdapter.submitList(coins)
    }
}
