package com.infendro.tasks2do

import androidx.recyclerview.widget.SortedList
import androidx.recyclerview.widget.SortedListAdapterCallback

class Tasks {
    val tasks = ArrayList<Task>()
    val checkedTasks = ArrayList<Task>()

    fun check(position: Int){
        val task = tasks.removeAt(position)
        task.checked=true
        checkedTasks.add(0,task)

        println(task.checked)
        println(task.title)
        println(task.details)
        println(task.getDueString("dd.MM.yyyy","HH:mm"))
    }

    fun uncheck(position: Int){
        val task = checkedTasks.removeAt(position)
        task.checked=false
        tasks.add(0,task)

        println(task.checked)
        println(task.title)
        println(task.details)
        println(task.getDueString("dd.MM.yyyy","HH:mm"))
    }
}