package com.bianca.fcmpushnotificationkeycloak

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
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
            client_id = "DemoVotingApp",
            grant_type = "password",
            client_secret = "R7wP6CWdYaG9RUAAgJu5VfLKA86KuVWr",
            scope = "openid",
            username = username,
            password = password
        )

        call.enqueue(object : Callback<AccessToken> {
            override fun onResponse(call: Call<AccessToken>, response: Response<AccessToken>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@LoginActivity, "Login Successful", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    val errorBody = response.errorBody()?.string()
                    Toast.makeText(this@LoginActivity, "Login Failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<AccessToken>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "Login Failed", Toast.LENGTH_SHORT).show()
            }
        })
    }


}