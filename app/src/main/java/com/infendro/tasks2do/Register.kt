package com.infendro.tasks2do

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.IOException
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL

class Register {
    companion object{
        fun register(username: String, password: String) {
            //TODO do in kotlin coroutine
            GlobalScope.launch {
                try{
                    val url = URL("http://sickinger-solutions.at/notesserver/register.php")
                    (url.openConnection() as? HttpURLConnection)?.run{
                        requestMethod = "POST"
                        setRequestProperty("Content-Type","application/json")
                        doOutput = true
                        val data = "{\"username\": \"$username\", \"password\": \"$password\", \"name\":\"\"}".toByteArray()
                        setFixedLengthStreamingMode(data.size)
                        outputStream.write(data)
                        outputStream.flush()

                        /*val content = StringBuilder()
                        val reader = BufferedReader(InputStreamReader(inputStream))

                        var line = reader.readLine()
                        while(line!=null) {
                            content.append(line)
                            line = reader.readLine()
                        }
                        println("3 $content")
                        reader.close()*/
                        //TODO handle response?
                    }
                }catch(ex: IOException){
                    ex.printStackTrace() //TODO
                }
            }
        }
    }
}