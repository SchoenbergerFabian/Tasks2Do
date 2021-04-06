package com.infendro.tasks2do.ui.main.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.infendro.tasks2do.List
import com.infendro.tasks2do.ui.main.MainActivity

class ViewModelMain : ViewModel() {
    val list : MutableLiveData<List> by lazy {
        MutableLiveData<List>()
    }

    fun loadCurrentList(){
        list.value = MainActivity.lists.getCurrentList()
    }
}