package com.infendro.tasks2do

class Tasks {
    val tasks = ArrayList<Task>()

    fun add(task: Task){
        tasks.add(task)
    }

    fun get(index: Int) : Task {
        return tasks[index]
    }

    fun size() : Int {
        return tasks.size
    }
}