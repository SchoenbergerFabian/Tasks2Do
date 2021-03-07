package com.infendro.tasks2do.activities.main.fragments.main.adapters

import android.app.Activity
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.infendro.tasks2do.R
import com.infendro.tasks2do.List
import com.infendro.tasks2do.Task
import com.infendro.tasks2do.activities.main.fragments.main.Fragment_Main

class Adapter(private val activity: Activity, private val list: List) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object{
        private var checkedTasksShown = false
        private var checkedTasks = false
        private lateinit var adapter : Adapter
    }

    init {
        adapter = this
        checkedTasks=list.checkedTasks.isNotEmpty()
    }

    val HEADER = 1
    val DROPDOWN = 2
    val SPACE = 3

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            HEADER -> ViewHolder_Header(LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.view_header, viewGroup, false))
            DROPDOWN -> ViewHolder_Dropdown(LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.view_dropdown, viewGroup, false),
            list)
            SPACE -> ViewHolder_Space(LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.view_space, viewGroup, false))
            else -> ViewHolder_Task(
                activity,
                LayoutInflater.from(viewGroup.context)
                    .inflate(R.layout.view_task, viewGroup, false),
                list
            )
        }

    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        if(viewHolder is ViewHolder_Header){
            viewHolder.bind(list.title)
        }else if(viewHolder is ViewHolder_Dropdown){
            viewHolder.bind()
        }else if(viewHolder is ViewHolder_Space){
            //do nothing
        }else{
            (viewHolder as ViewHolder_Task).bind(getItem(position))
        }
    }

    override fun getItemCount() : Int{
        return list.uncheckedTasks.size + when(checkedTasks){
            false -> 2 //only header and space
            true -> 3 /*header + dropdown + space*/ + when(checkedTasksShown){
                true -> list.checkedTasks.size
                false -> 0
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(isHeader(position)){
            HEADER
        }else if(isDropdown(position)){
            DROPDOWN
        }else if(isSpace(position)){
            SPACE
        }else{
            0
        }
    }

    fun isHeader(position: Int) : Boolean {
        return position==getHeaderIndex()
    }

    fun getHeaderIndex() : Int{
        return 0
    }

    fun isDropdown(position: Int) : Boolean {
        return position==getDropdownIndex()
    }

    fun getDropdownIndex() : Int{
        return when(checkedTasks){
            true -> list.uncheckedTasks.size+1
            false -> -1
        }
    }

    fun isSpace(position: Int) : Boolean {
        return position == getSpaceIndex()
    }

    fun getSpaceIndex() : Int{
        return list.uncheckedTasks.size + when(checkedTasks){
            true -> 2 + when(checkedTasksShown){
                true -> list.checkedTasks.size
                false -> 0
            }
            false -> 1
        }
    }

    fun getItem(adapterPosition: Int) : Task {
        if(isChecked(adapterPosition)){
            return list.checkedTasks[getCheckedIndex(adapterPosition)]
        }
        return list.uncheckedTasks[getUncheckedIndex(adapterPosition)]
    }

    fun isChecked(adapterPosition: Int) : Boolean{
        if(adapterPosition-1<list.uncheckedTasks.size){
            return false
        }
        return true
    }

    fun getUncheckedIndex(position: Int) : Int{
        return position - 1
    }

    fun getCheckedIndex(adapterPosition: Int) : Int{
        return adapterPosition - list.uncheckedTasks.size - 2
    }

    fun getAdapterPositionOfUncheckedIndex0() : Int {
        return 1
    }

    fun getAdapterPositionOfCheckedIndex0() : Int {
        return list.uncheckedTasks.size+2
    }

    class ViewHolder_Task(private val activity: Activity, view: View, val list: List) : RecyclerView.ViewHolder(view) {

        val layout: LinearLayout = view.findViewById(R.id.layout)

        private val button_check: ImageView = view.findViewById(R.id.button_check)

        private val textview_title: TextView = view.findViewById(R.id.textview_title)
        private val textview_details: TextView = view.findViewById(R.id.textview_details)
        private val textview_due: TextView = view.findViewById(R.id.textview_due)

        fun bind(task : Task){
            bindLayout(task)

            bindButton(task)

            setCheckedImage(task)

            textview_title.text = task.title

            val details = task.details.toString()
            if(task.details!=null){
                val detailsLines = details.split("\n")
                if(detailsLines.size<=2){
                    textview_details.text = details
                }else{
                    textview_details.text = "${detailsLines[0]}\n${detailsLines[1]}..."
                }
                textview_details.visibility = View.VISIBLE
            }else{
                textview_details.text = ""
                textview_details.visibility = View.GONE
            }

            val due = task.getDueString(activity.getString(R.string.pattern_date),activity.getString(R.string.pattern_time))
            if(due!=null&&!task.checked){
                textview_due.text = due
                textview_due.visibility = View.VISIBLE
                if(task.isOver()){
                    textview_due.setTextColor(activity.getColor(R.color.due_late))
                }else{
                    textview_due.setTextColor(activity.getColor(R.color.due))
                }
            }else{
                textview_due.text = ""
                textview_due.visibility = View.GONE
            }
        }

        private fun bindLayout(task: Task){
            layout.setOnClickListener {
                val bundle = bundleOf("LIST" to list,"TASK" to task, "INDEX" to when(task.checked){true -> adapter.getCheckedIndex(adapterPosition) false -> adapter.getUncheckedIndex(adapterPosition)})
                activity.findNavController(R.id.nav).navigate(R.id.action_fragment_Main_to_fragmentDetail, bundle)
            }
        }

        private fun bindButton(task: Task){
            button_check.setOnClickListener {
                button_check.setOnClickListener(null)
                when(task.checked){
                    true -> {
                        list.uncheck(adapter.getCheckedIndex(adapterPosition))

                        //update image
                        setCheckedImage(task)

                        adapter.notifyItemMoved(adapterPosition,1)
                        bindButton(task)

                        if(list.checkedTasks.isEmpty()){
                            adapter.notifyItemRemoved(adapter.itemCount-2)
                            checkedTasks=false
                            checkedTasksShown = false
                        }


                    }
                    false -> {
                        list.check(adapterPosition-1)
                        checkedTasks=true

                        //update image
                        setCheckedImage(task)

                        if(checkedTasksShown){
                            adapter.notifyItemRemoved(adapterPosition)
                            adapter.notifyItemInserted(adapter.getAdapterPositionOfCheckedIndex0())
                            adapter.notifyItemRangeChanged(adapterPosition,adapter.getAdapterPositionOfCheckedIndex0())
                        }else{
                            adapter.notifyItemRemoved(adapterPosition)
                        }
                    }
                }
            }
        }

        private fun setCheckedImage(task: Task){
            when(task.checked){
                true -> button_check.setImageResource(R.drawable.ic_checked)
                false -> button_check.setImageResource(R.drawable.ic_unchecked)
            }
        }

    }

    class ViewHolder_Header(view: View) : RecyclerView.ViewHolder(view) {

        private val textview_title: TextView = view.findViewById(R.id.textview_title)

        fun bind(title: String) {
            textview_title.text=title
        }
    }

    class ViewHolder_Dropdown(view: View, val list: List) : RecyclerView.ViewHolder(view) {

        private val layout = view.findViewById<LinearLayout>(R.id.layout)
        private val imageview_expand = view.findViewById<ImageView>(R.id.imageview_expand)

        fun bind() {
            layout.setOnClickListener {
                when(checkedTasksShown){
                    false -> {
                        checkedTasksShown=true
                        adapter.notifyItemRangeInserted(adapterPosition+1,list.checkedTasks.size)

                        if(list.checkedTasks.size>5){
                            Fragment_Main.recyclerview.scrollToPosition(adapterPosition+6)
                        }else{
                            Fragment_Main.recyclerview.scrollToPosition(adapterPosition+list.checkedTasks.size+1)
                        }

                        //rotate expand icon 180°
                        imageview_expand.animate().setDuration(200).rotation(180f)
                    }
                    true -> {
                        checkedTasksShown=false
                        adapter.notifyItemRangeRemoved(adapterPosition+1,list.checkedTasks.size)

                        //rotate expand icon back
                        imageview_expand.animate().setDuration(200).rotation(0f)
                    }
                }
            }
        }
    }

    class ViewHolder_Space(view: View) : RecyclerView.ViewHolder(view) {}

}