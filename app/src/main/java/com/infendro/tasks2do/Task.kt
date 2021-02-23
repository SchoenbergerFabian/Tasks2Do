package com.infendro.tasks2do

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class Task(var title: String, var details: String?, var due_date: LocalDate?, var due_time: LocalTime?, var checked: Boolean) {
    constructor() : this("", null, null, null, false)

    fun getDueString(pattern_date: String, pattern_time: String) : String? {
        if(due_date!=null){
            if(due_time!=null){
                return due_date?.atTime(due_time)?.format(DateTimeFormatter.ofPattern("$pattern_date, $pattern_time"))
            }else{
                return due_date?.format(DateTimeFormatter.ofPattern(pattern_date))
            }
        }else{
            return null
        }
    }

    fun changeChecked(){
        checked = !checked
    }

}