package ru.pavel.tomato.wear.timer

interface Timer {

    fun start(durationMls: Long)
    fun pause()
    fun resume()
    fun cancel()
    fun startListening(timerListener: TimerListener)
    fun stopListening(timerListener: TimerListener)
    fun isActive(): Boolean
    fun isPaused(): Boolean
    fun timeLeft(): Long

    companion object {
        fun get() = TimerImpl
    }
}

interface TimerListener {
    fun onEveryTick(timeLeftMls: Long) {}
    fun onCancel() {}
    fun onFinish() {}
}
