package ru.pavel.tomato.wear

import android.content.Context
import android.os.Bundle
import android.support.wearable.activity.WearableActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        val arrayAdapter = RestTimeArrayAdapter(this, R.layout.item_select_time, values)
        list_select_time.apply {
            adapter = arrayAdapter
            //onItemClickListener = createItemClickListener()
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

    private inner class RestTimeArrayAdapter(context: Context, itemLayout: Int, values: Array<String>)
        : ArrayAdapter<String>(context, itemLayout, values) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var listItemView = convertView
            if (listItemView == null) {
                listItemView = LayoutInflater.from(context).inflate(R.layout.item_select_time, parent, false)
            }

            val restTimeView = listItemView!!.findViewById<View>(R.id.item_time_to_select) as TextView
            val currentTime = getItem(position)
            restTimeView.text = currentTime

            restTimeView.setOnClickListener {
                presenter.onTimeChosen(
                    it.findViewById<TextView>(R.id.item_time_to_select).text.toString()
                )
            }

            return listItemView
        }
    }
}
