package ru.pavel.tomato.wear

import android.util.Log
import ru.pavel.tomato.wear.timer.Timer
import ru.pavel.tomato.wear.timer.TimerListener

interface TimerInteractor {
    fun startOrJoin(timeInSeconds: Int, timerListener: TimerListener)
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

    override fun startOrJoin(
        timeInSeconds: Int,
        timerListener: TimerListener
    ) {

        if (!timer.isActive()) {
            timer.start(timeInSeconds)
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

const val TOMATO_IN_SECONDS = (25 * 60).toString()