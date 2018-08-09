package com.daftcode.recruitment.timer.viper.timer

import com.daftcode.recruitment.timer.extension.reportToServer
import com.daftcode.recruitment.timer.extension.retrySubscribe
import com.daftcode.recruitment.timer.view.state.RunningTimerState
import com.daftcode.recruitment.timer.view.state.StoppedTimerState
import com.mateuszkoslacz.moviper.base.presenter.BaseRxPresenter
import com.mateuszkoslacz.moviper.iface.presenter.ViperPresenter

class TimerPresenter :
        BaseRxPresenter<TimerContract.View,
                TimerContract.Interactor,
                TimerContract.Routing>(),
        ViperPresenter<TimerContract.View> {

    override fun attachView(attachingView: TimerContract.View?) {
        super.attachView(attachingView)

        addSubscription(
                view
                        ?.fabClickEvents
                        ?.retrySubscribe(
                                onNext = {
                                    view?.timerState?.let {
                                        if (it is StoppedTimerState) {
                                            view?.timerState = it.start()
                                            view?.startTimer()
                                        } else if (it is RunningTimerState) {
                                            view?.timerState = it.stop()
                                            view?.cancelTimer()
                                            view?.setTimerToPreviousState()
                                        }
                                        view?.renderState(view!!.timerState)
                                    }
                                },
                                onError = { it.reportToServer() }))
        addSubscription(
                view
                        ?.onTimerFinishEvents
                        ?.retrySubscribe(
                                onNext = {
                                    view?.let {
                                        view?.timerState = (it.timerState as RunningTimerState).stop()
                                        view?.renderState(view!!.timerState)
                                        it.cancelTimer()
                                        it.setTimerToPreviousState()
                                    }
                                },
                                onError = { it.reportToServer() }
                        ))
    }

    override fun createRouting() = TimerRouting()

    override fun createInteractor() = TimerInteractor()
}
