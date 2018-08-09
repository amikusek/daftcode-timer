package com.daftcode.recruitment.timer.extension

import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import com.daftcode.recruitment.timer.constants.FLASH_ANIMATION_DURATION_IN_MILLIS
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_timer.*
import java.util.concurrent.TimeUnit

const val WINDOW_DURATION_IN_MILLIS = 200L

fun View.throttleClicks() = RxView
        .clicks(this)
        .throttleFirst(WINDOW_DURATION_IN_MILLIS, TimeUnit.MILLISECONDS)

fun View.makeGone() {
    this.visibility = View.GONE
}

fun View.makeVisible() {
    this.visibility = View.VISIBLE
}

fun View.makeInvisible() {
    this.visibility = View.INVISIBLE
}

fun View.makeFlashAnimation() = this.startAnimation(AlphaAnimation(1F, 0F).apply {

    duration = FLASH_ANIMATION_DURATION_IN_MILLIS
    setAnimationListener(object : Animation.AnimationListener {

        override fun onAnimationRepeat(p0: Animation?) {}

        override fun onAnimationStart(p0: Animation?) {
            this@makeFlashAnimation.makeVisible()
        }

        override fun onAnimationEnd(anim: Animation) {
            this@makeFlashAnimation.makeGone()
        }
    })
})
