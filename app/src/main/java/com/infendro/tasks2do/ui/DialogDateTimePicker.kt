package com.infendro.tasks2do.ui

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import com.infendro.tasks2do.R
import com.infendro.tasks2do.Task
import java.time.LocalDate
import java.time.LocalTime

class DialogDateTimePicker(val activity: Activity, val task: Task?) : Dialog(activity), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_datetimepicker)

        //TODO
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        task?.dueDate = LocalDate.of(year, month, dayOfMonth)
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        task?.dueTime = LocalTime.of(hourOfDay,minute)
    }

}