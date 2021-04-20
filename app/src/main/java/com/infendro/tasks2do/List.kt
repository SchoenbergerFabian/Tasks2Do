package com.infendro.tasks2do

import java.io.Serializable

class List(var title: String, val uncheckedTasks: ArrayList<Task>, val checkedTasks: ArrayList<Task>) : Serializable {
    constructor(title: String) : this(title,ArrayList<Task>(),ArrayList<Task>())
    constructor() : this("",ArrayList<Task>(),ArrayList<Task>())

    var id = -1

    fun getTask(id: Int) : Task? {
        uncheckedTasks.forEach {
            if(it.id==id){
                return it
            }
        }
        checkedTasks.forEach {
            if(it.id==id){
                return it
            }
        }
        return null
    }

    fun check(task: Task){
        uncheckedTasks.remove(task)
        task.checked=true
        checkedTasks.add(0,task)
    }

    fun uncheck(task: Task){
        checkedTasks.remove(task)
        task.checked=false
        uncheckedTasks.add(0,task)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as List

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id
    }


}