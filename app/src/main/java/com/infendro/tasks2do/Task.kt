package com.infendro.tasks2do

import java.io.Serializable
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class Task(var title: String, var details: String?, var dueDate: LocalDate?, var dueTime: LocalTime?, var checked: Boolean, var locationInfo: LocationInfo?) : Serializable {
    constructor() : this("", null, null, null, false, null)

    var id = -1

    fun getDueString(patternDate: String, patternTime: String) : String? {
        return if(dueDate!=null){
            if(dueTime!=null){
                getDueDateString(patternDate)+" "+getDueTimeString(patternTime)
            }else{
                getDueDateString(patternDate)
            }
        }else{
            null
        }
    }

    fun getDueDateString(patternDate : String) : String? {
        return dueDate?.format(DateTimeFormatter.ofPattern(patternDate))
    }

    fun getDueTimeString(patternTime: String) : String? {
        return dueTime?.format(DateTimeFormatter.ofPattern(patternTime))
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

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id
    }


}