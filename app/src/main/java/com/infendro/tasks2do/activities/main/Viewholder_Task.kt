package com.infendro.tasks2do.activities.main

import android.app.Activity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.infendro.tasks2do.R
import com.infendro.tasks2do.Task
import com.infendro.tasks2do.List

class ViewHolder(private val activity: Activity, view: View, val list: List) : RecyclerView.ViewHolder(view) {

    private val button_check: ImageView = view.findViewById(R.id.button_check)

    private val textview_title: TextView = view.findViewById(R.id.textview_title)
    private val textview_details: TextView = view.findViewById(R.id.textview_details)
    private val textview_due: TextView = view.findViewById(R.id.textview_due)

    private lateinit var task: Task

    init {
        button_check.setOnClickListener {
            when(task.checked){
                true -> {
                    list.uncheck(adapterPosition)

                    //update image
                    setCheckedImage()

                    //updating with animations
                    MainActivity.adapter_checked.notifyItemRemoved(adapterPosition)
                    MainActivity.adapter_checked.notifyItemRangeChanged(adapterPosition, MainActivity.adapter_checked.getItemCount());
                    MainActivity.adapter_unchecked.notifyItemInserted(0)

                    //show empty message if empty
                    MainActivity.showEmpty(list)
                }
                false -> {
                    list.check(adapterPosition)

                    //update image
                    setCheckedImage()

                    //updating with animations
                    MainActivity.adapter_unchecked.notifyItemRemoved(adapterPosition)
                    MainActivity.adapter_unchecked.notifyItemRangeChanged(adapterPosition, MainActivity.adapter_unchecked.getItemCount());
                    MainActivity.adapter_checked.notifyItemInserted(0)
                }
            }
        }
    }

    fun bind(task : Task){
        this.task=task

        setCheckedImage()

        textview_title.text = task.title

        val details = task.details
        if(details!=null){
            textview_details.text = details
            textview_details.visibility = View.VISIBLE
        }else{
            textview_details.text = ""
            textview_details.visibility = View.GONE
        }

        val due = task.getDueString(activity.getString(R.string.pattern_date),activity.getString(R.string.pattern_time))
        if(due!=null){
            textview_due.text = due
            textview_due.visibility = View.VISIBLE
        }else{
            textview_due.text = ""
            textview_due.visibility = View.GONE
        }
    }

    private fun setCheckedImage(){
        when(task.checked){
            true -> button_check.setImageResource(R.drawable.ic_checked)
            false -> button_check.setImageResource(R.drawable.ic_unchecked)
        }
    }

}