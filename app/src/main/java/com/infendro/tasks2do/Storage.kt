package com.infendro.tasks2do

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Environment
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStreamReader
import java.io.PrintWriter
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class Storage {
    companion object{
        private fun getGson() : Gson {
            return GsonBuilder()
                .registerTypeAdapter(LocalDate::class.java,LocalDateAdapter())
                .registerTypeAdapter(LocalTime::class.java,LocalTimeAdapter())
                .create()
        }

        fun saveToPhone(activity: Activity, lists: Lists){
            val printWriter = PrintWriter(activity.openFileOutput(activity.getString(R.string.filename), Context.MODE_PRIVATE))
            printWriter.println(getGson().toJson(lists))
            printWriter.flush()
            printWriter.close()
        }

        fun addList(list: List){
            //TODO
            println("save new list to cloud")
        }

        fun editList(list: List){
            //TODO
            println("save changed list to cloud")
        }

        fun removeList(list: List){
            //TODO
            println("remove list on cloud")
        }

        fun addTask(task: Task){
            //TODO
            println("save new task to cloud")
        }

        fun editTask(task: Task){
            //TODO
            println("save changed task to cloud")
        }

        fun removeTask(task: Task){
            //TODO
            println("remove task on cloud")
        }

        fun loadFromPhone(activity: Activity) : Lists{
            return getGson().fromJson(InputStreamReader(activity.openFileInput(activity.getString(R.string.filename))), Lists::class.java)
        }

        fun loadFromCloud(username: String, password: String) : Lists {
            //TODO
            println("load from cloud")
            return Lists() //temp
        }

        fun hasInternetConnection(activity: Activity) : Boolean {
            val connectivityManager = activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
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