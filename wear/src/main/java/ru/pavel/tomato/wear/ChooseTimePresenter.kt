package ru.pavel.tomato.wear

interface ChooseTimePresenter {
    fun onViewCreated()
    fun onTomatoChosen()
    fun onTimeChosen(chosenTime: String)
}

class ChooseTimePresenterImpl(private val chooseTimeView: ChooseTimeView) : ChooseTimePresenter {

    override fun onViewCreated() {
        chooseTimeView.showList(arrayOf("5", "10", "15"))
    }

    override fun onTimeChosen(chosenTime: String) {
        chooseTimeView.goToTimerView(chosenTime.toInt() * SECONDS_IN_MINUTE)
    }

    override fun onTomatoChosen() {
        onTimeChosen(TOMATO_IN_MINUTES)
    }
}

private const val TOMATO_IN_MINUTES = 25.toString()
private const val SECONDS_IN_MINUTE = 60
//private const val SECONDS_IN_MINUTE = 1 // todo
