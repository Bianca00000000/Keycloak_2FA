package com.bianca.fcmpushnotificationkeycloak

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

class RetrofitClientInstance {
    companion object {
        private var retrofit: Retrofit? = null
        private const val BASE_URL = "http://192.168.1.132:8080"

        fun getRetrofitInstance(): Retrofit {
            if (retrofit == null) {
                val logging = HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }

                val client = OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .build()

                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit!!
        }

    }
    data class FcmTokenRequest(val token: String)

    interface KeycloakService {
        @POST("/realms/pnrealm/token-fcm/fcmToken")
        fun sendFcmToken(@Header("Authorization") authorization: String, @Body fcmTokenRequest: FcmTokenRequest): Call<Void>
    }
}