package ru.pavel.tomato.wear

import android.util.Log
import ru.pavel.tomato.wear.timer.Timer
import ru.pavel.tomato.wear.timer.TimerListener

interface TimerInteractor {
    fun startOrResume(timeInSeconds: Int, timerListener: TimerListener)
    fun pause()
    fun resume()
    fun cancel()
    fun stopListening(timerListener: TimerListener)

    companion object {
        fun create() = TimerInteractorImpl()
    }
}

class TimerInteractorImpl : TimerInteractor {

    private val timer = Timer.get()

    override fun startOrResume(
        timeInSeconds: Int,
        timerListener: TimerListener
    ) {
        if (!timer.isActive()) {
            if (timeInSeconds > 0) {
                timer.start(timeInSeconds)
            } else {
                Log.w(TAG, "Attempt to start timer with time: $timeInSeconds")
            }
        }

        timer.startListening(timerListener)
    }

    override fun pause() {
        timer.pause()
    }

    override fun resume() {
        if (timer.isPaused()) {
            timer.resume()
        } else {
            Log.w(TAG, "Attempt to resume not paused timer")
        }
    }

    override fun cancel() {
        timer.cancel()
    }

    override fun stopListening(timerListener: TimerListener) {
        timer.stopListening(timerListener)
    }

    companion object {
        private const val TAG = "D-TIMER"
    }
}
