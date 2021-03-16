package com.infendro.tasks2do

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

        fun save(outputStream: FileOutputStream, lists: Lists){
            val printWriter = PrintWriter(outputStream)
            printWriter.println(getGson().toJson(lists))
            printWriter.flush()
            printWriter.close()
        }

        fun load(inputStream: FileInputStream) : Lists{
            return getGson().fromJson(InputStreamReader(inputStream), Lists::class.java)
        }

        fun isSDMounted() : Boolean {
            return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
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