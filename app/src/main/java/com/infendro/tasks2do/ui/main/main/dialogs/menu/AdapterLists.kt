package com.infendro.tasks2do.ui.main.main.dialogs.menu

import android.app.Activity
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.infendro.tasks2do.List
import com.infendro.tasks2do.Lists
import com.infendro.tasks2do.R
import com.infendro.tasks2do.Storage.Account
import com.infendro.tasks2do.Storage.Connection
import com.infendro.tasks2do.Storage.Storage
import com.infendro.tasks2do.ui.main.MainActivity
import com.infendro.tasks2do.ui.main.main.ViewModelMain
import kotlinx.coroutines.*

class AdapterLists(private val activity: Activity, private val lists: Lists, private val viewModelMain: ViewModelMain) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), CoroutineScope by MainScope() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.view_list, viewGroup, false))
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        (viewHolder as ViewHolder).bind(activity, lists, getItem(position), position, viewModelMain)
    }

    override fun getItemCount() : Int{
        return lists.lists.size
    }

    private fun getItem(position: Int) : List {
        return lists.lists[position]
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val layout = view.findViewById<LinearLayout>(R.id.layout)
        private val textViewTitle = view.findViewById<TextView>(R.id.textViewTitle)

        fun bind(activity: Activity, lists: Lists, list: List, index: Int, viewModelMain: ViewModelMain) {
            textViewTitle.text=list.title

            if(lists.currentList==index){
                layout.setBackgroundColor(activity.getColor(R.color.colorHighlighted))
            }else{
                layout.setBackgroundColor(Color.TRANSPARENT)
                layout.setOnClickListener {
                    lists.currentList = index
                    MainActivity.save(activity)

                    viewModelMain.loadCurrentList()

                    BottomSheetDialogMenu.dismiss()
                }
            }
        }
    }

}