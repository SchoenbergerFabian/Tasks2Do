package com.infendro.tasks2do

class List(var title: String, val uncheckedTasks: ArrayList<Task>, val checkedTasks: ArrayList<Task>) {
    constructor() : this("",ArrayList<Task>(),ArrayList<Task>())

    fun check(position: Int){
        val task = uncheckedTasks.removeAt(position)
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
        uncheckedTasks.add(0,task)

        println(task.checked)
        println(task.title)
        println(task.details)
        println(task.getDueString("dd.MM.yyyy","HH:mm"))
    }
}