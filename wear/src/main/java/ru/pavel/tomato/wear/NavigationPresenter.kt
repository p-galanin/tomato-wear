package ru.pavel.tomato.wear

import ru.pavel.tomato.wear.timer.Timer

interface NavigationPresenter {
    fun onStart()
}

class NavigationPresenterImpl(private val view: NavigationView) : NavigationPresenter {

    override fun onStart() {
        if (Timer.get().isActive() || Timer.get().isPaused()) {
            view.goToTimerView()
        } else {
            view.goToChooseTimeView()
        }
    }
}