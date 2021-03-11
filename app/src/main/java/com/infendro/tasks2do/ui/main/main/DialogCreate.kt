package com.infendro.tasks2do.ui.main.main

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import androidx.core.widget.doOnTextChanged
import com.infendro.tasks2do.List
import com.infendro.tasks2do.R
import com.infendro.tasks2do.Task
import com.infendro.tasks2do.ui.main.DialogDateTimePicker
import kotlinx.android.synthetic.main.dialog_create.*


class DialogCreate(private val activity: Activity, private val list: List) : Dialog(activity,R.style.Dialog_Create) {
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

        val onClickListenerAddDateTime = View.OnClickListener {
            val dateTimePicker = DialogDateTimePicker(activity,task)
            dateTimePicker.setOnDismissListener {
                val dueString = task.getDueString(activity.getString(R.string.pattern_date),activity.getString(R.string.pattern_time))
                if(dueString!=null){
                    textViewDue.text = dueString
                    due.visibility=View.VISIBLE
                }
            }
            dateTimePicker.show()
        }
        imageButtonAddDatetime.setOnClickListener(onClickListenerAddDateTime)
        textViewDue.setOnClickListener(onClickListenerAddDateTime)

        imageButtonRemove.setOnClickListener {
            due.visibility=View.GONE
            task.dueDate=null
            task.dueTime=null
        }

    }
}