package ru.pavel.tomato.wear.timer

interface Timer {

    fun start(durationSeconds: Int)
    fun pause()
    fun resume()
    fun stop()
    fun startListening(timerListener: TimerListener)
    fun stopListening(timerListener: TimerListener)
    fun isActive(): Boolean

    companion object {
        fun get() = TimerImpl
    }
}

interface TimerListener {
    fun onEverySecond(secondsLeft: Int) {}
    fun onStop() {}
    fun onFinish() {}
}
