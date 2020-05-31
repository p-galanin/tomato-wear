package ru.pavel.tomato.wear.timer

import android.os.CountDownTimer
import android.util.Log
import kotlin.math.max

object TimerImpl : Timer {

    private const val TAG = "D-TIMER"
    private const val TICK_FREQUENCY_MLS = 100L

    @Volatile
    private var currentTimeLeft = 0L

    @Volatile
    private var state: TimerState = TimerState.Idle

    private val listeners = LinkedHashSet<TimerListener>()

    override fun start(durationMls: Long) {
        Log.d(TAG, "start with time $durationMls")

        if (isActive()) {
            throw IllegalStateException("Active timer exists")
        }

        val newTimer = object : CountDownTimer(durationMls, TICK_FREQUENCY_MLS) {

            override fun onTick(millisUntilFinished: Long) {
                currentTimeLeft = millisUntilFinished
                listeners.forEach {
                    it.onEveryTick(millisUntilFinished)
                }
            }

            override fun onFinish() {
                Log.d(TAG, "onFinish")
                listeners.forEach {
                    it.onFinish()
                }
                state = TimerState.Idle
            }
        }

        currentTimeLeft = durationMls
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
        }
        state = TimerState.Idle
    }

    override fun startListening(timerListener: TimerListener) {
        listeners.add(timerListener)
    }

    override fun stopListening(timerListener: TimerListener) {
        listeners.remove(timerListener)
    }

    override fun isActive() = state is TimerState.Active

    override fun isPaused() = state is TimerState.Paused

    override fun timeLeft() = currentTimeLeft

    override fun pause() {
        Log.d(TAG, "pause")
        val currentState = state
        if (currentState is TimerState.Active) {
            state = TimerState.Paused(currentTimeLeft)
            currentState.timer.cancel()
        } else {
            Log.w(TAG,"Pause called not from active state")
        }
    }

    override fun resume() {
        val currentState = state
        if (currentState is TimerState.Paused) {
            val timeToResume = currentState.timeLeft
            Log.d(TAG, "resume, $timeToResume time left")
            start(max(timeToResume, 1))
        } else {
            Log.e(TAG, "Unable to resume from state $currentState")
        }
    }
}