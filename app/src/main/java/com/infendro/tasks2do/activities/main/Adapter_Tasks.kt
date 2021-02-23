package com.infendro.tasks2do.activities.main

import android.app.Activity
import android.view.*
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SortedList
import androidx.recyclerview.widget.SortedListAdapterCallback
import com.infendro.tasks2do.R
import com.infendro.tasks2do.Tasks
import com.infendro.tasks2do.Task

class Adapter_Tasks(private val activity: Activity, private val tasks: Tasks) : RecyclerView.Adapter<Adapter_Tasks.ViewHolder>() {



    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.view_task, viewGroup, false)

        return ViewHolder(activity,view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.bind(getItem(position))
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = tasks.size()

    fun getItem(position: Int) : Task {
        return tasks.get(position)
    }

    class ViewHolder(private val activity: Activity, view: View) : RecyclerView.ViewHolder(view) {
        private val button_check: Button = view.findViewById(R.id.button_check)

        private val textview_title: TextView = view.findViewById(R.id.textview_title)
        private val textview_details: TextView = view.findViewById(R.id.textview_details)
        private val textview_due: TextView = view.findViewById(R.id.textview_due)

        private lateinit var task: Task

        init {
            button_check.setOnClickListener {
                task.changeChecked()
                setCheckedBackground(task.checked)
            }
            // Define click listener for the ViewHolder's View.
        }

        fun bind(task : Task){
            this.task=task

            setCheckedBackground(task.checked)

            textview_title.text = task.title

            val details = task.details
            if(details!=null){
                textview_details.text = details
                textview_details.visibility = View.VISIBLE
            }

            val due = task.getDueString(activity.getString(R.string.pattern_date),activity.getString(R.string.pattern_time))
            if(due!=null){
                textview_due.text = due
                textview_due.visibility = View.VISIBLE
            }
        }

        private fun setCheckedBackground(checked: Boolean){
            when(checked){
                true -> button_check.background = ContextCompat.getDrawable(activity,R.drawable.ic_checked)
                false -> button_check.background = ContextCompat.getDrawable(activity,R.drawable.ic_unchecked)
            }
        }

    }

}