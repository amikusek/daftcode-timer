package com.daftcode.recruitment.timer.viper.timer

import android.support.v7.app.AppCompatActivity
import com.hannesdorfmann.mosby.mvp.MvpView
import com.mateuszkoslacz.moviper.iface.interactor.ViperRxInteractor
import com.mateuszkoslacz.moviper.iface.routing.ViperRxRouting

interface TimerContract {

    interface View : MvpView

    interface Interactor : ViperRxInteractor

    interface Routing : ViperRxRouting<AppCompatActivity>
}
