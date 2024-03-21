package com.bianca.fcmpushnotificationkeycloak

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

class RetrofitClientInstance {
    companion object {
        private var retrofit: Retrofit? = null
        private const val BASE_URL = "http://192.168.174.203:8080"

        fun getRetrofitInstance(): Retrofit {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit!!
        }

    }
    data class FcmTokenRequest(val token: String)

    interface KeycloakService {
        @POST("/auth/realms/pnrealm/custom-endpoints/fcmToken")
        fun sendFcmToken(@Header("Authorization") authorization: String, @Body fcmTokenRequest: FcmTokenRequest): Call<Void>
    }
}