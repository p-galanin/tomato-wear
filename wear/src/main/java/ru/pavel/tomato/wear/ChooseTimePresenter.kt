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
        chooseTimeView.goToTimerView(chosenTime.toInt()) // todo convert to seconds
    }

    override fun onTomatoChosen() {
        onTimeChosen(TOMATO_IN_SECONDS)
    }

}
