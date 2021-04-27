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

        private fun buildNotification(context: Context, icon: Int, title: String, text: String, priority: Int): NotificationCompat.Builder{
            return NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(icon)
                .setContentTitle(title)
                .setContentText(text)
                .setPriority(priority)
                .setAutoCancel(true)
        }

        private fun notifyIfAllowed(context: Context, notificationId: Int, builder: NotificationCompat.Builder){
            if(allowed){
                with(NotificationManagerCompat.from(context)) {
                    notify(notificationId, builder.build())
                }
            }
        }

        private const val ID_TASKS_TODAY = 0
        fun notifyTasksToday(context: Context, numberOfTasksToday: Int){
            val builder = buildNotification(context, R.drawable.ic_list_bulleted,"Tasks open today:", "$numberOfTasksToday", NotificationCompat.PRIORITY_LOW)
            notifyIfAllowed(context, ID_TASKS_TODAY, builder)
        }

        private const val ID_GROUP_UPLOADED = 1
        private const val GROUP_UPLOADED = "Tasks2Do_Uploaded"
        fun notifyUploaded(context: Context, list: List, task: Task){
            //summary
            val groupBuilder = buildNotification(context, R.drawable.ic_check, "Tasks have been uploaded to the cloud","", NotificationCompat.PRIORITY_LOW)
                .setGroup(GROUP_UPLOADED)
                .setGroupSummary(true)
            notifyIfAllowed(context, ID_GROUP_UPLOADED, groupBuilder)

            //individual notification
            val bundle = bundleOf("LIST_ID" to list.id,"TASK_ID" to task.id)
            val builder = buildNotification(context, R.drawable.ic_check,task.title, task.details?:"", NotificationCompat.PRIORITY_LOW)
                .setGroup(GROUP_UPLOADED)
                .setContentIntent(NavDeepLinkBuilder(context)
                    .setGraph(R.navigation.nav)
                    .setDestination(R.id.fragmentDetail)
                    .setArguments(bundle)
                    .createPendingIntent())
            notifyIfAllowed(context, task.id, builder)
        }

        private const val ID_GROUP_DUE = 2
        private const val GROUP_DUE = "Tasks2Do_Due"
        fun notifyDue(context: Context, list: List, task: Task){
            //summary
            val groupBuilder = buildNotification(context, R.drawable.ic_check, "Tasks due today","", NotificationCompat.PRIORITY_LOW)
                .setGroup(GROUP_DUE)
                .setGroupSummary(true)
            notifyIfAllowed(context, ID_GROUP_DUE, groupBuilder)

            //individual notification
            val bundle = bundleOf("LIST_ID" to list.id,"TASK_ID" to task.id)
            val builder = buildNotification(context, R.drawable.ic_check, task.title, task.details?:"", NotificationCompat.PRIORITY_LOW)
                .setGroup(GROUP_DUE)
                .setContentIntent(NavDeepLinkBuilder(context)
                    .setGraph(R.navigation.nav)
                    .setDestination(R.id.fragmentDetail)
                    .setArguments(bundle)
                    .createPendingIntent())
            notifyIfAllowed(context, task.id, builder)
        }

        private const val ID_GROUP_PROXIMITY = 3
        private const val GROUP_PROXIMITY = "Tasks2Do_Proximity"
        fun notifyProximity(context: Context, list: List, task: Task){
            //summary
            val groupBuilder = buildNotification(context, R.drawable.ic_check, "Tasks created near you","", NotificationCompat.PRIORITY_LOW)
                .setGroup(GROUP_PROXIMITY)
                .setGroupSummary(true)
            notifyIfAllowed(context, ID_GROUP_PROXIMITY, groupBuilder)

            //individual notification
            val bundle = bundleOf("LIST_ID" to list.id,"TASK_ID" to task.id)
            val builder = buildNotification(context, R.drawable.ic_check, task.title, task.details?:"", NotificationCompat.PRIORITY_LOW)
                .setGroup(GROUP_PROXIMITY)
                .setContentIntent(NavDeepLinkBuilder(context)
                    .setGraph(R.navigation.nav)
                    .setDestination(R.id.fragmentDetail)
                    .setArguments(bundle)
                    .createPendingIntent())
            notifyIfAllowed(context, task.id, builder)
        }
    }

}