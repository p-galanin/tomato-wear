package ru.pavel.tomato.wear.timer

interface Timer {

    fun start(durationSeconds: Int)
    fun pause()
    fun resume()
    fun cancel()
    fun startListening(timerListener: TimerListener)
    fun stopListening(timerListener: TimerListener)
    fun isActive(): Boolean
    fun isPaused(): Boolean

    companion object {
        fun get() = TimerImpl
    }
}

interface TimerListener {
    fun onEverySecond(secondsLeft: Int) {}
    fun onCancel() {}
    fun onFinish() {}
}
