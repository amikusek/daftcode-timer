package com.daftcode.recruitment.timer.viper.timer

import com.mateuszkoslacz.moviper.base.presenter.BaseRxPresenter
import com.mateuszkoslacz.moviper.iface.presenter.ViperPresenter

class TimerPresenter :
        BaseRxPresenter<TimerContract.View,
                TimerContract.Interactor,
                TimerContract.Routing>(),
        ViperPresenter<TimerContract.View> {

    override fun createRouting() = TimerRouting()

    override fun createInteractor() = TimerInteractor()
}
