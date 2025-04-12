package com.intprog.schoolkkd

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button

class LandingActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)

        val button_settings = findViewById<Button>(R.id.button_set)
        val button_profile = findViewById<Button>(R.id.button_profile)
        val button_enroll = findViewById<Button>(R.id.button_enroll)
        val button_grades = findViewById<Button>(R.id.btngrades)
        val button_assessment = findViewById<Button>(R.id.button_assessment)
        val button_studyLoad = findViewById<Button>(R.id.button_studyload)

        button_profile.setOnClickListener{
            val intent = Intent(this,ProfileActivity::class.java )
            startActivity(intent)
        }
        button_assessment.setOnClickListener{
            val intent = Intent(this,AssessmentActivity::class.java )
            startActivity(intent)
        }
        button_grades.setOnClickListener{
            val intent = Intent(this,GradesActivity::class.java )
            startActivity(intent)
        }

        button_enroll.setOnClickListener{
            val intent = Intent(this,EnrollmentActivity::class.java )
            startActivity(intent)
        }

        button_settings.setOnClickListener{
            val intent = Intent(this,SettingsActivity::class.java )
            startActivity(intent)
        }

        button_studyLoad.setOnClickListener{
            val intent = Intent(this,StudyLoadActivity::class.java )
            startActivity(intent)
        }
    }
}