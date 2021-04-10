package com.infendro.tasks2do.ui.main.main

import android.app.Activity
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.infendro.tasks2do.R
import com.infendro.tasks2do.List
import com.infendro.tasks2do.Storage.Account
import com.infendro.tasks2do.Storage.Connection
import com.infendro.tasks2do.Storage.Storage
import com.infendro.tasks2do.Task
import com.infendro.tasks2do.ui.main.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AdapterList(private val activity: Activity, private val list: List) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object{
        private var checkedTasksShown = false
        private var checkedTasks = false
        private lateinit var adapter : AdapterList
    }

    init {
        adapter = this
        checkedTasks =list.checkedTasks.isNotEmpty()
    }

    private val HEADER = 1
    private val DROPDOWN = 2
    private val SPACE = 3

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            HEADER -> ViewHolderHeader(LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.view_header, viewGroup, false))
            DROPDOWN -> ViewHolderDropdown(LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.view_dropdown, viewGroup, false),
            list)
            SPACE -> ViewHolderSpace(LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.view_space, viewGroup, false))
            else -> ViewHolderTask(
                activity,
                LayoutInflater.from(viewGroup.context)
                    .inflate(R.layout.view_task, viewGroup, false),
                list
            )
        }

    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        if(viewHolder is ViewHolderHeader){
            viewHolder.bind(list.title)
        }else if(viewHolder is ViewHolderDropdown){
            viewHolder.bind()
        }else if(viewHolder is ViewHolderSpace){
            //do nothing
        }else{
            (viewHolder as ViewHolderTask).bind(getItem(position))
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

    override fun getItemViewType(adapterPosition: Int): Int {
        return when {
            isHeader(adapterPosition) -> {
                HEADER
            }
            isDropdown(adapterPosition) -> {
                DROPDOWN
            }
            isSpace(adapterPosition) -> {
                SPACE
            }
            else -> {
                0
            }
        }
    }

    private fun isHeader(adapterPosition: Int) : Boolean {
        return adapterPosition==getHeaderIndex()
    }

    private fun getHeaderIndex() : Int{
        return 0
    }

    private fun isDropdown(adapterPosition: Int) : Boolean {
        return adapterPosition==getDropdownIndex()
    }

    private fun getDropdownIndex() : Int{
        return when(checkedTasks){
            true -> list.uncheckedTasks.size+1
            false -> -1
        }
    }

    private fun isSpace(adapterPosition: Int) : Boolean {
        return adapterPosition == getSpaceIndex()
    }

    private fun getSpaceIndex() : Int{
        return list.uncheckedTasks.size + when(checkedTasks){
            true -> 2 + when(checkedTasksShown){
                true -> list.checkedTasks.size
                false -> 0
            }
            false -> 1
        }
    }

    private fun getItem(adapterPosition: Int) : Task {
        if(isChecked(adapterPosition)){
            return list.checkedTasks[getCheckedIndex(adapterPosition)]
        }
        return list.uncheckedTasks[getUncheckedIndex(adapterPosition)]
    }

    private fun isChecked(adapterPosition: Int) : Boolean{
        if(adapterPosition-1<list.uncheckedTasks.size){
            return false
        }
        return true
    }

    fun getUncheckedIndex(adapterPosition: Int) : Int{
        return adapterPosition - 1
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

    class ViewHolderTask(private val activity: Activity, view: View, val list: List) : RecyclerView.ViewHolder(view) {

        private val linearLayout: LinearLayout = view.findViewById(R.id.linearLayout)

        private val buttonCheck: ImageView = view.findViewById(R.id.buttonCheck)

        private val textViewTitle: TextView = view.findViewById(R.id.textViewTitle)
        private val textViewDetails: TextView = view.findViewById(R.id.textViewDetails)
        private val textViewDue: TextView = view.findViewById(R.id.textViewDue)

        fun bind(task : Task){
            bindLayout(task)

            bindButton(task)

            setCheckedImage(task)

            textViewTitle.text = task.title

            val details = task.details.toString()
            if(task.details!=null){
                val detailsLines = details.split("\n")
                if(detailsLines.size<=2){
                    textViewDetails.text = details
                }else{
                    textViewDetails.text = "${detailsLines[0]}\n${detailsLines[1]}..."
                }
                textViewDetails.visibility = View.VISIBLE
            }else{
                textViewDetails.text = ""
                textViewDetails.visibility = View.GONE
            }

            bindDue(task)
        }

        private fun bindDue(task: Task){
            val due = task.getDueString(activity.getString(R.string.pattern_date),activity.getString(R.string.pattern_time))
            if(due!=null&&!task.checked){
                textViewDue.text = due
                textViewDue.visibility = View.VISIBLE
                if(task.isOver()){
                    textViewDue.setTextColor(activity.getColor(R.color.due_late))
                }else{
                    textViewDue.setTextColor(activity.getColor(R.color.due))
                }
            }else{
                textViewDue.text = ""
                textViewDue.visibility = View.GONE
            }
        }

        private fun bindLayout(task: Task){
            linearLayout.setOnClickListener {
                val bundle = bundleOf("LIST" to list,"TASK" to task, "INDEX" to when(task.checked){true -> adapter.getCheckedIndex(adapterPosition) false -> adapter.getUncheckedIndex(adapterPosition)})
                activity.findNavController(R.id.nav).navigate(R.id.action_fragment_Main_to_fragmentDetail, bundle)
            }
        }

        private fun bindButton(task: Task){
            buttonCheck.setOnClickListener {
                buttonCheck.setOnClickListener(null)
                when(task.checked){
                    true -> {
                        list.uncheck(adapter.getCheckedIndex(adapterPosition))
                        MainActivity.save(activity)
                        if(Account.isLoggedIn()){
                            if(Connection.hasInternetConnection(activity)){
                                GlobalScope.launch(Dispatchers.IO) {
                                    Storage.editTask(list, task)
                                }
                            }else {
                                //TODO
                            }
                        }

                        //update image
                        setCheckedImage(task)

                        adapter.notifyItemMoved(adapterPosition,1)
                        bindDue(task)
                        bindButton(task)

                        if(list.checkedTasks.isEmpty()){
                            adapter.notifyItemRemoved(adapter.itemCount-2)
                            checkedTasks =false
                            checkedTasksShown = false
                        }


                    }
                    false -> {
                        list.check(adapterPosition-1)
                        MainActivity.save(activity)
                        if(Account.isLoggedIn()){
                            if(Connection.hasInternetConnection(activity)){
                                GlobalScope.launch(Dispatchers.IO) {
                                    Storage.editTask(list, task)
                                }
                            }else {
                                //TODO
                            }
                        }

                        checkedTasks=true

                        //update image
                        setCheckedImage(task)

                        if(checkedTasksShown){
                            adapter.notifyItemRemoved(adapterPosition)
                            adapter.notifyItemInserted(adapter.getAdapterPositionOfCheckedIndex0())
                            adapter.notifyItemRangeChanged(adapterPosition, adapter.getAdapterPositionOfCheckedIndex0())
                        }else{
                            adapter.notifyItemRemoved(adapterPosition)
                        }
                    }
                }
            }
        }

        private fun setCheckedImage(task: Task){
            when(task.checked){
                true -> buttonCheck.setImageResource(R.drawable.ic_checked)
                false -> buttonCheck.setImageResource(R.drawable.ic_unchecked)
            }
        }

    }

    class ViewHolderHeader(view: View) : RecyclerView.ViewHolder(view) {

        private val textview_title: TextView = view.findViewById(R.id.textViewTitle)

        fun bind(title: String) {
            textview_title.text=title
        }
    }

    class ViewHolderDropdown(view: View, val list: List) : RecyclerView.ViewHolder(view) {

        private val linearLayout = view.findViewById<LinearLayout>(R.id.linearLayout)
        private val imageViewExpand = view.findViewById<ImageView>(R.id.imageview_expand)

        fun bind() {
            linearLayout.setOnClickListener {
                when(checkedTasksShown){
                    false -> {
                        checkedTasksShown =true
                        adapter.notifyItemRangeInserted(adapterPosition+1,list.checkedTasks.size)

                        if(list.checkedTasks.size>5){
                            FragmentMain.recyclerView.scrollToPosition(adapterPosition+6)
                        }else{
                            FragmentMain.recyclerView.scrollToPosition(adapterPosition+list.checkedTasks.size+1)
                        }

                        //rotate expand icon 180°
                        imageViewExpand.animate().setDuration(200).rotation(180f)
                    }
                    true -> {
                        checkedTasksShown =false
                        adapter.notifyItemRangeRemoved(adapterPosition+1,list.checkedTasks.size)

                        //rotate expand icon back
                        imageViewExpand.animate().setDuration(200).rotation(0f)
                    }
                }
            }
        }
    }

    class ViewHolderSpace(view: View) : RecyclerView.ViewHolder(view) {}

}