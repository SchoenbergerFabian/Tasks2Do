package com.infendro.tasks2do.activities.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.infendro.tasks2do.R
import com.infendro.tasks2do.Task
import com.infendro.tasks2do.Tasks
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.time.LocalDate
import java.time.LocalTime

class MainActivity : AppCompatActivity() {

    lateinit var adapter : Adapter_Tasks
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

        adapter = Adapter_Tasks(this,tasks)
        recyclerview_tasks.adapter = adapter
    }

    fun TEST_addTask(){
        val task = Task()
        task.title = "TEST"
        tasks.add(task)
    }

    fun TEST_addTask_withDetails(){
        val task = Task()
        task.title = "TEST details"
        task.details = "details"
        tasks.add(task)
    }

    fun TEST_addTask_withDueDate(){
        val task = Task()
        task.title = "TEST date"
        task.due_date = LocalDate.now()
        tasks.add(task)
    }

    fun TEST_addTask_withDueDateTime(){
        val task = Task()
        task.title = "TEST datetime"
        task.due_date = LocalDate.now()
        task.due_time = LocalTime.now()
        tasks.add(task)
    }

    fun TEST_addTask_withDetails_withDueDateTime(){
        val task = Task()
        task.title = "TEST details datetime"
        task.details = "details"
        task.due_date = LocalDate.now()
        task.due_time = LocalTime.now()
        tasks.add(task)
    }
}