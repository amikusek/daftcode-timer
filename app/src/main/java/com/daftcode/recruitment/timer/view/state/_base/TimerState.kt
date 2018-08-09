package com.daftcode.recruitment.timer.view.state._base

import android.content.Context
import android.graphics.drawable.Drawable

abstract class TimerState(protected val context: Context) {

    abstract val fabIcon: Drawable
    abstract val startColor: Int
    abstract val endColor: Int
}
