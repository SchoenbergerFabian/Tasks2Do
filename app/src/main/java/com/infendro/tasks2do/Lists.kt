package com.infendro.tasks2do

import java.io.Serializable
import java.lang.IndexOutOfBoundsException

class Lists(val lists: ArrayList<List>, var currentList: Int) {
    constructor() : this(ArrayList<List>(),-1)

    fun addList(list: List) {
        lists.add(list)
        currentList=lists.size-1
    }

    //returns if current list changed
    fun removeList(list: Int) {
        lists.removeAt(list)

        if(list == currentList){
            currentList = if(lists.size==0){
                -1
            }else{
                0
            }
        }

    }

    fun getCurrentList() : List? {
        return try {
            lists[currentList]
        }catch (ex: IndexOutOfBoundsException){
            null
        }
    }
}