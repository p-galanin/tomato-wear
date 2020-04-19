package ru.pavel.tomato.wear

import android.os.CountDownTimer
import android.util.Log
import java.lang.IllegalStateException
import java.util.concurrent.Executors

object TimerHandlerImpl : TimerHandler {

    private const val TAG = "DEBUG-TIMER"

    @Volatile
    private var timer: CountDownTimer? = null
    private val listeners =
        LinkedHashSet<TimerHandler.TimerListener>() // todo multithread problems?

    override fun startTimer(durationSeconds: Int, listener: TimerHandler.TimerListener?) {

        if (isTimerActive()) {
            throw IllegalStateException("Active timer exists")
        }

        val newTimer = object : CountDownTimer(durationSeconds * 1000L, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                Log.d(TAG, "onTick $millisUntilFinished")
                listeners.forEach {
                    it.onEverySecond((millisUntilFinished / 1000).toInt())
                }
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

        listener?.let {
            listeners.add(it)
        }
    }

    override fun stopTimer() {
        if (isTimerActive()) {
            timer!!.cancel()
            listeners.forEach {
                it.onStop()
            }
            timer = null
        }
    }

    override fun startListening(timerListener: TimerHandler.TimerListener) {
        listeners.add(timerListener)
    }

    override fun stopListening(timerListener: TimerHandler.TimerListener) {
        listeners.remove(timerListener)
    }

    override fun isTimerActive(): Boolean {
        return timer != null
    }

//
//    private fun createTimer(): CountDownTimer {
//        return object : CountDownTimer(60 * 5000, 1000) {
//
//            override fun onFinish() {
//                createTimer().start()
//            }
//
//            override fun onTick(millisUntilFinished: Long) {
//                test_text.text = (millisUntilFinished / 1000).toString()
//            }
//        }
//
//    }
}