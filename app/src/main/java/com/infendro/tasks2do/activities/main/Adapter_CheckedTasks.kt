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

class Adapter_CheckedTasks(private val activity: Activity, private val tasks: Tasks) : RecyclerView.Adapter<ViewHolder>() {

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.view_task, viewGroup, false)

        return ViewHolder(activity,view,tasks)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.bind(getItem(position))
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = tasks.checkedTasks.size

    fun getItem(position: Int) : Task {
        return tasks.checkedTasks[position]
    }

}