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

class LoginActivity : Activity() {

    private lateinit var loginService: LoginService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginTextView = findViewById<TextView>(R.id.textview_register)
        loginTextView.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        val retrofit = createRetrofit()
        loginService = retrofit.create(LoginService::class.java)

        val edittextUsername = findViewById<EditText>(R.id.edittext_username)
        val edittextPassword = findViewById<EditText>(R.id.edittext_password)

        val buttonLogin = findViewById<Button>(R.id.button_login)

        buttonLogin.setOnClickListener {

            val username = edittextUsername.text.toString().trim()
            val password = edittextPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Username and Password are required", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (username.length < 4) {
                Toast.makeText(this, "Username must be more than 4 characters", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (password.length < 4) {
                Toast.makeText(this, "Password must be more than 4 characters", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val loginRequest = LoginRequest(username, password)
            loginService.login(loginRequest).enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.isSuccessful) {
                        val loginResponse = response.body()
                        if (loginResponse != null) {
                            if (loginResponse.message == "Login successful") {
                                getSharedPreferences("userPrefs", MODE_PRIVATE).edit().apply {
                                    putString("username", username)
                                    putString("password", password)
                                    apply()
                                }
                                val intent = Intent(applicationContext, LandingActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(this@LoginActivity, loginResponse.message, Toast.LENGTH_LONG).show()
                            }
                        }
                    } else {
                        Toast.makeText(this@LoginActivity, "Login failed: ${response.code()}", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Toast.makeText(this@LoginActivity, "Network error: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
        }
    }
}