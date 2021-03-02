package com.infendro.tasks2do

import java.lang.IndexOutOfBoundsException

class Lists(val lists: ArrayList<List>, var currentList: Int) {
    constructor() : this(ArrayList<List>(),-1)
    fun addList(list: List){
        lists.add(list)
        currentList=lists.size-1
    }

    fun getCurrentList() : List? {
        return try {
            lists[currentList]
        }catch (ex: IndexOutOfBoundsException){
            null
        }
    }
}