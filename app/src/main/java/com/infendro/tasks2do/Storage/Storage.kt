package com.infendro.tasks2do.Storage

import android.app.Activity
import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import com.infendro.tasks2do.List
import com.infendro.tasks2do.Lists
import com.infendro.tasks2do.R
import com.infendro.tasks2do.Storage.Connection.Companion.post
import com.infendro.tasks2do.Storage.Connection.Companion.get
import com.infendro.tasks2do.Storage.Connection.Companion.put
import com.infendro.tasks2do.Storage.Connection.Companion.delete
import com.infendro.tasks2do.Task
import com.infendro.tasks2do.ui.main.MainActivity
import org.json.JSONArray
import org.json.JSONObject
import java.io.*
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class Storage {
    companion object{

        suspend fun addList(list: List) : Boolean {
            val currentList = MainActivity.lists.getCurrentList()
            if(currentList!=null){
                editList(currentList)
            }

            val params = JsonObject()
            params.addProperty("name", list.title)
            params.addProperty("additionalData", "x")
            val response = post(
                "http://sickinger-solutions.at/notesserver/todolists.php?username=${Account.username}&password=${Account.password}",
                params.toString()
            )

            val success = response.codeStartsWith(2)
            if(success){
                val resp = JSONObject(response.response.reader().readText())
                list.id = resp.getString("id").toInt()
            }

            response.close()

            return success
        }

        suspend fun getTodoLists() : Lists {
            val lists = Lists()

            val listsResponse = get("http://sickinger-solutions.at/notesserver/todolists.php?username=${Account.username}&password=${Account.password}")

            val listsSuccess = listsResponse.codeStartsWith(2)
            if(listsSuccess) {
                val listsResp = JSONArray(listsResponse.response.reader().readText())
                listsResponse.close()
                for(listIndex in 0 until listsResp.length()){
                    val listResp = listsResp[listIndex] as JSONObject

                    val list = List()
                    list.id = listResp.getString("id").toInt()
                    list.title = listResp.getString("name")

                    lists.lists.add(list)
                }
            }

            if(lists.lists.size!=0){
                lists.currentList=0
            }

            val todosResponse = get("http://sickinger-solutions.at/notesserver/todo.php?username=${Account.username}&password=${Account.password}")

            val todosSuccess = listsResponse.codeStartsWith(2)
            if(todosSuccess){
                val todosResp = JSONArray(todosResponse.response.reader().readText())
                todosResponse.close()
                for(todoIndex in 0 until todosResp.length()){
                    val todoResp = todosResp[todoIndex] as JSONObject

                    val task = Task()
                    task.id = todoResp.getString("id").toInt()
                    task.title = todoResp.getString("title")
                    task.details = todoResp.getString("description")
                    val datetimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                    task.dueDate = LocalDate.parse(todoResp.getString("dueDate"),datetimeFormat)
                    task.checked = todoResp.getString("state")=="x"

                    val list = lists.getList(todoResp.getString("todoListId").toInt())
                    if(task.checked){
                        list?.checkedTasks?.add(task)
                    }else{
                        list?.uncheckedTasks?.add(task)
                    }
                }
            }

            return lists
        }

        suspend fun editList(list: List) : Boolean {
            val params = JsonObject()
            params.addProperty("name", list.title)
            params.addProperty("additionalData", "")
            val response = put(
                "http://sickinger-solutions.at/notesserver/todolists.php?id=${list.id}&username=${Account.username}&password=${Account.password}",
                params.toString()
            )

            val success = response.codeStartsWith(2)
            response.close()

            return success
        }

        suspend fun removeList(list: List) : Boolean {
            val response = delete(
                "http://sickinger-solutions.at/notesserver/todolists.php?id=${list.id}&username=${Account.username}&password=${Account.password}"
            )

            val success = response.codeStartsWith(2)
            response.close()

            return success
        }

        suspend fun addTask(list: List, task: Task) : Boolean {

            val params = JsonObject()
            params.addProperty("todoListId", list.id)
            params.addProperty("title", task.title)
            params.addProperty("description", task.details)
            val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss")
            params.addProperty("dueDate", "${task.dueDate?.format(dateFormat)} ${task.dueTime?.format(timeFormat)}")
            params.addProperty("state", if(task.checked) "x" else "")
            val response = post(
                "http://sickinger-solutions.at/notesserver/todo.php?username=${Account.username}&password=${Account.password}",
                params.toString()
            )

            val success = response.codeStartsWith(2)
            if(success){
                val resp = JSONObject(response.response.reader().readText())
                task.id = resp.getString("id").toInt()
            }

            response.close()

            return success
        }

        suspend fun editTask(list: List, task: Task) : Boolean {
            val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss")

            val params = JsonObject()
            params.addProperty("todoListId", list.id)
            params.addProperty("title", task.title)
            params.addProperty("description", task.details)
            params.addProperty("dueDate", "${task.dueDate?.format(dateFormat)} ${task.dueTime?.format(timeFormat)}")
            params.addProperty("state", if(task.checked) "x" else "o")
            val response = put(
                "http://sickinger-solutions.at/notesserver/todo.php?username=${Account.username}&password=${Account.password}",
                params.toString()
            )

            val success = response.codeStartsWith(2)
            response.close()

            return success
        }

        suspend fun removeTask(task: Task) : Boolean {
            val response = delete(
                "http://sickinger-solutions.at/notesserver/todo.php?id=${task.id}&username=${Account.username}&password=${Account.password}"
            )

            val success = response.codeStartsWith(2)
            response.close()

            return success
        }

        private fun getGson() : Gson {
            return GsonBuilder()
                .registerTypeAdapter(LocalDate::class.java, LocalDateAdapter())
                .registerTypeAdapter(LocalTime::class.java, LocalTimeAdapter())
                .create()
        }

        fun saveToPhone(activity: Activity, lists: Lists){
            val printWriter = PrintWriter(activity.openFileOutput(activity.getString(R.string.filename), Context.MODE_PRIVATE))
            printWriter.println(getGson().toJson(lists))
            printWriter.flush()
            printWriter.close()
        }

        fun loadFromPhone(activity: Activity) : Lists {
            return getGson().fromJson(InputStreamReader(activity.openFileInput(activity.getString(R.string.filename))), Lists::class.java)
        }

    }

    class LocalDateAdapter : TypeAdapter<LocalDate>() {

        val dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy")

        override fun write(writer: JsonWriter?, value: LocalDate?) {
            if(value==null){
                writer?.nullValue()
            }else{
                writer?.value(value.format(dtf))
            }
        }

        override fun read(reader: JsonReader?): LocalDate? {
            if(reader?.peek()==JsonToken.NULL){
                reader.nextNull()
                return null
            }else{
                return LocalDate.parse(reader?.nextString(),dtf)
            }
        }

    }

    class LocalTimeAdapter : TypeAdapter<LocalTime>() {

        val dtf = DateTimeFormatter.ofPattern("HH:mm")

        override fun write(writer: JsonWriter?, value: LocalTime?) {
            if(value==null){
                writer?.nullValue()
            }else{
                writer?.value(value.format(dtf))
            }
        }

        override fun read(reader: JsonReader?): LocalTime? {
            if(reader?.peek()==JsonToken.NULL){
                reader.nextNull()
                return null
            }else{
                return LocalTime.parse(reader?.nextString(),dtf)
            }
        }

    }
}