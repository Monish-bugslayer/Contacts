package com.monish.recyclermultiview

import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener


class VerticalBubbleScrollBarAnimationManager :
    BubbleScrollBarAnimationManager {
    private var updateListener: AnimatorUpdateListener? = null
    override fun provideShowBubbleAnimation(viewComponents: BubbleScrollBarViewComponents?): ValueAnimator {
        return ValueAnimator.ofFloat(HIDDEN_ALPHA.toFloat(), VISIBLE_ALPHA.toFloat())
    }

    override fun provideShowBubbleUpdateListener(viewComponents: BubbleScrollBarViewComponents?): AnimatorUpdateListener? {
        if (updateListener == null) {
            updateListener = AnimatorUpdateListener { animation ->
                val animatedValue = animation.animatedValue as Float
                viewComponents?.bubble?.setAlpha(animatedValue)
                viewComponents?.bubble?.setScaleX(animatedValue)
                viewComponents?.bubble?.setScaleY(animatedValue)
            }
        }
        return updateListener
    }

    override fun provideHideBubbleUpdateListener(viewComponents: BubbleScrollBarViewComponents?): AnimatorUpdateListener? {
        return provideShowBubbleUpdateListener(viewComponents)
    }

    override fun provideHideBubbleAnimation(viewComponents: BubbleScrollBarViewComponents?): ValueAnimator? {
        val hideAnimation = ValueAnimator.ofFloat(VISIBLE_ALPHA.toFloat(), HIDDEN_ALPHA.toFloat())
        hideAnimation.startDelay = HIDE_ANIMATION_START_DELAY.toLong()
        return hideAnimation
    }

    override fun isBubbleVisible(viewComponents: BubbleScrollBarViewComponents?): Boolean {
        return viewComponents?.bubble?.getAlpha() === VISIBLE_ALPHA.toFloat()
    }

    override fun isBubbleHidden(viewComponents: BubbleScrollBarViewComponents?): Boolean {
        return viewComponents?.bubble?.getAlpha() === HIDDEN_ALPHA.toFloat()
    }

    override fun provideShowScrollBarAnimation(viewComponents: BubbleScrollBarViewComponents?): ValueAnimator? {
        TODO("Not yet implemented")
    }

    override fun provideHideScrollBarAnimation(viewComponents: BubbleScrollBarViewComponents?): ValueAnimator? {
        TODO("Not yet implemented")
    }

    override fun provideShowScrollBarUpdateListner(viewComponents: BubbleScrollBarViewComponents?): AnimatorUpdateListener? {
        TODO("Not yet implemented")
    }

    override fun provideHideScrollBarUpdateListner(viewComponents: BubbleScrollBarViewComponents?): AnimatorUpdateListener? {
        TODO("Not yet implemented")
    }


    companion object {
        const val HIDE_ANIMATION_START_DELAY = 350
        private const val VISIBLE_ALPHA = 1
        private const val HIDDEN_ALPHA = 0
    }
}