package ru.otus.cryptosample.coins.feature.adapter

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView

class CustomItemAnimator : DefaultItemAnimator() {

    override fun animateAdd(holder: RecyclerView.ViewHolder): Boolean {

        holder.itemView.alpha = 0f
        holder.itemView.translationX = holder.itemView.width.toFloat()

        val animatorSet = AnimatorSet()
        val fadeIn = ObjectAnimator.ofFloat(
            holder.itemView, "alpha", 0f, 1f
        )
        val slideIn = ObjectAnimator.ofFloat(
            holder.itemView, "translationX", holder.itemView.width.toFloat(), 0f
        )

        animatorSet.playTogether(fadeIn, slideIn)

        animatorSet.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                dispatchAddStarting(holder)
            }

            override fun onAnimationEnd(animation: Animator) {
                holder.itemView.alpha = 1f
                holder.itemView.translationX = 0f
                dispatchAddFinished(holder)
            }

            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })

        animatorSet.start()
        return true
    }

    override fun animateRemove(holder: RecyclerView.ViewHolder): Boolean {

        val animatorSet = AnimatorSet()
        val fadeOut = ObjectAnimator.ofFloat(
            holder.itemView, "alpha", 1f, 0f
        )
        val slideOut = ObjectAnimator.ofFloat(
            holder.itemView, "translationX", 0f, holder.itemView.width.toFloat()
        )

        animatorSet.playTogether(fadeOut, slideOut)

        animatorSet.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                dispatchRemoveStarting(holder)
            }

            override fun onAnimationEnd(animation: Animator) {
                holder.itemView.alpha = 1f
                holder.itemView.translationX = 0f
                dispatchRemoveFinished(holder)
            }

            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })

        animatorSet.start()
        return true
    }
}
