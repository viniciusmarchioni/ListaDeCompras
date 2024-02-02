package com.viniciusmarchioni.compras

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import com.example.compras.R

class AboutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        val backButton = findViewById<ImageButton>(R.id.backbutton)
        val google = findViewById<TextView>(R.id.google)
        val efects = findViewById<TextView>(R.id.zapsplat)

        backButton.setOnClickListener{
            startActivity(Intent(this, ConfigActivity::class.java))
        }


        google.setOnClickListener{
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/google/material-design-icons")))
        }

        efects.setOnClickListener{
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.zapsplat.com/basic-member-home/")))
        }


    }
}