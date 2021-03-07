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
import java.time.LocalDate
import java.time.LocalTime


class MainActivity : AppCompatActivity() {

    //TODO edit tasks (partially done)
    //TODO settings fragment with preferences
    //TODO save on sd card or internal storage (gson)
    //TODO add time when creating Todo

    //TODO program personal serialisation format

    //TODO multiple Todo lists (drawer from below)
    //TODO better edittext style
    //TODO highlighting

    companion object{
        val lists = Lists() //TODO load
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        //TODO remove later
        val list = List()

        list.title = "TEST"

        TEST_addTask("TEST1",list)
        TEST_addTask("TEST2",list)
        TEST_addTask_withDetails("TEST3",list)
        TEST_addTask_withDetails("TEST4",list)
        TEST_addTask_withDetails_withDueDateTime("TEST5",list)
        TEST_addTask_withDetails_withDueDateTime("TEST6",list)
        TEST_addTask_withDueDate("TEST7",list)
        TEST_addTask_withDueDate("TEST8",list)
        TEST_addTask_withDueDateTime("TEST9",list)
        TEST_addTask_withDueDateTime("TEST10",list)
        TEST_addTask_checked("TEST11",list)
        TEST_addTask_checked("TEST12",list)
        TEST_addTask_checked("TEST13",list)
        TEST_addTask_checked("TEST14",list)
        TEST_addTask_checked("TEST15",list)
        TEST_addTask_checked("TEST16",list)
        TEST_addTask_checked("TEST17",list)
        TEST_addTask_checked("TEST18",list)

        lists.addList(list)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    fun TEST_addTask(title: String,list: List){
        val task = Task()
        task.title = "$title"
        list.uncheckedTasks.add(task)
    }

    fun TEST_addTask_withDetails(title: String,list: List){
        val task = Task()
        task.title = "$title details"
        task.details = "details"
        list.uncheckedTasks.add(task)
    }

    fun TEST_addTask_withDueDate(title: String,list: List){
        val task = Task()
        task.title = "$title date"
        task.due_date = LocalDate.now()
        list.uncheckedTasks.add(task)
    }

    fun TEST_addTask_withDueDateTime(title: String,list: List){
        val task = Task()
        task.title = "$title datetime"
        task.due_date = LocalDate.now()
        task.due_time = LocalTime.now()
        list.uncheckedTasks.add(task)
    }

    fun TEST_addTask_withDetails_withDueDateTime(title: String,list: List){
        val task = Task()
        task.title = "$title details datetime"
        task.details = "details"
        task.due_date = LocalDate.now()
        task.due_time = LocalTime.now()
        list.uncheckedTasks.add(task)
    }

    fun TEST_addTask_checked(title: String,list: List){
        val task = Task()
        task.title = "$title checked"
        task.checked=true
        list.checkedTasks.add(task)
    }
}