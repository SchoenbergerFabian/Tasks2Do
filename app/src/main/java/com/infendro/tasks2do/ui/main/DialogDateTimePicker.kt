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
import java.util.*

class DialogDateTimePicker(private val activity: Activity, private val task: Task?) : Dialog(activity) {

    private var tempDueDate: LocalDate? = task?.dueDate
    private var tempDueTime: LocalTime? = task?.dueTime

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_datetimepicker)

        //<date>
        tempDueDate = tempDueDate ?: LocalDate.now()
        val calendar = Calendar.getInstance()
        calendar.set(tempDueDate!!.year,tempDueDate!!.monthValue-1,tempDueDate!!.dayOfMonth)
        datePicker.date = calendar.timeInMillis

        datePicker.setOnDateChangeListener { _, year, month, dayOfMonth ->
            tempDueDate = LocalDate.of(year, month+1, dayOfMonth)
        }

        //<time>
        val dueTimeString = getDueTimeString(activity.getString(R.string.pattern_time))
        if(dueTimeString!=null){
            showTime(dueTimeString)
        }

        dueTime.setOnClickListener {
            showTimePicker()
        }

        imageButtonRemove.setOnClickListener{
            tempDueTime=null
            hideTime()
        }
        //</time>

        buttonCancelDate.setOnClickListener{
            dismiss()
        }

        buttonDoneDate.setOnClickListener{
            task?.dueDate = tempDueDate
            task?.dueTime = tempDueTime
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

        tempDueTime = tempDueTime?:LocalTime.now()
        timePicker.hour = tempDueTime!!.hour
        timePicker.minute = tempDueTime!!.minute

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

    private fun showTime(dueTime : String){
        textViewDueTime.hint = ""
        textViewDueTime.text = dueTime
        imageButtonRemove.visibility = View.VISIBLE
    }

    private fun hideTime(){
        imageButtonRemove.visibility=View.GONE
        textViewDueTime.hint=activity.getString(R.string.add_time)
        textViewDueTime.text=""
    }

    private fun getDueTimeString(patternTime: String) : String? {
        return tempDueTime?.format(DateTimeFormatter.ofPattern(patternTime))
    }

}