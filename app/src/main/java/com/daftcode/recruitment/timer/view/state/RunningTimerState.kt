package com.daftcode.recruitment.timer.view.state

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import com.daftcode.recruitment.timer.R
import com.daftcode.recruitment.timer.view.state._base.TimerState

class RunningTimerState(context: Context) : TimerState(context) {

    override val fabIcon: Drawable
        get() = ContextCompat.getDrawable(context, R.drawable.ic_timer_off)!!

    override val startColor: Int
        get() = ContextCompat.getColor(context, R.color.lightBackground)

    override val endColor: Int
        get() = ContextCompat.getColor(context, R.color.darkBackground)

    fun stop() = StoppedTimerState(context)
}
