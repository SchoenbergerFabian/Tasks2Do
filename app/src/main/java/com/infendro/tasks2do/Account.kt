package com.infendro.tasks2do

import android.app.Activity
import androidx.preference.PreferenceManager
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.infendro.tasks2do.ui.main.MainActivity
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.IOException
import java.io.InputStreamReader
import kotlin.coroutines.suspendCoroutine

class Account {
    companion object {

        lateinit var username: String
        lateinit var password: String

        fun changeLoginInfo(activity: Activity, username: String, password: String) : Boolean{
            if(this.username == username && this.password == password) return false

            PreferenceManager.getDefaultSharedPreferences(activity).edit()
                .putString(activity.getString(R.string.username_key),username)
                .putString(activity.getString(R.string.password_key),password)
                .apply()
            this.username = username
            this.password = password

            return true
        }

        suspend fun signIn(username: String, password: String) : Boolean {
            val params = JsonObject()
            params.addProperty("username", username)
            params.addProperty("password", password)
            params.addProperty("name", "x")
            val response = Storage.post(
                "http://sickinger-solutions.at/notesserver/register.php",
                params.toString()
            )

            println(response.responseCode)
            val success = response.codeStartsWith(2)
            response.close()
            return success
        }

        suspend fun isCorrect(username: String, password: String): Boolean {
            val response = Storage.post("http://sickinger-solutions.at/notesserver/todolists.php?username=$username&password=$password", "")
            val json = JsonParser().parse(InputStreamReader(response.response).readText()).asJsonObject
            response.close()
            return !(json.has("message") && json.get("message").asString == "username or password not found")
        }

        fun isValid(username: String, password: String) : Boolean {
            val regex = Regex("[A-Za-z0-9]+")
            return username.matches(regex)&&password.matches(regex)
        }

        fun isLoggedIn() : Boolean {
            return username!=""
        }
    }
}