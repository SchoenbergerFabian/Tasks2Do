package com.infendro.tasks2do.Notification

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.os.bundleOf
import androidx.navigation.NavDeepLinkBuilder
import com.infendro.tasks2do.List
import com.infendro.tasks2do.R
import com.infendro.tasks2do.Task
import com.infendro.tasks2do.ui.main.MainActivity
import com.infendro.tasks2do.ui.main.main.AdapterList

class Notification {
    companion object{
        var allowed = true

        private const val CHANNEL_ID = "Tasks2Do"

        fun createNotificationChannel(activity: Activity) {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name = activity.getString(R.string.channel_name)
                val descriptionText = activity.getString(R.string.channel_description)
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                    description = descriptionText
                }
                // Register the channel with the system
                val notificationManager: NotificationManager =
                    activity.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            }
        }

        private fun buildNotification(activity: Activity, icon: Int, title: String, text: String, priority: Int): NotificationCompat.Builder{
            return NotificationCompat.Builder(activity, CHANNEL_ID)
                .setSmallIcon(icon)
                .setContentTitle(title)
                .setContentText(text)
                .setPriority(priority)
                .setAutoCancel(true)
        }

        private fun notifyIfAllowed(activity: Activity, notificationId: Int, builder: NotificationCompat.Builder){
            with(NotificationManagerCompat.from(activity)) {
                notify(notificationId, builder.build())
            }
        }

        private const val ID_TASKS_TODAY = 0
        fun notifyTasksToday(activity: Activity, numberOfTasksToday: Int){
            val builder = buildNotification(activity, R.drawable.ic_list_bulleted,"Tasks open today:", "$numberOfTasksToday", NotificationCompat.PRIORITY_LOW)
            notifyIfAllowed(activity, ID_TASKS_TODAY, builder)
        }

        private const val ID_GROUP_TASK = 1
        private const val GROUP_TASKS = "Tasks2Do_Tasks"
        fun notifyUploaded(activity: Activity, list: List, task: Task){
            //summary
            val groupBuilder = buildNotification(activity, R.drawable.ic_check, "Tasks have been uploaded to the cloud","", NotificationCompat.PRIORITY_LOW)
                .setGroup(GROUP_TASKS)
                .setGroupSummary(true)
            notifyIfAllowed(activity, ID_GROUP_TASK, groupBuilder)

            //individual notification
            val bundle = bundleOf("LIST_ID" to list.id,"TASK_ID" to task.id)
            val builder = buildNotification(activity, R.drawable.ic_check,task.title, task.details?:"", NotificationCompat.PRIORITY_LOW)
                .setGroup(GROUP_TASKS)
                .setContentIntent(NavDeepLinkBuilder(activity)
                    .setGraph(R.navigation.nav)
                    .setDestination(R.id.fragmentDetail)
                    .setArguments(bundle)
                    .createPendingIntent())
            notifyIfAllowed(activity, task.id, builder)}
    }
}