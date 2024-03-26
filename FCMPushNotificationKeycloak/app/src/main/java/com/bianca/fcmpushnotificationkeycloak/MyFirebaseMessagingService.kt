package com.bianca.fcmpushnotificationkeycloak

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

const val channelId = "notification_channel"
const val channelName = "KeycloakPN"
class MyFirebaseMessagingService : FirebaseMessagingService() {
    companion object {
        private const val MAX_ATTEMPTS = 3
        private const val RETRY_DELAY_MS = 30000L // 30 seconds
    }

    // send fcm token
    // When the user downloads the application, he will automatically receive from FCM a token that he should send to Keycloak,
    // but he will not be able to send it if he is not logged in with the credentials in the android application
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.v("FCMTokenInfo", token)
        attemptToSendTokenToServer(token)
    }
    

    // To the /fcmToken endpoint URL, send the FCM token and a JWT access token for authentication
    // A periodic submission will be made in case of failure (JWT currently unavailable) until the submission is successful
    private fun attemptToSendTokenToServer(token: String, attemptCount: Int = 0) {
        val jwtToken = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE).getString("JWT_TOKEN", null)
        if (jwtToken != null) {
            sendTokenToServer(token, jwtToken, attemptCount)
            Log.d("FCMToken", "JWT: " + jwtToken)
        } else {
            if(attemptCount < MAX_ATTEMPTS)
            {
                Log.e("FCMToken", "JWT Token is not available, will retry in 30 seconds")
                Handler(Looper.getMainLooper()).postDelayed({
                    attemptToSendTokenToServer(token, attemptCount + 1)
                }, RETRY_DELAY_MS)
            }
            else{
                Log.e("FCMToken", "Max attempts reached. Could not send FCM Token without JWT.")
                return
            }
        }
    }
    private fun sendTokenToServer(token: String, jwtToken: String, attemptCount: Int) {
        val retrofitInstance = RetrofitClientInstance.getRetrofitInstance()
        val keycloakService = retrofitInstance.create(RetrofitClientInstance.KeycloakService::class.java)

        val fcmTokenRequest = RetrofitClientInstance.FcmTokenRequest(token)
        val call = keycloakService.sendFcmToken("Bearer $jwtToken", fcmTokenRequest)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.d("FCMToken", "FCM token sent successfully")
                } else {
                    Log.e("FCMToken", "Error sending FCM token, Response Code: ${response.code()}")
                    if (response.code() == 401 && attemptCount < MAX_ATTEMPTS) {
                        attemptToSendTokenToServer(token, attemptCount + 1)
                    }
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("FCMToken", "Error sending FCM token", t)
            }
        })
    }

    // 1. generate the notification
    // 2. attach the notification created with the custom layout
    // 3. show the notification

    // here display the notifications
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

        // Decline button
        val declineIntent = Intent(this, DeclineActionReceiver::class.java)
        declineIntent.setAction("ACTION_DECLINE")
        val declinePendingIntent = PendingIntent.getBroadcast(this, 0, declineIntent,
            PendingIntent.FLAG_IMMUTABLE)


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
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .addAction(R.drawable.ic_decline, getString(R.string.decline), declinePendingIntent)
            .setStyle(NotificationCompat.BigTextStyle().bigText("Much longer text that cannot fit one line..."))

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