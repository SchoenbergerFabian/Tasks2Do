package com.infendro.tasks2do.ui.main.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.infendro.tasks2do.List
import com.infendro.tasks2do.Task
import com.infendro.tasks2do.ui.main.MainActivity

class ViewModelDetail : ViewModel() {
    val listId : MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

    val list : MutableLiveData<List?> by lazy {
        MutableLiveData<List?>()
    }

    val taskId : MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

    val task : MutableLiveData<Task?> by lazy {
        MutableLiveData<Task?>()
    }

    fun setListId(listId: Int){
        this.listId.value = listId
    }

    fun setList(list: List?){
        this.list.value = list
    }

    fun setTaskId(taskId: Int){
        this.taskId.value = taskId
    }

    fun setTask(task: Task?){
        this.task.value = task
    }
}