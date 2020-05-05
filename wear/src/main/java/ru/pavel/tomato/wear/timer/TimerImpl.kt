package ru.pavel.tomato.wear.timer

import android.os.CountDownTimer
import android.util.Log
import java.lang.IllegalStateException
import kotlin.math.min

object TimerImpl : Timer {

    private const val TAG = "DEBUG-TIMER"

    @Volatile
    private var timer: CountDownTimer? = null

    @Volatile
    private var currentLeftTime: Int = 0

    private val listeners =
        LinkedHashSet<TimerListener>() // todo multithread problems?

    override fun start(durationSeconds: Int) {

        if (isActive()) {
            throw IllegalStateException("Active timer exists")
        }

        val newTimer = object : CountDownTimer(durationSeconds * 1000L, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                Log.d(TAG, "onTick $millisUntilFinished")
                val currentTime = (millisUntilFinished / 1000).toInt() + 1
                listeners.forEach {
                    it.onEverySecond(currentTime)
                }
                currentLeftTime = currentTime
            }

            override fun onFinish() {
                Log.d(TAG, "onFinish")
                listeners.forEach {
                    it.onFinish()
                }
                timer = null
            }

        }

        timer = newTimer.apply {
            start()
        }
    }

    override fun stop() {
        if (isActive()) {
            timer!!.cancel()
            listeners.forEach {
                it.onStop()
            }
            timer = null
            currentLeftTime = 0
        }
    }

    override fun startListening(timerListener: TimerListener) {
        listeners.add(timerListener)
    }

    override fun stopListening(timerListener: TimerListener) {
        listeners.remove(timerListener)
    }

    override fun isActive(): Boolean {
        return timer != null
    }

    override fun pause() {
        timer!!.cancel()
        timer = null
    }

    override fun resume() {
        start(min(currentLeftTime, 1))
    }

}