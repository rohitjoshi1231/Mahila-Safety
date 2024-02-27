package com.domingo.mahila_saftey.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.domingo.mahila_saftey.R
import com.domingo.mahila_saftey.Utils

class WelcomeActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = getSharedPreferences("isWelcomed", Context.MODE_PRIVATE)

        window.statusBarColor = Utils.customColor(this, R.color.app_background)

        // Check if the user has already been welcomed
        if (!isWelcomed()) {
            // If not welcomed yet, show the welcome screen
            setContentView(R.layout.activity_welcome)
            val btnNext = findViewById<Button>(R.id.btnNext)
            btnNext.setOnClickListener {
                // Mark the user as welcomed
                markAsWelcomed()
                // Navigate to com.domingo.mahila_saftey.activities.MainActivity
                val intent = Intent(this@WelcomeActivity, MainActivity::class.java)
                startActivity(intent)
                finish() // Finish WelcomeActivity so that it's not returned to when pressing back
            }
        } else {
            // If already welcomed, directly navigate to com.domingo.mahila_saftey.activities.MainActivity
            val intent = Intent(this@WelcomeActivity, MainActivity::class.java)
            startActivity(intent)
            finish() // Finish WelcomeActivity so that it's not returned to when pressing back
        }
    }

    private fun isWelcomed(): Boolean {
        return sharedPreferences.getBoolean("isWelcomed", false)
    }

    private fun markAsWelcomed() {
        val editor = sharedPreferences.edit()
        editor.putBoolean("isWelcomed", true)
        editor.apply()
    }
}
