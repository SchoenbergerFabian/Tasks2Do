package com.infendro.tasks2do.ui.main

import android.app.Activity
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.findNavController
import androidx.preference.PreferenceManager
import com.infendro.tasks2do.*
import com.infendro.tasks2do.Storage.Storage
import com.infendro.tasks2do.Storage.Account
import com.infendro.tasks2do.Storage.Connection.Companion.hasInternetConnection
import com.infendro.tasks2do.ui.main.main.FragmentMain
import kotlinx.coroutines.*


class MainActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener {

    //TODO save accordingly for every situation
    //TODO how to handle loading/saving when logging in/logging out/signing in
    //TODO are you sure? when logging out
    //TODO continue as soon as internet is connected again

    companion object{
        private lateinit var activity : Activity

        lateinit var lists : Lists

        fun save(activity: Activity){
            Storage.saveToPhone(activity, lists)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        activity = this
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        lists = Storage.loadFromPhone(activity)

        Account.username = sharedPreferences.getString(getString(R.string.username_key),"")?:""
        Account.password = sharedPreferences.getString(getString(R.string.password_key),"")?:""

        changeTheme(sharedPreferences.getString(getString(R.string.theme_key),getString(R.string.system_val)))

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        GlobalScope.launch {
            val lists = loadLists(Account.username, Account.password)
            if(lists!=null){
                Companion.lists = lists
//                FragmentMain.updateUI()
            }
        }
    }

    private suspend fun loadLists(username: String, password: String) : Lists? {
        return if(username!=""){ //TODO if password cannot be "" then check as well
            if(hasInternetConnection(this)){
                val lists = Storage.loadFromCloud(username, password)
                lists.currentList = MainActivity.lists.currentList
                lists
            }else{
                //TODO load as soon as internet is connected, update UI
                null
            }
        }else{
            null
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