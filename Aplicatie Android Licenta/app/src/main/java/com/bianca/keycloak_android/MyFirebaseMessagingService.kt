package com.bianca.keycloak_android

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage : RemoteMessage) {
        // Logica pentru gestionarea mesajelor push
    }

    override fun onNewToken(token: String) {
        // Logica pentru trimiterea noului token la server(my backend)
    }
}