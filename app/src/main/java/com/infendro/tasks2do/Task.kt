package com.infendro.tasks2do

import java.io.Serializable
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class Task(var title: String?, var details: String?, var dueDate: LocalDate?, var dueTime: LocalTime?, var checked: Boolean) : Serializable {
    constructor() : this("", null, null, null, false)

    fun getDueString(pattern_date: String, pattern_time: String) : String? {
        if(dueDate!=null){
            if(dueTime!=null){
                return dueDate?.atTime(dueTime)?.format(DateTimeFormatter.ofPattern("$pattern_date, $pattern_time"))
            }else{
                return dueDate?.format(DateTimeFormatter.ofPattern(pattern_date))
            }
        }else{
            return null
        }
    }

    fun isOver() : Boolean {
        val now_date = LocalDate.now()
        if(dueDate!=null){
            if(now_date.isAfter(dueDate)){
                return true
            }else if(now_date.isEqual(dueDate)){
                val now_time = LocalTime.now()

                if(dueTime!=null){
                    if(now_time.hour>dueTime!!.hour){
                        return true
                    }else if(now_time.hour==dueTime!!.hour){
                        if(now_time.minute>dueTime!!.minute){
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
        if (dueDate != other.dueDate) return false
        if (dueTime != other.dueTime) return false
        if (checked != other.checked) return false

        return true
    }

    override fun hashCode(): Int {
        var result = title.hashCode()
        result = 31 * result + (details?.hashCode() ?: 0)
        result = 31 * result + (dueDate?.hashCode() ?: 0)
        result = 31 * result + (dueTime?.hashCode() ?: 0)
        result = 31 * result + checked.hashCode()
        return result
    }


}