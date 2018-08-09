package com.daftcode.recruitment.timer.extension

import android.view.View
import com.jakewharton.rxbinding2.view.RxView
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
