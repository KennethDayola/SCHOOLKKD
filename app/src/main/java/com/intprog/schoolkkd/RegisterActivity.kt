package com.intprog.schoolkkd

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RegisterActivity : Activity() {

    private lateinit var registerService: RegisterService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val loginTextView = findViewById<TextView>(R.id.login_text_view)
        loginTextView.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        val retrofit = Retrofit.Builder()
            .baseUrl("https://lime-thread-jaxartosaurus.glitch.me")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        registerService = retrofit.create(RegisterService::class.java)

        val usernameEditText = findViewById<EditText>(R.id.edittext_username)
        val passwordEditText = findViewById<EditText>(R.id.edittext_password)
        val firstnameEditText = findViewById<EditText>(R.id.edittext_firstname)
        val middlenameEditText = findViewById<EditText>(R.id.edittext_middle)
        val lastnameEditText = findViewById<EditText>(R.id.edittext_lastname)
        val studentIdEditText = findViewById<EditText>(R.id.edittext_student_id)
        val courseEditText = findViewById<EditText>(R.id.edittext_course)
        val registerButton = findViewById<Button>(R.id.button_register)

        registerButton.setOnClickListener {
            val username = usernameEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            val firstname = firstnameEditText.text.toString().trim()
            val middlename = middlenameEditText.text.toString().trim()
            val lastname = lastnameEditText.text.toString().trim()
            val studentId = studentIdEditText.text.toString().trim()
            val course = courseEditText.text.toString().trim()

            if (validateFields(username, password, firstname, middlename, lastname, studentId, course)) {
                val request = RegisterRequest(username, password, firstname, middlename, lastname, studentId, course)

                registerService.register(request).enqueue(object : Callback<RegisterResponse> {
                    override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                        if (response.isSuccessful) {
                            val registerResponse = response.body()
                            Toast.makeText(this@RegisterActivity, registerResponse?.message, Toast.LENGTH_LONG).show()
                            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this@RegisterActivity, "Failed to register", Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                        Toast.makeText(this@RegisterActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
                    }
                })
            }
        }
    }

    private fun validateFields(
        username: String,
        password: String,
        firstname: String,
        middlename: String,
        lastname: String,
        studentId: String,
        course: String
    ): Boolean {
        if (username.isEmpty() || username.length < 4) {
            Toast.makeText(this, "Username must be at least 4 characters", Toast.LENGTH_LONG).show()
            return false
        }

        if (password.isEmpty() || password.length < 4) {
            Toast.makeText(this, "Password must be at least 4 characters", Toast.LENGTH_LONG).show()
            return false
        }

        if (firstname.isEmpty() || !firstname.matches(Regex("^[a-zA-Z\\s]+$"))) {
            Toast.makeText(this, "First name must contain only letters", Toast.LENGTH_LONG).show()
            return false
        }

        if (middlename.isEmpty() || !middlename.matches(Regex("^[a-zA-Z\\s]+$"))) {
            Toast.makeText(this, "Middle name must contain only letters", Toast.LENGTH_LONG).show()
            return false
        }

        if (lastname.isEmpty() || !lastname.matches(Regex("^[a-zA-Z\\s]+$"))) {
            Toast.makeText(this, "Last name must contain only letters", Toast.LENGTH_LONG).show()
            return false
        }

        if (studentId.isEmpty() || !studentId.matches(Regex("^\\d+$"))) {
            Toast.makeText(this, "Student ID must be a number", Toast.LENGTH_LONG).show()
            return false
        }

        if (course.isEmpty()) {
            Toast.makeText(this, "Course field must not be empty", Toast.LENGTH_LONG).show()
            return false
        }

        return true
    }
}