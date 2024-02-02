package com.viniciusmarchioni.compras

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import com.example.compras.R

class ConfigActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_config)

        val soundSwitch = findViewById<SwitchCompat>(R.id.musicSwitch)
        val vibrationSwitch = findViewById<SwitchCompat>(R.id.vibrationSwitch)
        val notificationSwitch = findViewById<SwitchCompat>(R.id.notificationsSwitch)
        val about = findViewById<TextView>(R.id.sobreTextView)
        val backButton = findViewById<ImageButton>(R.id.backbutton)
        val pref: SharedPreferences = getSharedPreferences("config-xml", MODE_PRIVATE)


        //definindo valores dos switchs
        soundSwitch.isChecked = pref.getBoolean("sound", true)
        notificationSwitch.isChecked = pref.getBoolean("notification", true)
        vibrationSwitch.isChecked = pref.getBoolean("vibration", true)



        backButton.setOnClickListener{
            startActivity(Intent(this, MainActivity::class.java))
        }

        notificationSwitch.setOnCheckedChangeListener { _, isChecked ->
            val editor: SharedPreferences.Editor = pref.edit()
            editor.putBoolean("notification", isChecked)
            editor.apply()

        }

        vibrationSwitch.setOnCheckedChangeListener { _, isChecked ->
            val editor: SharedPreferences.Editor = pref.edit()
            editor.putBoolean("vibration", isChecked)
            editor.apply()
        }
        soundSwitch.setOnCheckedChangeListener { _, isChecked ->
            val editor: SharedPreferences.Editor = pref.edit()
            editor.putBoolean("sound", isChecked)
            editor.apply()
        }

        about.setOnClickListener {
            startActivity(Intent(this, AboutActivity::class.java))
        }


    }
}