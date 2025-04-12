package com.intprog.schoolkkd

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button


class SettingsActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val button_developers = findViewById<Button>(R.id.button_developers)
        val button_logout = findViewById<Button>(R.id.button_logout)

        button_developers.setOnClickListener{
            val intent = Intent(this,DeveloperActivity::class.java )
            startActivity(intent)
        }

        button_logout.setOnClickListener{
            val intent = Intent(this,LogoutActivity::class.java )
            startActivity(intent)
        }

    }
}