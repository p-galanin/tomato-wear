package ru.pavel.tomato.wear

import android.text.format.DateFormat
import ru.pavel.tomato.wear.timer.TimerListener

interface TimerPresenter {
    fun onStart(timeInSeconds: Int)
    fun onPauseTimer()
    fun onResumeTimer()
    fun onCancelTimer()
    fun onEveryTimerTick(secondsLeft: Int)
    fun onDestroy()
    fun onTimerFinish()
}

class TimerPresenterImpl(private val timerView: TimerView) : TimerPresenter{

    private val timerInteractor = TimerInteractor.create()
    private val timerListener = BasicTimerListener(this)

    override fun onStart(timeInSeconds: Int) {
        timerInteractor.startOrResume(timeInSeconds, timerListener)
        timerView.setPauseButtonVisibility(isVisible = true)
        timerView.setResumeButtonVisibility(isVisible = false)
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

    private fun setButtonsVisibility(isPaused: Boolean) {
        timerView.setPauseButtonVisibility(!isPaused)
        timerView.setResumeButtonVisibility(isPaused)
    }

    private class BasicTimerListener(private val timerPresenter: TimerPresenter)
        : TimerListener {

        override fun onEveryTick(timeLeft: Int) {
            timerPresenter.onEveryTimerTick(timeLeft)
        }

        override fun onFinish() {
            timerPresenter.onTimerFinish()
        }

    }
}