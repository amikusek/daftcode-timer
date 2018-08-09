package com.daftcode.recruitment.timer.viper.timer

import com.daftcode.recruitment.timer.R
import com.mateuszkoslacz.moviper.base.view.activity.autoinject.passive.ViperAiPassiveActivity

class TimerActivity : ViperAiPassiveActivity<TimerContract.View>(), TimerContract.View {

    override fun createPresenter() = TimerPresenter()

    override fun getLayoutId() = R.layout.activity_timer
}
