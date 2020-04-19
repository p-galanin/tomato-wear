package ru.pavel.tomato.wear

import android.os.Bundle
import android.os.CountDownTimer
import android.support.wearable.activity.WearableActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : WearableActivity(), TimerView {

    private lateinit var timerPresenter: TimerPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Enables Always-on
        setAmbientEnabled()

        timerPresenter = TimerPresenter(this)

        timerPresenter.onViewCreated()

    }

    override fun onDestroy() {
        timerPresenter.onViewDestroyed()
        super.onDestroy()
    }

    override fun setTimerText(text: String) {
        runOnUiThread {
            test_text.text = text
        }
    }
}
