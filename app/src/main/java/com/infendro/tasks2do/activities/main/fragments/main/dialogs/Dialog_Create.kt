package com.infendro.tasks2do.activities.main.fragments.main.dialogs

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
import com.infendro.tasks2do.activities.main.fragments.main.Fragment_Main
import kotlinx.android.synthetic.main.dialog_create.*
import java.time.LocalDate


class Dialog_Create(val activity: Activity, val list: List) : Dialog(activity,R.style.Dialog_Create) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_create)

        window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT)
        window?.setGravity(Gravity.BOTTOM)

        val task = Task()

        edittext_title.doOnTextChanged { text, _, _, count ->
            if(count==0){
                button_save.setTextColor(activity.getColor(R.color.save_invalid))
                button_save.setOnClickListener(null)
            }else{
                button_save.setTextColor(activity.getColor(R.color.colorAccent))
                button_save.setOnClickListener {
                    list.uncheckedTasks.add(0, task)
                    Fragment_Main.adapter.notifyItemInserted(Fragment_Main.adapter.getAdapterPositionOfUncheckedIndex0())
                    Fragment_Main.recyclerview.scrollToPosition(0)
                    dismiss()
                }
            }
            task.title = text.toString()
        }

        edittext_details.doOnTextChanged { text, _, _, _ ->
            task.details=text.toString()
        }

        imagebutton_add_details.setOnClickListener {
            edittext_details.visibility= View.VISIBLE
        }

        imagebutton_add_datetime.setOnClickListener {
            val now = LocalDate.now()
            val datepicker = DatePickerDialog(activity, DatePickerDialog.OnDateSetListener{ _, year, month, dayOfMonth ->
                    task.due_date = LocalDate.of(year, month+1, dayOfMonth)
                    textview_due.text = task.getDueString(activity.getString(R.string.pattern_date),activity.getString(R.string.pattern_time))
                    due.visibility=View.VISIBLE
                },
                now.year,
                now.monthValue-1,
                now.dayOfMonth)
            datepicker.setContentView(R.layout.dialog_create)
            //datepicker.findViewById<LinearLayout>(R.id.settime)
            datepicker.show()
        }

        imagebutton_remove_datetime.setOnClickListener {
            due.visibility=View.GONE
            task.due_date=null
            task.due_time=null
        }

    }
}