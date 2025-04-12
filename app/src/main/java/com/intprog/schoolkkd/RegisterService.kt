package com.intprog.schoolkkd

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

data class RegisterRequest(
    val username: String,
    val password: String,
    val firstname: String,
    val middlename: String,
    val lastname: String,
    val student_id: String,  // Update this to match the backend

    val course: String
)


data class RegisterResponse(
    val message: String
)

interface RegisterService {
    @POST("/add-user")
    fun register(@Body request: RegisterRequest): Call<RegisterResponse>
}