package com.intprog.schoolkkd

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button


class LogoutActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logout)

        val button_yes = findViewById<Button>(R.id.button_yes)
        val button_no = findViewById<Button>(R.id.button_no)

        button_yes.setOnClickListener{
            val intent = Intent(this,LoginActivity::class.java )
            startActivity(intent)
        }
        button_no.setOnClickListener{
            val intent = Intent(this,SettingsActivity::class.java )
            startActivity(intent)
        }
    }
}