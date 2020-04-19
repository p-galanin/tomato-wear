package ru.pavel.tomato.wear

class TimerPresenter(private val timerView: TimerView?) {

    private val timerHandler = TimerHandler.get()
    private val timerListener = BasicTimerListener()

    fun onViewCreated() {
        if (timerHandler.isTimerActive()) {
            timerHandler.startListening(timerListener)
        } else {
            timerHandler.startTimer(15, timerListener)
        }
    }

    fun onViewDestroyed() {
        timerHandler.stopListening(timerListener)
    }

    inner class BasicTimerListener : TimerHandler.TimerListener {

        override fun onEverySecond(secondsLeft: Int) {
            timerView?.setTimerText(secondsLeft.toString())
        }

    }


}