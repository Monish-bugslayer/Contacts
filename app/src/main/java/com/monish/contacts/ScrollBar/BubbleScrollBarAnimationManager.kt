package com.monish.recyclermultiview

import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener

interface BubbleScrollBarAnimationManager {
    fun provideShowBubbleAnimation(viewComponents: BubbleScrollBarViewComponents?): ValueAnimator?

    fun provideShowBubbleUpdateListener(viewComponents: BubbleScrollBarViewComponents?): AnimatorUpdateListener?

    fun provideHideBubbleUpdateListener(viewComponents: BubbleScrollBarViewComponents?): AnimatorUpdateListener?

    fun provideHideBubbleAnimation(viewComponents: BubbleScrollBarViewComponents?): ValueAnimator?

    fun isBubbleVisible(viewComponents: BubbleScrollBarViewComponents?): Boolean

    fun isBubbleHidden(viewComponents: BubbleScrollBarViewComponents?): Boolean
    fun provideShowScrollBarAnimation(viewComponents: BubbleScrollBarViewComponents?):ValueAnimator?
    fun provideHideScrollBarAnimation(viewComponents: BubbleScrollBarViewComponents?):ValueAnimator?
    fun provideShowScrollBarUpdateListner(viewComponents: BubbleScrollBarViewComponents?):AnimatorUpdateListener?
    fun provideHideScrollBarUpdateListner(viewComponents: BubbleScrollBarViewComponents?):AnimatorUpdateListener?
}