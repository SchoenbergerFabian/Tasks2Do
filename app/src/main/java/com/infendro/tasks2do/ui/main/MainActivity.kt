package com.infendro.tasks2do.ui.main

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import com.infendro.tasks2do.*
import com.infendro.tasks2do.List
import com.infendro.tasks2do.Storage
import java.io.File
import java.io.FileInputStream


class MainActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener {

    //TODO add IDs to List and Task
    //TODO save accordingly for every situation
    //TODO Kotlin Coroutines

    companion object{
        private lateinit var activity : Activity
        private lateinit var sharedPreferences : SharedPreferences

        lateinit var username: String
        lateinit var password: String

        fun changeLoginInfo(username: String, password: String) : Boolean{
            if(this.username == username && this.password == password) return false

            sharedPreferences.edit()
                .putString(activity.getString(R.string.username),username).putString(
                activity.getString(R.string.password),password)
                .apply()
            this.username = username
            this.password = password

            return true
        }

        lateinit var lists : Lists

        fun save(activity: Activity){
            Storage.saveToPhone(activity, lists)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        activity = this
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        username = sharedPreferences.getString(getString(R.string.username),"")?:""
        password = sharedPreferences.getString(getString(R.string.password),"")?:""

        lists = loadLists(username, password)

        changeTheme(sharedPreferences.getString(getString(R.string.theme_key),getString(R.string.system_val)))

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    private fun loadLists(username: String, password: String) : Lists {
        return if(username!=""){ //TODO if password cannot be "" then check as well
            PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean(getString(R.string.first_load),false).apply()
            if(Storage.hasInternetConnection(this)){
                //TODO load in async task so the application doesn't stop?
                Storage.loadFromCloud(username, password) //TODO watch out for errors!
            }else{
                //TODO launch async task to load as soon as internet is connected, update UI somehow?
                Storage.loadFromPhone(this)
            }
        }else{
            Storage.loadFromPhone(this)
        }
    }

    override fun onResume() {
        super.onResume()

        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()

        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(null)
    }

    override fun onSharedPreferenceChanged(sharedPrefs: SharedPreferences, key: String) {
        when(key){
            getString(R.string.theme_key) -> {
                changeTheme(sharedPrefs.getString(key,getString(R.string.system_val)))
            }
        }
    }

    private fun changeTheme(value: String?){
        when(value) {
            getString(R.string.system_val) -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
            getString(R.string.darkmode_val) -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            getString(R.string.lightmode_val) -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }
}