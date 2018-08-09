package com.daftcode.recruitment.timer.viper.timer

import android.support.v7.app.AppCompatActivity
import com.daftcode.recruitment.timer.view.state._base.TimerState
import com.hannesdorfmann.mosby.mvp.MvpView
import com.mateuszkoslacz.moviper.iface.interactor.ViperRxInteractor
import com.mateuszkoslacz.moviper.iface.routing.ViperRxRouting
import io.reactivex.Observable

interface TimerContract {

    interface View : MvpView {
        var timerState: TimerState
        val fabClickEvents: Observable<Any>
        fun renderState(timerState: TimerState)
    }

    interface Interactor : ViperRxInteractor

    interface Routing : ViperRxRouting<AppCompatActivity>
}
