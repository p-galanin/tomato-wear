package ru.pavel.tomato.wear

interface ChooseTimeView {

    fun showList(values: Array<String>)

    fun goToTimerView(startWithTimeInSeconds: Int)

}