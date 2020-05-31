package ru.pavel.tomato.wear

import android.text.format.DateFormat

interface TimerPresenter {
    fun onStart(timeInSeconds: Int)
    fun onPauseTimer()
    fun onResumeTimer()
    fun onCancelTimer()
    fun onEveryTimerTick(secondsLeft: Int)
    fun onDestroy()
    fun onTimerFinish()
    fun onEnableEconomyMode()
    fun onDisableEconomyMode()
}

class TimerPresenterImpl(private val timerView: TimerView) : TimerPresenter{

    private val timerInteractor = TimerInteractor.create()
    private val timerListener = TimerStateListener(this)

    override fun onStart(timeInSeconds: Int) {
        timerInteractor.startOrResume(timeInSeconds, timerListener)
        timerView.setTimerText(getTimeFormatted(timerInteractor.currentTimeLeft()))
        setButtonsVisibility(timerInteractor.isTimerPaused())
    }

    override fun onCancelTimer() {
        timerInteractor.cancel()
        timerView.goToNavigationView()
    }

    override fun onPauseTimer() {
        setButtonsVisibility(isPaused = true)
        timerInteractor.pause()
    }

    override fun onResumeTimer() {
        setButtonsVisibility(isPaused = false)
        timerInteractor.resume()
    }

    override fun onDestroy() {
        timerInteractor.stopListening(timerListener)
    }

    override fun onEveryTimerTick(secondsLeft: Int) {
        timerView.setTimerText(getTimeFormatted(secondsLeft))
    }

    private fun getTimeFormatted(secondsLeft: Int) =
        DateFormat.format("mm:ss", secondsLeft * 1000L).toString()

    override fun onTimerFinish() {
        timerView.apply {
            signalOnComplete()
            goToNavigationView()
        }
    }

    override fun onEnableEconomyMode() {
        timerView.enableEconomyMode()
        timerInteractor.enableEconomyMode()
    }

    override fun onDisableEconomyMode() {
        timerView.disableEconomyMode()
        timerInteractor.disableEconomyMode()
    }

    private fun setButtonsVisibility(isPaused: Boolean) {
        timerView.setPauseButtonVisibility(!isPaused)
        timerView.setResumeButtonVisibility(isPaused)
    }

    private class TimerStateListener(private val timerPresenter: TimerPresenter)
        : TimerInteractor.TimerStateListener {

        override fun onChangeTime(timeInSeconds: Int) {
            timerPresenter.onEveryTimerTick(timeInSeconds)
        }

        override fun onFinish() {
            timerPresenter.onTimerFinish()
        }
    }
}