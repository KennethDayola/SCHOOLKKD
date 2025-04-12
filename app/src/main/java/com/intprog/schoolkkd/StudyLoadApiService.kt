package com.intprog.schoolkkd

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

data class Subject(
    @SerializedName("EDPCode") val EDPCode: String?,
    @SerializedName("Subject_Code") val SubjectCode: String?,
    @SerializedName("Subject_Name") val SubjectName: String?,
    @SerializedName("Start_Time") val StartTime: String?,
    @SerializedName("End_Time") val EndTime: String?,
    @SerializedName("Days") val Days: String?,
    @SerializedName("Room") val Room: String?,
    @SerializedName("Units") val Units: Int?
)


data class StudyLoadResponse(
    val studentId: String,
    val studyLoad: List<Subject>
)

interface StudyLoadApiService {
    @GET("study-load/{studentId}")
    fun getStudyLoad(@Path("studentId") studentId: String): Call<StudyLoadResponse>
}