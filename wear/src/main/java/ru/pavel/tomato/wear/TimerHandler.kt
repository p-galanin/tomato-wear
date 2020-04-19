package ru.pavel.tomato.wear

interface TimerHandler {

    fun startTimer(durationSeconds: Int, listener: TimerListener? = null)

    fun stopTimer()

    fun startListening(timerListener: TimerListener)

    fun stopListening(timerListener: TimerListener)

    fun isTimerActive(): Boolean

    interface TimerListener {
        fun onEverySecond(secondsLeft: Int) {}
        fun onStop() {}
        fun onFinish() {}
    }

    companion object {
        fun get() = TimerHandlerImpl
    }

}