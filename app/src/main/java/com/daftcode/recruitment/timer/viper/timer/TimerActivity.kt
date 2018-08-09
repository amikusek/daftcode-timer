package com.daftcode.recruitment.timer.viper.timer

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import com.daftcode.recruitment.timer.R
import com.daftcode.recruitment.timer.constants.BACKGROUND_TRANSITION_DURATION_IN_MILLIS
import com.daftcode.recruitment.timer.view.state._base.TimerState
import com.daftcode.recruitment.timer.extension.throttleClicks
import com.daftcode.recruitment.timer.view.state.StoppedTimerState
import com.mateuszkoslacz.moviper.base.view.activity.autoinject.passive.ViperAiPassiveActivity
import kotlinx.android.synthetic.main.activity_timer.*

const val BACKGROUND_PROPERTY_NAME = "backgroundColor"

class TimerActivity : ViperAiPassiveActivity<TimerContract.View>(), TimerContract.View {

    private val argbEvaluator = ArgbEvaluator()

    override var timerState: TimerState = StoppedTimerState(context)
    override val fabClickEvents
        get() = fab.throttleClicks()!!

    override fun renderState(timerState: TimerState) {
        fab.setImageDrawable(timerState.fabIcon)
        ObjectAnimator
                .ofObject(root,
                        BACKGROUND_PROPERTY_NAME,
                        argbEvaluator,
                        timerState.startColor,
                        timerState.endColor)
                .apply {
                    duration = BACKGROUND_TRANSITION_DURATION_IN_MILLIS
                }.start()
    }

    override fun createPresenter() = TimerPresenter()

    override fun getLayoutId() = R.layout.activity_timer
}
