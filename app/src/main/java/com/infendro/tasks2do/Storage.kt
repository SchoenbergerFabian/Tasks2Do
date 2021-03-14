package com.infendro.tasks2do

import android.os.Environment
import com.google.gson.Gson
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStreamReader
import java.io.PrintWriter

class Storage {
    companion object{
        fun save(outputStream: FileOutputStream, lists: Lists){
            val printWriter = PrintWriter(outputStream)
            printWriter.println(Gson().toJson(lists))
            printWriter.flush()
            printWriter.close()
        }

        fun load(inputStream: FileInputStream) : Lists{
            return Gson().fromJson(InputStreamReader(inputStream), Lists::class.java)
        }

        fun isSDMounted() : Boolean {
            return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
        }
    }
}