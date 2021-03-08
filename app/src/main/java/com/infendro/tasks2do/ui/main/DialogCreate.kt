package com.infendro.tasks2do.activities.ui.fragments.main.dialogs

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import androidx.core.widget.doOnTextChanged
import com.infendro.tasks2do.List
import com.infendro.tasks2do.R
import com.infendro.tasks2do.Task
import com.infendro.tasks2do.activities.ui.fragments.main.FragmentMain
import kotlinx.android.synthetic.main.dialog_create.*
import java.time.LocalDate


class DialogCreate(val activity: Activity, val list: List) : Dialog(activity,R.style.Dialog_Create) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_create)

        window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT)
        window?.setGravity(Gravity.BOTTOM)

        val task = Task()

        editTextTitle.doOnTextChanged { text, _, _, count ->
            if(count==0){
                buttonSave.setTextColor(activity.getColor(R.color.invalid))
                buttonSave.setOnClickListener(null)
            }else{
                buttonSave.setTextColor(activity.getColor(R.color.colorAccent))
                buttonSave.setOnClickListener {
                    list.uncheckedTasks.add(0, task)
                    FragmentMain.adapter.notifyItemInserted(FragmentMain.adapter.getAdapterPositionOfUncheckedIndex0())
                    FragmentMain.recyclerview.scrollToPosition(0)
                    dismiss()
                }
            }
            task.title = text.toString()
        }

        editTextDetails.doOnTextChanged { text, _, _, _ ->
            task.details=text.toString()
        }

        imageButtonAddDetails.setOnClickListener {
            editTextDetails.visibility= View.VISIBLE
        }

        imageButtonAddDatetime.setOnClickListener {
            val now = LocalDate.now()
            val datepicker = DatePickerDialog(activity, DatePickerDialog.OnDateSetListener{ _, year, month, dayOfMonth ->
                    task.dueDate = LocalDate.of(year, month+1, dayOfMonth)
                    textViewDue.text = task.getDueString(activity.getString(R.string.pattern_date),activity.getString(R.string.pattern_time))
                    due.visibility=View.VISIBLE
                },
                now.year,
                now.monthValue-1,
                now.dayOfMonth)
            datepicker.setContentView(R.layout.dialog_create)
            //datepicker.findViewById<LinearLayout>(R.id.settime)
            datepicker.show()
        }

        imageButtonRemoveDatetime.setOnClickListener {
            due.visibility=View.GONE
            task.dueDate=null
            task.dueTime=null
        }

    }
}