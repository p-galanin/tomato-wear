package ru.pavel.tomato.wear

import android.util.Log
import ru.pavel.tomato.wear.timer.Timer
import ru.pavel.tomato.wear.timer.TimerListener

interface TimerInteractor {
    fun startOrResume(timeToSetInSeconds: Int, timerStateListener: TimerStateListener)
    fun pause()
    fun resume()
    fun cancel()
    fun currentTimeLeft(): Int
    fun isTimerPaused(): Boolean
    fun stopListening(timerStateListener: TimerStateListener)
    fun enableEconomyMode()
    fun disableEconomyMode()

    interface TimerStateListener {
        fun onChangeTime(timeInSeconds: Int)
        fun onFinish()
    }

    companion object {
        fun create() = TimerInteractorImpl()
    }
}

class TimerInteractorImpl : TimerInteractor {

    private val timer = Timer.get()
    private var timeLeftInSec: Int = 0
    private val listeners = LinkedHashSet<TimerInteractor.TimerStateListener>()
    private var isPowerEconomyMode = false

    init {
        timer.startListening(BasicTimerListener())
    }

    override fun startOrResume(
        timeToSetInSeconds: Int,
        timerStateListener: TimerInteractor.TimerStateListener
    ) {
        listeners.add(timerStateListener)
        if (!timer.isActive()) {
            if (timeToSetInSeconds > 0) {
                timer.start(timeToSetInSeconds * TIME_UNIT_DIVISOR)
            } else {
                Log.w(TAG, "Attempt to start timer with time: $timeToSetInSeconds")
            }
        }
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

    override fun stopListening(timerStateListener: TimerInteractor.TimerStateListener) {
        listeners.remove(timerStateListener)
    }

    override fun enableEconomyMode() {
        isPowerEconomyMode = true
    }

    override fun disableEconomyMode() {
        isPowerEconomyMode = false
    }

    override fun isTimerPaused() = timer.isPaused()

    override fun currentTimeLeft() = adjustMlsTime(timer.timeLeft())

    private fun adjustMlsTime(timeMls: Long): Int {
        return (timeMls / TIME_UNIT_DIVISOR).toInt() + 1
    }

    private fun powerModeAllowToChangeTime(timeInSec: Int) =
        !isPowerEconomyMode || timeInSec % POWER_ECONOMY_MODE_REFRESH_FREQUENCY_SEC == 0

    private inner class BasicTimerListener : TimerListener {

        override fun onEveryTick(timeLeftMls: Long) {
            val timeInSec = adjustMlsTime(timeLeftMls)
            if (timeLeftInSec == timeInSec) {
                return
            }

            timeLeftInSec = timeInSec
            if (powerModeAllowToChangeTime(timeLeftInSec)) {
                listeners.forEach {
                    it.onChangeTime(timeLeftInSec)
                }
            }
        }

        override fun onFinish() = listeners.forEach {
            it.onFinish()
        }
    }

    companion object {
        private const val TAG = "D-TIMER"
        private const val TIME_UNIT_DIVISOR = 1000L
        private const val POWER_ECONOMY_MODE_REFRESH_FREQUENCY_SEC = 1
    }
}
