package com.example.compras

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat

class ConfigActivity : AppCompatActivity() {

    var sound:Boolean = true
    var vibration:Boolean = true
    var notification:Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_config)

        val soundSwitch = findViewById<SwitchCompat>(R.id.musicSwitch)
        val vibrationSwitch = findViewById<SwitchCompat>(R.id.vibrationSwitch)
        val notificationSwitch = findViewById<SwitchCompat>(R.id.notificationsSwitch)
        val about = findViewById<TextView>(R.id.sobreTextView)
        val backButton = findViewById<ImageButton>(R.id.backbutton)



        backButton.setOnClickListener{
            Intent(this@ConfigActivity, MainActivity::class.java)
        }

        notificationSwitch.setOnClickListener {
            notification = it.isActivated

        }
        vibrationSwitch.setOnClickListener {
            vibration = it.isActivated
        }
        soundSwitch.setOnClickListener {
            sound = it.isActivated
        }

        about.setOnClickListener {

        }


    }
}