package ru.pavel.tomato.wear

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.wearable.activity.WearableActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_timer.*

class TimerActivity : WearableActivity(), TimerView {

    private lateinit var timerPresenter: TimerPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)

        // Enables Always-on
        setAmbientEnabled()

        timerPresenter = TimerPresenterImpl(this).apply {
            onStart(intent.getIntExtra(TIME_IN_SECONDS, 0))
        }

        // todo start/resume buttons listeners
    }

    override fun onDestroy() {
        timerPresenter.onDestroy()
        super.onDestroy()
    }

    override fun setTimerText(text: String) {
        runOnUiThread {
            test_text.text = text
        }
    }

    override fun signalOnComplete() {
        test_text.text = "done"
    }

    override fun setPauseButtonVisibility(isVisible: Boolean) {
        btn_pause_timer.visibility = if (isVisible) {
            View.VISIBLE
        } else {
            View.INVISIBLE
        }
    }

    override fun setResumeButtonVisibility(isVisible: Boolean) {
        btn_resume_timer.visibility = if (isVisible) {
            View.VISIBLE
        } else {
            View.INVISIBLE
        }
    }

    override fun goToChooseTimeView() {
        ChooseTimeActivity.start(this)
    }

    companion object {

        private const val TIME_IN_SECONDS = "time_in_seconds"

        fun start(context: Context, timeInSeconds: Int) {
            val intent = Intent(context, TimerActivity::class.java).apply {
                putExtra(TIME_IN_SECONDS, timeInSeconds)
            }
            context.startActivity(intent)
        }
    }
}
