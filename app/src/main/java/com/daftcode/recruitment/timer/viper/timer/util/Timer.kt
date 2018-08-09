package com.daftcode.recruitment.timer.viper.timer.util

import android.os.CountDownTimer
import io.reactivex.subjects.PublishSubject

const val TIMER_INTERVAL = 1L

class Timer(timeInMillis: Long,
            val onTickEvents: PublishSubject<Long>,
            val onFinishEvents: PublishSubject<Any>) : CountDownTimer(timeInMillis, TIMER_INTERVAL) {

    override fun onFinish() = onFinishEvents.onNext(Unit)

    override fun onTick(millisToFinish: Long) = onTickEvents.onNext(millisToFinish)
}
