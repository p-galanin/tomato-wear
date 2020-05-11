package ru.pavel.tomato.wear

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.wearable.activity.WearableActivity
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_choose_time.*

class ChooseTimeActivity : WearableActivity(), ChooseTimeView {

    private lateinit var presenter: ChooseTimePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_time)

        presenter = ChooseTimePresenterImpl(this).apply {
            onViewCreated()
        }

        btn_start_tomato.setOnClickListener {
            presenter.onTomatoChosen()
        }
    }

    override fun showList(values: Array<String>) {
        val arrayAdapter = ArrayAdapter(this, R.layout.item_select_time, values)
        list_select_time.apply {
            adapter = arrayAdapter
            onItemClickListener = createItemClickListener()
        }

    }

    override fun goToTimerView(startWithTimeInSeconds: Int) {
        TimerActivity.start(this, startWithTimeInSeconds)
    }

    private fun createItemClickListener(): AdapterView.OnItemClickListener {
        return AdapterView.OnItemClickListener { _, view, _, _ ->
            presenter.onTimeChosen(view.findViewById<TextView>(R.id.item_time_to_select).text.toString())
        }
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(createIntent(ChooseTimeActivity::class.java, context, false))
        }
    }
}
