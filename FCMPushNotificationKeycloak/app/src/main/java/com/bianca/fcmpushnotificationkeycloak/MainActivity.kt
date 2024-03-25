package com.bianca.fcmpushnotificationkeycloak

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

    private lateinit var timerTextView: TextView
    private lateinit var codeTextView: TextView
    private lateinit var countDownTimer: CountDownTimer
    private lateinit var expiredTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        expiredTextView = findViewById(R.id.expiredTextView)
        expiredTextView.visibility = View.INVISIBLE

        val mapButton = findViewById<ImageButton>(R.id.buttonMap)
        mapButton.setOnClickListener {

            // TODO: Replace with the actual location!!!!!!!!!!!

            val location = "Romania"
            val gmmIntentUri =
                Uri.parse("geo:0,0?q=" + Uri.encode(location))
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")

            // Check if the Maps application is available
            if (mapIntent.resolveActivity(packageManager) != null) {
                startActivity(mapIntent)
            } else {
                // Handle the situation where the Maps application is not installed
                Toast.makeText(this@MainActivity, "Maps app is not installed", Toast.LENGTH_LONG)
                    .show()
            }
        }

        // TIMER
        timer()
    }
    fun timer() {
        timerTextView = findViewById(R.id.timerTextView)
        codeTextView = findViewById(R.id.textViewCode)

        val twoMinutesInMilli = 2 * 60 * 1000L

        countDownTimer = object : CountDownTimer(twoMinutesInMilli, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                // Refreshes the user interface every second
                val timeLeft = String.format(Locale.getDefault(), "%d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)))
                timerTextView.text = timeLeft
            }

            override fun onFinish() {
                // Hide the passcode when the timer reaches zero
                codeTextView.visibility = View.INVISIBLE
                timerTextView.visibility = View.GONE // Hide the timer

                expiredTextView.visibility = View.VISIBLE
            }
        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer.cancel()
    }
}