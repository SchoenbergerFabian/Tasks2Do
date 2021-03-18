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
import java.io.FileOutputStream
import java.time.LocalDate
import java.time.LocalTime


class MainActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener {

    companion object{
        lateinit var lists : Lists

        fun save(activity: Activity){
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
            when(sharedPreferences.getString(activity.getString(R.string.location_key),"")){
                activity.getString(R.string.phone_val) -> {
                    val outputStream = activity.openFileOutput(activity.getString(R.string.filename), Context.MODE_PRIVATE)
                    Storage.save(outputStream, lists)
                }
                activity.getString(R.string.sdcard_val) -> {
                    if(Storage.isSDMounted()){
                        val outputStream = FileOutputStream(File(activity.getExternalFilesDir(null),activity.getString(R.string.filename)))
                        Storage.save(outputStream, lists)
                    }else{
                        Toast.makeText(activity,activity.getString(R.string.sd_not_accessible),Toast.LENGTH_SHORT).show()
                    }
                }
                else /*change to phone*/ -> {
                    sharedPreferences.edit().putString(activity.getString(R.string.location_key),activity.getString(R.string.phone_val)).apply()
                    save(activity)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        lists = loadLists(sharedPreferences.getString(getString(R.string.location_key),""))

        changeTheme(sharedPreferences.getString(getString(R.string.theme_key),getString(R.string.system_val)))

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    private fun loadLists(location: String?) : Lists {
        return when(location){
            getString(R.string.phone_val) -> {
                val inputStream = openFileInput(getString(R.string.filename))
                Storage.load(inputStream)
            }
            getString(R.string.sdcard_val) -> {
                if(Storage.isSDMounted()){
                    val inputStream = FileInputStream(File(getExternalFilesDir(null),getString(R.string.filename)))
                    Storage.load(inputStream)
                }else{
                    Toast.makeText(this,getString(R.string.sd_not_accessible),Toast.LENGTH_SHORT).show()
                    Lists()
                }
            }
            else -> {
                //first time loading:
                //create new lists object and add a default list to it
                //save lists object on phone and return it
                val lists = Lists()
                val list = List()
                list.title = "ToDo"
                lists.addList(list)
                val outputStream = openFileOutput(getString(R.string.filename), Context.MODE_PRIVATE)
                Storage.save(outputStream, lists)
                lists
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
        preferenceChanged(sharedPrefs, key)
    }

    private fun preferenceChanged(sharedPrefs: SharedPreferences, key: String){
        when(key){
            getString(R.string.theme_key) -> {
                changeTheme(sharedPrefs.getString(key,getString(R.string.system_val)))
            }
            getString(R.string.location_key) -> {
                save(this)
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