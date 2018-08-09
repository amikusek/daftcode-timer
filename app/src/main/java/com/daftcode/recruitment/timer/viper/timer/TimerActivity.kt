package com.daftcode.recruitment.timer.viper.timer

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import com.daftcode.recruitment.timer.R
import com.daftcode.recruitment.timer.constants.BACKGROUND_TRANSITION_DURATION_IN_MILLIS
import com.daftcode.recruitment.timer.constants.TIMER_DEFAULT_TIME_IN_MILLIS
import com.daftcode.recruitment.timer.extension.*
import com.daftcode.recruitment.timer.view.state._base.TimerState
import com.daftcode.recruitment.timer.view.state.StoppedTimerState
import com.daftcode.recruitment.timer.viper.timer.util.Timer
import com.mateuszkoslacz.moviper.base.view.activity.autoinject.passive.ViperAiPassiveActivity
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_timer.*

const val BACKGROUND_PROPERTY_NAME = "backgroundColor"

class TimerActivity : ViperAiPassiveActivity<TimerContract.View>(), TimerContract.View {

    override var timerState: TimerState = StoppedTimerState(context)
    override val fabClickEvents
        get() = fab.throttleClicks()!!
    override var timerTimeInMillis = TIMER_DEFAULT_TIME_IN_MILLIS
    override val onTimerTickEvents = PublishSubject.create<Long>()!!
    override val onTimerFinishEvents = PublishSubject.create<Any>()!!
    private var timer = Timer(timerTimeInMillis, onTimerTickEvents, onTimerFinishEvents)
    private val argbEvaluator = ArgbEvaluator()

    override fun injectViews() {
        super.injectViews()
        setTimerToPreviousState()
        subscribeToTimerTickEvents()
        subscribeToTimerProgressChanges()
    }

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

    override fun startTimer() {
        timer.start()
        timerView.isTimerRunning = true
    }

    override fun cancelTimer() {
        timer.cancel()
        timerView.isTimerRunning = false
    }

    override fun setTimerToPreviousState() {
        timerView.setProgress(timerTimeInMillis)
        timeTv.text = timerTimeInMillis.getFormattedTimeFromMillis()
    }

    private fun subscribeToTimerTickEvents() {
        onTimerTickEvents.retrySubscribe(
                onNext = {
                    timerView.setProgress(it)
                    timeTv.text = it.getFormattedTimeFromMillis()
                },
                onError = { it.reportToServer() }
        )
    }

    private fun subscribeToTimerProgressChanges() {
        timerView.onTimerValueChangeEvents.retrySubscribe(
                onNext = {
                    timerTimeInMillis = it
                    timer = Timer(timerTimeInMillis, onTimerTickEvents, onTimerFinishEvents)
                    timeTv.text = timerTimeInMillis.getFormattedTimeFromMillis()
                },
                onError = { it.reportToServer() }
        )
    }

    override fun createPresenter() = TimerPresenter()

    override fun getLayoutId() = R.layout.activity_timer
}
