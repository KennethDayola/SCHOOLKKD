package com.intprog.schoolkkd

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query



data class ProfileResponse(
    val username: String,
    val firstname: String,
    val middlename: String? = null, // Default to null to prevent errors
    val lastname: String,
    @SerializedName("student_id") val studentId: String,
    val course: String,
    @SerializedName("year_level") val yearLevel: String?, // Added missing fields
    val classification: String?,
    val college: String?,
    val mobile: String?,
    val birthdate: String?,
    val gender: String?
)

interface ProfileService {
    @GET("/userinfo")
    fun getUserInfo(
        @Query("username") username: String,
        @Query("password") password: String
    ): Call<ProfileResponse>
}
