package ru.pavel.tomato.wear

interface TimerView {
    fun setTimerText(text: String)

    fun signalOnComplete()

    fun setPauseButtonVisibility(isVisible: Boolean)
    fun setResumeButtonVisibility(isVisible: Boolean)

    fun goToChooseTimeView()
}