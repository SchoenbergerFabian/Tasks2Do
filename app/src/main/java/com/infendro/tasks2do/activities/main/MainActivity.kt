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
        val lists = Lists() //TODO load
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar.title = ""
        setSupportActionBar(toolbar)


        //TODO remove later
        val list = List()

        list.title = "TEST"

        TEST_addTask(list)
        TEST_addTask(list)
        TEST_addTask_withDetails(list)
        TEST_addTask_withDetails(list)
        TEST_addTask_withDetails_withDueDateTime(list)
        TEST_addTask_withDetails_withDueDateTime(list)
        TEST_addTask_withDueDate(list)
        TEST_addTask_withDueDate(list)
        TEST_addTask_withDueDateTime(list)
        TEST_addTask_withDueDateTime(list)
        TEST_addTask_checked(list)
        TEST_addTask_checked(list)
        TEST_addTask_checked(list)
        TEST_addTask_checked(list)
        TEST_addTask_checked(list)
        TEST_addTask_checked(list)
        TEST_addTask_checked(list)
        TEST_addTask_checked(list)

        lists.addList(list)


    }

    fun TEST_addTask(list: List){
        val task = Task()
        task.title = "TEST"
        list.uncheckedTasks.add(task)
    }

    fun TEST_addTask_withDetails(list: List){
        val task = Task()
        task.title = "TEST details"
        task.details = "details"
        list.uncheckedTasks.add(task)
    }

    fun TEST_addTask_withDueDate(list: List){
        val task = Task()
        task.title = "TEST date"
        task.due_date = LocalDate.now()
        list.uncheckedTasks.add(task)
    }

    fun TEST_addTask_withDueDateTime(list: List){
        val task = Task()
        task.title = "TEST datetime"
        task.due_date = LocalDate.now()
        task.due_time = LocalTime.now()
        list.uncheckedTasks.add(task)
    }

    fun TEST_addTask_withDetails_withDueDateTime(list: List){
        val task = Task()
        task.title = "TEST details datetime"
        task.details = "details"
        task.due_date = LocalDate.now()
        task.due_time = LocalTime.now()
        list.uncheckedTasks.add(task)
    }

    fun TEST_addTask_checked(list: List){
        val task = Task()
        task.title = "TEST checked"
        list.uncheckedTasks.add(task)
        list.check(list.uncheckedTasks.size - 1)
    }
}