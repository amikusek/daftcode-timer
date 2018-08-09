package com.daftcode.recruitment.timer.viper.timer

import android.support.v7.app.AppCompatActivity
import com.daftcode.recruitment.timer.view.state._base.TimerState
import com.hannesdorfmann.mosby.mvp.MvpView
import com.mateuszkoslacz.moviper.iface.interactor.ViperRxInteractor
import com.mateuszkoslacz.moviper.iface.routing.ViperRxRouting
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

interface TimerContract {

    interface View : MvpView {
        var timerState: TimerState
        val fabClickEvents: Observable<Any>
        var timerTimeInMillis: Long
        val onTimerTickEvents: PublishSubject<Long>
        val onTimerFinishEvents: PublishSubject<Any>
        fun renderState(timerState: TimerState)
        fun startTimer()
        fun cancelTimer()
        fun setTimerToPreviousState()
        fun makeFlashAnimation()
    }

    interface Interactor : ViperRxInteractor

    interface Routing : ViperRxRouting<AppCompatActivity>
}
