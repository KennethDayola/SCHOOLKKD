package com.intprog.schoolkkd


import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

data class Schedule(
    val EDP_Code: String,
    val Start_Time: String,
    val End_Time: String,
    val Days: String,
    val Room: String,
    val Units: Int,
    val Actions: String
)

data class SubjectWithSchedules(
    val Subject_Code: String,
    val Subject_Name: String,
    val Description: String,
    val Schedules: List<Schedule>
)
data class EnrollmentRequest(
    val enrollments: List<EnrollmentDetail>
)

data class EnrollmentDetail(
    val StudId: Int,
    val SubjectCode: String,
    val EDPCode: String,
    val Status: String = "Pending"
)

interface EnrollmentService {
    @GET("api/subjects-with-schedules")
    fun getSubjectsWithSchedules(): Call<List<SubjectWithSchedules>>

    @POST("/enroll")
    fun submitEnrollment(@Body body: EnrollmentRequest): Call<Void>
}