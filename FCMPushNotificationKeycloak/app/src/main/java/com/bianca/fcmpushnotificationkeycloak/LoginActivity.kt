package com.bianca.fcmpushnotificationkeycloak

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.j2objc.annotations.ObjectiveCName
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class LoginActivity : AppCompatActivity() {

    private lateinit var etPassword: EditText
    private lateinit var etUsername: EditText
    private lateinit var btnLogin: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etPassword = findViewById(R.id.etPassword)
        etUsername = findViewById(R.id.etUsername)
        btnLogin = findViewById(R.id.btnLogin)

        btnLogin.setOnClickListener(View.OnClickListener(){
            getAccessToken()
        })

        val tvDontHavePhone = findViewById<TextView>(R.id.tvNoPhone)
        tvDontHavePhone.setOnClickListener {
            // Handle the click event here
        }
    }

    fun getAccessToken() {
        val service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)
        val password = etPassword.text.toString()
        val username = etUsername.text.toString()

        val call = service.getAccessToken(
            client_id = "AndroidApplicationNotification",
            grant_type = "password",
            client_secret = "fNNuNb3yLoU0GEYPsToVSk6Rvtt1OoBc",
            scope = "openid",
            username = username,
            password = password
        )

        call.enqueue(object : Callback<AccessToken> {
            override fun onResponse(call: Call<AccessToken>, response: Response<AccessToken>) {
                if (response.isSuccessful) {
                    // Get the AccessToken object from the response
                    val accessTokenResponse = response.body()
                    if (accessTokenResponse != null) {
                        // Extract the access token String
                        val accessToken = accessTokenResponse.accessToken
                        // Save the access token String to SharedPreferences
                        val sharedPreferences =
                            getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
                        sharedPreferences.edit().putString("JWT_TOKEN", accessToken).apply()

                        Toast.makeText(this@LoginActivity, "Login Successful", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        Log.e("LoginActivity", "Access Token Response is null")
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Toast.makeText(
                        this@LoginActivity,
                        "Login Failed: $errorBody",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

                override fun onFailure(call: Call<AccessToken>, t: Throwable) {
                    // Handle failure: network error, etc.
                    Toast.makeText(this@LoginActivity, "Login Failed: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
