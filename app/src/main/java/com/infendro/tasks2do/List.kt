package com.infendro.tasks2do

import java.io.Serializable

class List(var title: String, val uncheckedTasks: ArrayList<Task>, val checkedTasks: ArrayList<Task>) : Serializable {
    constructor(title: String) : this(title,ArrayList<Task>(),ArrayList<Task>())
    constructor() : this("",ArrayList<Task>(),ArrayList<Task>())

    fun check(position: Int){
        val task = uncheckedTasks.removeAt(position)
        task.checked=true
        checkedTasks.add(0,task)
    }

    fun uncheck(position: Int){
        val task = checkedTasks.removeAt(position)
        task.checked=false
        uncheckedTasks.add(0,task)
    }
}