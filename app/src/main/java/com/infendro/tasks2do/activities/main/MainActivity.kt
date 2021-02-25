package com.infendro.tasks2do.activities.main

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.infendro.tasks2do.R
import com.infendro.tasks2do.Task
import com.infendro.tasks2do.List
import com.infendro.tasks2do.Lists
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.time.LocalDate
import java.time.LocalTime


class MainActivity : AppCompatActivity() {

    //TODO display empty string resource when todo list is empty
    //TODO support multiple Todo lists (fragments)
    //TODO add new Todo (Dialog/Fragment)
    //TODO refer to homework

    companion object{
        lateinit var adapter_unchecked : Adapter_UncheckedTasks
        lateinit var adapter_checked : Adapter_CheckedTasks

        private lateinit var dropdown_showChecked : LinearLayout
        private lateinit var recyclerview_checkedTasks : RecyclerView
        fun showEmpty(list: List){
            if(list.checkedTasks.isEmpty()){
                dropdown_showChecked.visibility = View.GONE
                dropdown_showChecked.findViewById<ImageView>(R.id.imageview_ic_dropdown).rotation=0f
                recyclerview_checkedTasks.visibility=View.GONE
                recyclerview_checkedTasks.alpha=0f
            }else{
                dropdown_showChecked.visibility = View.VISIBLE
            }
        }
    }

    val lists = Lists() //TODO load
    lateinit var list : List

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar.title = ""
        setSupportActionBar(toolbar)

        //TODO remove later
        list = List()

        list.title = "TEST"

        TEST_addTask()
        TEST_addTask_withDetails()
        TEST_addTask_withDetails_withDueDateTime()
        TEST_addTask_withDueDate()
        TEST_addTask_withDueDateTime()
        TEST_addTask_checked()
        TEST_addTask_checked()

        Companion.dropdown_showChecked = dropdown_showChecked
        Companion.recyclerview_checkedTasks = recyclerview_checkedTasks

        dropdown_showChecked.setOnClickListener {
            if(recyclerview_checkedTasks.visibility==View.GONE){
                recyclerview_checkedTasks.visibility = View.VISIBLE
                recyclerview_checkedTasks.animate().setDuration(100).alpha(1f).setListener(null)
                imageview_ic_dropdown.animate().rotation(180f)
            }else{
                recyclerview_checkedTasks.animate().setDuration(100).alpha(0f).setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        recyclerview_checkedTasks.visibility = View.GONE
                    }
                })
                imageview_ic_dropdown.animate().rotation(0f)
            }
        }

        updateUI()
    }

    fun updateUI(){
        textview_title.text = list.title

        adapter_unchecked = Adapter_UncheckedTasks(this, list)
        recyclerview_tasks.adapter = adapter_unchecked

        adapter_checked = Adapter_CheckedTasks(this, list)
        recyclerview_checkedTasks.adapter = adapter_checked

        showEmpty(list)
    }

    fun TEST_addTask(){
        val task = Task()
        task.title = "TEST"
        list.uncheckedTasks.add(task)
    }

    fun TEST_addTask_withDetails(){
        val task = Task()
        task.title = "TEST details"
        task.details = "details"
        list.uncheckedTasks.add(task)
    }

    fun TEST_addTask_withDueDate(){
        val task = Task()
        task.title = "TEST date"
        task.due_date = LocalDate.now()
        list.uncheckedTasks.add(task)
    }

    fun TEST_addTask_withDueDateTime(){
        val task = Task()
        task.title = "TEST datetime"
        task.due_date = LocalDate.now()
        task.due_time = LocalTime.now()
        list.uncheckedTasks.add(task)
    }

    fun TEST_addTask_withDetails_withDueDateTime(){
        val task = Task()
        task.title = "TEST details datetime"
        task.details = "details"
        task.due_date = LocalDate.now()
        task.due_time = LocalTime.now()
        list.uncheckedTasks.add(task)
    }

    fun TEST_addTask_checked(){
        val task = Task()
        task.title = "TEST checked"
        list.uncheckedTasks.add(task)
        list.check(list.uncheckedTasks.size - 1)
    }
}