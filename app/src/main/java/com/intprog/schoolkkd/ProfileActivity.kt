package com.intprog.schoolkkd

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProfileActivity : Activity() {

    private lateinit var profileService: ProfileService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val logoutButton = findViewById<Button>(R.id.button_logout)
        logoutButton.setOnClickListener{
            val intent = Intent(this,LogoutActivity::class.java )
            startActivity(intent)
        }

        val sharedPrefs = getSharedPreferences("userPrefs", MODE_PRIVATE)
        val username = sharedPrefs.getString("username", "")
        val password = sharedPrefs.getString("password", "")

        if (username.isNullOrEmpty() || password.isNullOrEmpty()) {
            Toast.makeText(this, "User credentials not found", Toast.LENGTH_LONG).show()
            navigateToLogin()
            return
        }

        initRetrofit()
        fetchUserInfo(username, password)
    }

    private fun initRetrofit() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://astonishing-unruly-tomato.glitch.me") // Check endpoint correctness
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        profileService = retrofit.create(ProfileService::class.java)
    }

    private fun fetchUserInfo(username: String, password: String) {
        profileService.getUserInfo(username, password).enqueue(object : Callback<ProfileResponse> {
            override fun onResponse(call: Call<ProfileResponse>, response: Response<ProfileResponse>) {
                if (response.isSuccessful) {
                    val profileResponse = response.body()
                    profileResponse?.let {
                        populateUserInfo(it)
                    } ?: run {
                        Toast.makeText(this@ProfileActivity, "User info not found", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(this@ProfileActivity, "Failed to fetch user info", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                Toast.makeText(this@ProfileActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun populateUserInfo(profile: ProfileResponse) {
        findViewById<TextView>(R.id.textview_username).text = "Username: ${profile.username}"
        findViewById<TextView>(R.id.textview_firstname).text = "First Name: ${profile.firstname}"
        findViewById<TextView>(R.id.textview_middlename).text = "Middle Name: ${profile.middlename ?: "N/A"}"
        findViewById<TextView>(R.id.textview_lastname).text = "Last Name: ${profile.lastname}"
        findViewById<TextView>(R.id.textview_student_id).text = "Student ID: ${profile.studentId}"
        findViewById<TextView>(R.id.textview_course).text = "Course: ${profile.course}"
        findViewById<TextView>(R.id.textview_year_level).text = "Year Level: ${profile.yearLevel ?: "N/A"}"
        findViewById<TextView>(R.id.textview_classification).text = "Classification: ${profile.classification ?: "N/A"}"
        findViewById<TextView>(R.id.textview_college).text = "College: ${profile.college ?: "N/A"}"
        findViewById<TextView>(R.id.textview_mobile).text = "Mobile: ${profile.mobile ?: "N/A"}"
        findViewById<TextView>(R.id.textview_birthdate).text = "Birthdate: ${profile.birthdate ?: "N/A"}"
        findViewById<TextView>(R.id.textview_gender).text = "Gender: ${profile.gender ?: "N/A"}"
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LogoutActivity::class.java)
        startActivity(intent)
        finish()
    }
}
