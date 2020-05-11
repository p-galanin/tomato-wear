package ru.pavel.tomato.wear

import android.content.Context
import android.os.Bundle
import android.os.Vibrator
import android.support.wearable.activity.WearableActivity
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

        btn_cancel_timer.setOnClickListener { timerPresenter.onCancelTimer() }
        btn_pause_timer.setOnClickListener { timerPresenter.onPauseTimer() }
        btn_resume_timer.setOnClickListener { timerPresenter.onResumeTimer() }
    }

    override fun onDestroy() {
        timerPresenter.onDestroy()
        super.onDestroy()
    }

    override fun setTimerText(text: String) {
        runOnUiThread {
            tv_time_left.text = text
        }
    }

    override fun signalOnComplete() {
        val vibrator =
            getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        val vibrationPattern = longArrayOf(0, 500, 50, 300)
        vibrator.vibrate(vibrationPattern, -1)
    }

    override fun setPauseButtonVisibility(isVisible: Boolean) {
        btn_pause_timer.visibility = setVisibility(isVisible)
    }

    override fun setResumeButtonVisibility(isVisible: Boolean) {
        btn_resume_timer.visibility = setVisibility(isVisible)
    }

    override fun destroy() {
        finish()
    }

    companion object {

        private const val TIME_IN_SECONDS = "time_in_seconds"

        fun start(context: Context, timeInSeconds: Int) {
            val intent = createIntent(TimerActivity::class.java, context, false).apply {
                putExtra(TIME_IN_SECONDS, timeInSeconds)
            }
            context.startActivity(intent)
        }
    }
}
