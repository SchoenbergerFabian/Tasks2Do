package com.infendro.tasks2do.Storage

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class Connection {

    companion object{
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

        suspend fun post(urlString : String, data: String) : Response {
            val url = URL(urlString)
            val connection = url.openConnection() as HttpURLConnection
            connection.run{
                doOutput = true
                requestMethod = "POST"
                setRequestProperty("Content-Type","application/json")
                setFixedLengthStreamingMode(data.toByteArray().size)
            }

            val outputStream = connection.outputStream
            outputStream.write(data.toByteArray())
            outputStream.flush()
            outputStream.close()

            val responseCode = connection.responseCode
            return  if(responseCode/100==4||responseCode/100==5){
                Response(connection.errorStream, responseCode)
            }else{
                Response(connection.inputStream, responseCode)
            }
        }

        suspend fun put(urlString : String, data: String) : Response {
            val url = URL(urlString)
            val connection = url.openConnection() as HttpURLConnection
            connection.run{
                doOutput = true
                requestMethod = "PUT"
                setRequestProperty("Content-Type","application/json")
                setFixedLengthStreamingMode(data.toByteArray().size)
            }

            val outputStream = connection.outputStream
            outputStream.write(data.toByteArray())
            outputStream.flush()
            outputStream.close()

            val responseCode = connection.responseCode
            return  if(responseCode/100==4||responseCode/100==5){
                Response(connection.errorStream, responseCode)
            }else{
                Response(connection.inputStream, responseCode)
            }
        }

        suspend fun delete(urlString : String) : Response {
            val url = URL(urlString)
            val connection = url.openConnection() as HttpURLConnection
            connection.run{
                doOutput = true
                requestMethod = "DELETE"
                setRequestProperty("Content-Type","application/json")
            }

            val responseCode = connection.responseCode
            return  if(responseCode/100==4||responseCode/100==5){
                Response(connection.errorStream, responseCode)
            }else{
                Response(connection.inputStream, responseCode)
            }
        }

        suspend fun get(urlString : String) : Response {
            val url = URL(urlString)
            val connection = url.openConnection() as HttpURLConnection
            connection.run{
                requestMethod = "GET"
                setRequestProperty("Content-Type","application/json")
            }

            val responseCode = connection.responseCode
            return  if(responseCode/100==4||responseCode/100==5){
                Response(connection.errorStream, responseCode)
            }else{
                Response(connection.inputStream, responseCode)
            }
        }
    }

    class Response(var response: InputStream, var responseCode: Int){
        fun codeStartsWith(number: Int) : Boolean {
            return responseCode/100==number
        }

        fun close(){
            response.close()
        }
    }
}