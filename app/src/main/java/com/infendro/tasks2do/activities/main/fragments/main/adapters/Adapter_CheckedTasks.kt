package com.infendro.tasks2do.activities.main.fragments.main.adapters

import android.app.Activity
import android.view.*
import androidx.recyclerview.widget.RecyclerView
import com.infendro.tasks2do.R
import com.infendro.tasks2do.List
import com.infendro.tasks2do.Task
import com.infendro.tasks2do.activities.main.ViewHolder

class Adapter_CheckedTasks(private val activity: Activity, private val list: List) : RecyclerView.Adapter<ViewHolder>() {

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.view_task, viewGroup, false)

        return ViewHolder(
            activity,
            view,
            list
        )
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.bind(getItem(position))
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = list.checkedTasks.size

    fun getItem(position: Int) : Task {
        return list.checkedTasks[position]
    }

}