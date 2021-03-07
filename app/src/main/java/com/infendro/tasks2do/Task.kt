package com.infendro.tasks2do

import java.io.Serializable
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class Task(var title: String, var details: String?, var due_date: LocalDate?, var due_time: LocalTime?, var checked: Boolean) : Serializable {
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

    fun isOver() : Boolean {
        val now_date = LocalDate.now()
        if(due_date!=null){
            if(now_date.isAfter(due_date)){
                return true
            }else if(now_date.isEqual(due_date)){
                val now_time = LocalTime.now()

                if(due_time!=null){
                    if(now_time.hour>due_time!!.hour){
                        return true
                    }else if(now_time.hour==due_time!!.hour){
                        if(now_time.minute>due_time!!.minute){
                            return true
                        }
                    }
                }
            }
        }

        return false
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Task

        if (title != other.title) return false
        if (details != other.details) return false
        if (due_date != other.due_date) return false
        if (due_time != other.due_time) return false
        if (checked != other.checked) return false

        return true
    }

    override fun hashCode(): Int {
        var result = title.hashCode()
        result = 31 * result + (details?.hashCode() ?: 0)
        result = 31 * result + (due_date?.hashCode() ?: 0)
        result = 31 * result + (due_time?.hashCode() ?: 0)
        result = 31 * result + checked.hashCode()
        return result
    }


}