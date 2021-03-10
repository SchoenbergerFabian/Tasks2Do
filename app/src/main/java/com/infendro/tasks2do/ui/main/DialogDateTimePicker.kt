package com.infendro.tasks2do.ui.main

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import android.widget.TimePicker
import com.infendro.tasks2do.R
import com.infendro.tasks2do.Task
import kotlinx.android.synthetic.main.dialog_datetimepicker.*
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class DialogDateTimePicker(private val activity: Activity, private val task: Task?, private val textView: TextView, private val goneView: View?) : Dialog(activity) {

    private var tempDueTime: LocalTime? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_datetimepicker)

        //<date>
        val localDate = task?.dueDate ?: LocalDate.now()
        datePicker.init(localDate.year,
                localDate.monthValue,
                localDate.dayOfMonth,
                null)

        //<time>
        val dueTimeString = task?.getDueTimeString(activity.getString(R.string.pattern_time))
        if(dueTimeString!=null){
            textViewDueTime.hint = ""
            textViewDueTime.text = dueTimeString
            imageButtonRemove.visibility = View.VISIBLE
        }
        dueTime.setOnClickListener {
            showTimePicker()
        }

        imageButtonRemove.setOnClickListener{
            imageButtonRemove.visibility=View.GONE
            textViewDueTime.hint=activity.getString(R.string.add_time)
            textViewDueTime.text=""
            task?.dueTime=null
        }
        //</time>

        buttonCancelDate.setOnClickListener{
            dismiss()
        }

        buttonDoneDate.setOnClickListener{
            task?.dueDate = LocalDate.of(datePicker.year,datePicker.month,datePicker.dayOfMonth)
            task?.dueTime = tempDueTime
            textView.hint = ""
            textView.text = task?.getDueString(activity.getString(R.string.pattern_date),activity.getString(R.string.pattern_time))
            goneView?.visibility = View.VISIBLE
            dismiss()
        }
        //</date>
    }

    private var shown = false

    private fun showTimePicker(){
        if(!shown){
            setUpTimePicker()
            shown = true
        }

        val localTime = task?.dueTime?:LocalTime.now()
        timePicker.hour = localTime.hour
        timePicker.minute = localTime.minute

        date.visibility = View.GONE
        time.visibility = View.VISIBLE
    }

    private fun setUpTimePicker(){

        timePicker.setIs24HourView(true)

        buttonCancelTime.setOnClickListener {
            hideTimePicker()
        }

        buttonDoneTime.setOnClickListener {
            tempDueTime = LocalTime.of(timePicker.hour,timePicker.minute)
            textViewDueTime.hint = ""
            textViewDueTime.text = tempDueTime?.format(DateTimeFormatter.ofPattern(activity.getString(R.string.pattern_time)))

            imageButtonRemove.visibility=View.VISIBLE

            hideTimePicker()
        }
    }

    private fun hideTimePicker(){
        timePicker.findViewById<View>(Resources.getSystem().getIdentifier("hours", "id", "android")).performClick()
        time.visibility = View.GONE
        date.visibility = View.VISIBLE
    }

}