package ru.pavel.tomato.wear.timer

import android.os.CountDownTimer
import android.util.Log
import kotlin.math.max

object TimerImpl : Timer {

    private const val TAG = "D-TIMER"
    private const val TICK_FREQUENCY_MLS = 100L

    @Volatile
    private var currentLeftTime: Int = 0

    @Volatile
    private var state: TimerState = TimerState.Idle

    private val listeners = LinkedHashSet<TimerListener>()

    override fun start(durationSeconds: Int) {
        Log.d(TAG, "start")

        if (isActive()) {
            throw IllegalStateException("Active timer exists")
        }

        val newTimer = object : CountDownTimer(durationSeconds * 1000L, TICK_FREQUENCY_MLS) {

            override fun onTick(millisUntilFinished: Long) {
                Log.d(TAG, "onTick $millisUntilFinished")
                val currentTime = (millisUntilFinished / 1000L).toInt() + 1
                listeners.forEach {
                    it.onEveryTick(currentTime)
                }
                currentLeftTime = currentTime
            }

            override fun onFinish() {
                Log.d(TAG, "onFinish")
                listeners.forEach {
                    it.onFinish()
                }
                state = TimerState.Idle
            }
        }

        state = TimerState.Active(newTimer.apply {
            start()
        })
    }

    override fun cancel() {
        Log.d(TAG, "cancel")
        val currentState = state
        if (currentState is TimerState.Active) {
            currentState.timer.cancel()
            listeners.forEach {
                it.onCancel()
            }
            state = TimerState.Idle
        }
    }

    override fun startListening(timerListener: TimerListener) {
        listeners.add(timerListener)
    }

    override fun stopListening(timerListener: TimerListener) {
        listeners.remove(timerListener)
    }

    override fun isActive() = state is TimerState.Active

    override fun isPaused() = state is TimerState.Paused

    override fun pause() {
        Log.d(TAG, "pause")
        val currentState = state
        if (currentState is TimerState.Active) {
            state = TimerState.Paused(currentLeftTime)
            currentState.timer.cancel()
        } else {
            Log.w(TAG,"Pause called not from active state")
        }
    }

    override fun resume() {
        Log.d(TAG, "resume")
        start(max(currentLeftTime, 1))
    }
}