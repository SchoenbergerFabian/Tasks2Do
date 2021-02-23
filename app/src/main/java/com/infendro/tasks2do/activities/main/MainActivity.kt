package com.infendro.tasks2do.activities.main

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.infendro.tasks2do.R
import com.infendro.tasks2do.Task
import com.infendro.tasks2do.Tasks
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
        lateinit var adapter : Adapter_Tasks
        lateinit var adapter_checked : Adapter_CheckedTasks
    }

    lateinit var tasks : Tasks

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar.setTitle("Tasks2Do")
        setSupportActionBar(toolbar)

        tasks = Tasks()

        TEST_addTask()
        TEST_addTask_withDetails()
        TEST_addTask_withDetails_withDueDateTime()
        TEST_addTask_withDueDate()
        TEST_addTask_withDueDateTime()
        TEST_addTask_checked()
        TEST_addTask_checked()

        adapter = Adapter_Tasks(this, tasks)
        recyclerview_tasks.adapter = adapter

        adapter_checked = Adapter_CheckedTasks(this, tasks)
        recyclerview_checkedTasks.adapter = adapter_checked

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
    }

    fun TEST_addTask(){
        val task = Task()
        task.title = "TEST"
        tasks.tasks.add(task)
    }

    fun TEST_addTask_withDetails(){
        val task = Task()
        task.title = "TEST details"
        task.details = "details"
        tasks.tasks.add(task)
    }

    fun TEST_addTask_withDueDate(){
        val task = Task()
        task.title = "TEST date"
        task.due_date = LocalDate.now()
        tasks.tasks.add(task)
    }

    fun TEST_addTask_withDueDateTime(){
        val task = Task()
        task.title = "TEST datetime"
        task.due_date = LocalDate.now()
        task.due_time = LocalTime.now()
        tasks.tasks.add(task)
    }

    fun TEST_addTask_withDetails_withDueDateTime(){
        val task = Task()
        task.title = "TEST details datetime"
        task.details = "details"
        task.due_date = LocalDate.now()
        task.due_time = LocalTime.now()
        tasks.tasks.add(task)
    }

    fun TEST_addTask_checked(){
        val task = Task()
        task.title = "TEST checked"
        tasks.tasks.add(task)
        tasks.check(tasks.tasks.size - 1)
    }
}