package com.intprog.schoolkkd

import android.os.Bundle
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class StudyLoadActivity : ComponentActivity() {

    private lateinit var studyLoadTable: TableLayout
    private lateinit var studyLoadService: StudyLoadApiService

    private val studentId = "10010  " // Replace with actual student ID or retrieve from storage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_study_load)

        studyLoadTable = findViewById(R.id.study_load_table)

        initRetrofit()
        fetchStudyLoadData()
    }

    private fun initRetrofit() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://astonishing-unruly-tomato.glitch.me")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        studyLoadService = retrofit.create(StudyLoadApiService::class.java)
    }

    private fun fetchStudyLoadData() {
        studyLoadService.getStudyLoad(studentId).enqueue(object : Callback<StudyLoadResponse> {
            override fun onResponse(call: Call<StudyLoadResponse>, response: Response<StudyLoadResponse>) {
                if (response.isSuccessful) {
                    val studyLoadResponse = response.body()
                    studyLoadResponse?.let {
                        displayStudyLoadData(it.studyLoad)
                    } ?: run {
                        Toast.makeText(this@StudyLoadActivity, "No study load data found", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(this@StudyLoadActivity, "Failed to fetch study load data", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<StudyLoadResponse>, t: Throwable) {
                Toast.makeText(this@StudyLoadActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun displayStudyLoadData(studyLoadList: List<Subject>) {
        studyLoadList.forEach { subject ->
            val row = TableRow(this)

            val edpCode = TextView(this).apply {
                text = subject.EDPCode
                setPadding(8, 8, 8, 8)
            }
            val subjectCode = TextView(this).apply {
                text = "${subject.SubjectCode} - ${subject.SubjectName}"
                setPadding(8, 8, 8, 8)
            }
            val startTime = TextView(this).apply {
                text = subject.StartTime
                setPadding(8, 8, 8, 8)
            }
            val endTime = TextView(this).apply {
                text = subject.EndTime
                setPadding(8, 8, 8, 8)
            }
            val days = TextView(this).apply {
                text = subject.Days
                setPadding(8, 8, 8, 8)
            }
            val room = TextView(this).apply {
                text = subject.Room
                setPadding(8, 8, 8, 8)
            }
            val units = TextView(this).apply {
                text = subject.Units.toString()
                setPadding(8, 8, 8, 8)
            }

            row.apply {
                addView(edpCode)
                addView(subjectCode)
                addView(startTime)
                addView(endTime)
                addView(days)
                addView(room)
                addView(units)
            }

            studyLoadTable.addView(row)
        }
    }
}
