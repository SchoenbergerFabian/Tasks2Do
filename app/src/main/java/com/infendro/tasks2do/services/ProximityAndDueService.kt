package com.infendro.tasks2do.services

import android.app.Activity
import android.app.Service
import android.content.Intent
import android.location.Location
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.infendro.tasks2do.Lists
import com.infendro.tasks2do.Notification.Notification
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class ProximityAndDueService : Service() {

    private lateinit var worker: Thread

    private lateinit var lists: Lists

    override fun onBind(intent: Intent?): IBinder? {
        //not needed
        return null
    }

    override fun onCreate() {
        worker = Thread(this::doWork)
        super.onCreate()
    }

    private val maxProximity = 1 //km
    private fun doWork(){
        while(true){
            try {
                val currentLongAndLat = com.infendro.tasks2do.Connection.Location.getLongAndLat(this)
                var currentLocation : Location? = null
                if(currentLongAndLat!=null){
                    currentLocation = Location("currentLocation")
                    currentLocation.longitude = currentLongAndLat[0]
                    currentLocation.latitude = currentLongAndLat[1]
                }

                lists.lists.forEach { list ->
                    list.uncheckedTasks.forEach { task ->
                        if(task.dueDate!=null && LocalDate.now().isAfter(task.dueDate)
                            && (task.dueTime==null || LocalTime.now().isAfter(task.dueTime))){
                            Notification.notifyDue(this, list, task)
                        }

                        if(currentLocation!=null && task.locationInfo!=null){
                            val taskLocation = Location("taskLocation")
                            taskLocation.longitude = task.locationInfo!!.longitude
                            taskLocation.latitude = task.locationInfo!!.latitude
                            if(currentLocation.distanceTo(taskLocation)/1000<=maxProximity){
                                Notification.notifyProximity(this, list, task)
                            }
                        }
                    }
                }

                Thread.sleep(60000)
            } catch (ex: InterruptedException) {
                Log.println(Log.INFO, "", "thread interrupted")
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        lists = intent?.getSerializableExtra("LISTS") as Lists
        if(!worker.isAlive) worker.start()
        return START_STICKY
    }

    override fun onDestroy() {
        worker.interrupt()
        super.onDestroy()
    }
}