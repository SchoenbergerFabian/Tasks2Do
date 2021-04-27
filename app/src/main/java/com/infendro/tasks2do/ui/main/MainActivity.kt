package com.infendro.tasks2do.ui.main

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import com.infendro.tasks2do.*
import com.infendro.tasks2do.Connection.Storage
import com.infendro.tasks2do.Connection.Account
import com.infendro.tasks2do.Connection.Connection.Companion.hasInternetConnection
import com.infendro.tasks2do.Notification.Notification
import com.infendro.tasks2do.services.ProximityAndDueService
import com.infendro.tasks2do.ui.main.detail.ViewModelDetail
import com.infendro.tasks2do.ui.main.main.ViewModelMain
import kotlinx.coroutines.*
import java.io.FileNotFoundException
import java.time.LocalDate


class MainActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener {

    //TODO are you sure? when logging out
    //TODO continue as soon as internet is connected again

    companion object{
        private lateinit var activity : Activity

        lateinit var lists : Lists

        fun save(activity: Activity){
            val lists  = lists
            Storage.saveToPhone(activity, lists)
        }
    }

    private lateinit var proximityAndDueService: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        activity = this
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        Account.username = sharedPreferences.getString(getString(R.string.username_key),"")?:""
        Account.password = sharedPreferences.getString(getString(R.string.password_key),"")?:""

        changeTheme(sharedPreferences.getString(getString(R.string.theme_key),getString(R.string.system_val)))

        if(sharedPreferences.getBoolean("first_time", true)){
            registerForActivityResult(ActivityResultContracts.RequestPermission()){ isGranted: Boolean ->
                if(!isGranted) {
                    Toast.makeText(this, "You can grant location permissions in your settings later!", Toast.LENGTH_LONG).show() //TODO
                }
            }.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            sharedPreferences.edit().putBoolean("first_time", false).apply()
        }

        load()

        Notification.allowed = sharedPreferences.getBoolean(getString(R.string.notifications_key),true)
        Notification.createNotificationChannel(activity)
        Notification.notifyTasksToday(activity, lists.getNumberOfOpenTasksToday())

        startProximityAndDueService()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun startProximityAndDueService() {
        proximityAndDueService = Intent(this,ProximityAndDueService::class.java)
        proximityAndDueService.putExtra("LISTS", lists)
        startService(proximityAndDueService)
    }

    fun stopProximityAndDueService() {
        stopService(proximityAndDueService)
    }

    fun load() {
        //load
        lists = try {
            Storage.loadFromPhone(activity)
        }catch(ex: FileNotFoundException){
            Lists()
        }
        if(Account.isLoggedIn()){
            if(hasInternetConnection(this)){
                GlobalScope.launch(Dispatchers.Main) {
                    lists = withContext(Dispatchers.IO){
                        return@withContext Storage.getTodoLists(lists.currentList)
                    }

                    stopProximityAndDueService()
                    startProximityAndDueService()

                    save(activity)

                    val modelMain : ViewModelMain by viewModels()
                    modelMain.loadCurrentList()
                    val modelDetail : ViewModelDetail by viewModels()
                    if(modelDetail.listId.value!=null&&modelDetail.taskId.value!=null){
                        modelDetail.setList(lists.getList(modelDetail.listId.value!!))
                        modelDetail.setTask(modelDetail.list.value?.getTask(modelDetail.taskId.value!!))
                    }
                }
            }else{
                //TODO
            }
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