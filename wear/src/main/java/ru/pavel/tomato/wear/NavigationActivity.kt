package ru.pavel.tomato.wear

import android.content.Context
import android.os.Bundle
import android.support.wearable.activity.WearableActivity

class NavigationActivity : WearableActivity(), NavigationView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)
        NavigationPresenterImpl(this).onStart()
    }

    override fun goToChooseTimeView() {
        ChooseTimeActivity.start(this)
        finish()
    }

    override fun goToTimerView() {
        TimerActivity.start(this)
        finish()
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(createIntent(NavigationActivity::class.java, context, false))
        }
    }
}
