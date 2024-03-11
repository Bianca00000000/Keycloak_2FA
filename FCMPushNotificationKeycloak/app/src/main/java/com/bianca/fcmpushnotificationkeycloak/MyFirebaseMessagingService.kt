package com.bianca.fcmpushnotificationkeycloak

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

const val channelId = "notification_channel"
const val channelName = "KeycloakPN"
class MyFirebaseMessagingService : FirebaseMessagingService() {

    // 1. generate the notification
    // 2. attach the notification created with the custom layout
    // 3. show the notification


    // here we display the notifications
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if(remoteMessage.getNotification() != null) {
            generateNotification(remoteMessage.notification!!.title!!, remoteMessage.notification!!.body!!)
        }
    }

    // suggests that the annotation is used to suppress warnings about using layouts in RemoteViews
    @SuppressLint("RemoteViewLayout")
    fun getRemoteView(title: String, message: String): RemoteViews {
        val remoteView = RemoteViews("com.bianca.fcmpushnotificationkeycloak", R.layout.notification)

        // by this we associate what should be displayed in the interface with what we want
        remoteView.setTextViewText(R.id.title, title)
        remoteView.setTextViewText(R.id.message, message)
        remoteView.setImageViewResource(R.id.app_logo, R.drawable.logo)

        return remoteView
    }

    fun generateNotification(title: String, message: String) {
        // create an intent that when the user clicks on the notification, the application will open with details about the connection
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) // puts the current activity at the top

        // provides a way to pass an intent that will be executed on behalf of your application at a later time by another application
        // PendingIntent.FLAG_ONE_SHOT => indicates that the PendingIntent can be used only once. After it is executed, it will become invalid.
        // PendingIntent.FLAG_IMMUTABLE => specifies that the PendingIntent cannot be modified after it is created.
        val pendingIntent = PendingIntent.getActivity(this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)


        // channel id, channel name = constants to identify the notification more easily
        // The NotificationCompat class is used in Android development to create notifications in a way that is
        // compatible with different versions of Android. It offers a smooth interface that makes it easy to build and
        // customize notifications. NotificationCompat.Builder is an inner class of NotificationCompat
        // and is used to build the notification object itself.
        var builder: NotificationCompat.Builder = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.logo)
            .setAutoCancel(true) // the notification is automatically dismissed when the user taps it
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000)) // the phone will vibrate for a second, after which it will wait for a second, twice
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)

        // now we have to link the intent to the notification.xml
        builder = builder.setContent(getRemoteView(title, message))

        // create the Notification Manager
        // NotificationManager is used to manage notifications (showing, updating or canceling them).
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // a check is made that the api is greater than or equal to 26, otherwise the notifications are not supported
        // as of api 26 all notifications must be posted to a NotificationChannel to provide the user with better control over notifications.
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel) // the created channel is then registered in the system
        }

        // display notification
        notificationManager.notify(0, builder.build())

    }
}