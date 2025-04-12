package com.intprog.schoolkkd

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EnrollmentActivity : Activity() {

    private lateinit var enrollmentService: EnrollmentService
    private lateinit var enrollmentTable: TableLayout
    private lateinit var prospectusTable: TableLayout
    private val enrollmentDetails = mutableListOf<EnrollmentDetail>()
    private lateinit var prospectusRecyclerView: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enrollment)

        val retrofit = createRetrofit()
        enrollmentService = retrofit.create(EnrollmentService::class.java)

        prospectusRecyclerView = findViewById(R.id.prospectusRecyclerView)
        prospectusRecyclerView.layoutManager = LinearLayoutManager(this)

        enrollmentTable = findViewById(R.id.enrollmentTable)
       // prospectusTable = findViewById(R.id.prospectusTable) // Add this ID in your XML

        val saveEnrollmentButton = findViewById<Button>(R.id.saveEnrollmentButton)
        saveEnrollmentButton.setOnClickListener {
            submitEnrollment()
        }

        loadProspectusSubjects()
    }

    private fun loadProspectusSubjects() {
        enrollmentService.getSubjectsWithSchedules().enqueue(object : Callback<List<SubjectWithSchedules>> {
            override fun onResponse(call: Call<List<SubjectWithSchedules>>, response: Response<List<SubjectWithSchedules>>) {
                if (response.isSuccessful) {
                    val subjects = response.body() ?: emptyList()
                    val adapter = ProspectusAdapter(subjects) { subject ->
                        showScheduleDialog(subject)
                    }
                    prospectusRecyclerView.adapter = adapter
                } else {
                    Toast.makeText(this@EnrollmentActivity, "Failed to load subjects", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<SubjectWithSchedules>>, t: Throwable) {
                Toast.makeText(this@EnrollmentActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showScheduleDialog(subject: SubjectWithSchedules) {
        val dialogView = layoutInflater.inflate(R.layout.schedule_dialog, null)
        val container = dialogView.findViewById<LinearLayout>(R.id.scheduleListContainer)
        val title = dialogView.findViewById<TextView>(R.id.dialogTitle)
        title.text = "Schedules for ${subject.Subject_Code}"

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setNegativeButton("Cancel", null)
            .create()

        subject.Schedules.forEach { schedule ->
            val scheduleView = TextView(this@EnrollmentActivity).apply {
                text = "${schedule.EDP_Code} | ${schedule.Start_Time} - ${schedule.End_Time} | ${schedule.Days} | ${schedule.Room}"
                setPadding(16, 16, 16, 16)
                setOnClickListener {
                    addScheduleToTable(schedule, subject.Subject_Code)
                    dialog.dismiss()
                }
            }
            container.addView(scheduleView)
        }

        dialog.show()
    }

    private fun addScheduleToTable(schedule: Schedule, subjectCode: String) {
        // Avoid duplicates
        if (enrollmentDetails.any { it.EDPCode == schedule.EDP_Code }) {
            Toast.makeText(this, "Already added", Toast.LENGTH_SHORT).show()
            return
        }

        val tableRow = TableRow(this)

        tableRow.addView(makeTextView(schedule.EDP_Code))
        tableRow.addView(makeTextView(subjectCode))
        tableRow.addView(makeTextView(schedule.Start_Time))
        tableRow.addView(makeTextView(schedule.End_Time))
        tableRow.addView(makeTextView(schedule.Days))
        tableRow.addView(makeTextView(schedule.Room))
        tableRow.addView(makeTextView(schedule.Units.toString()))

        val removeButton = Button(this).apply {
            text = "Remove"
            setOnClickListener {
                enrollmentTable.removeView(tableRow)
                enrollmentDetails.removeIf { it.EDPCode == schedule.EDP_Code }
            }
        }
        tableRow.addView(removeButton)

        enrollmentTable.addView(tableRow)

        enrollmentDetails.add(
            EnrollmentDetail(
                StudId = 10010,
                SubjectCode = subjectCode,
                EDPCode = schedule.EDP_Code
            )
        )
    }

    private fun makeTextView(text: String): TextView {
        return TextView(this).apply {
            this.text = text
            setPadding(8, 8, 8, 8)
            gravity = android.view.Gravity.CENTER
        }
    }

    private fun submitEnrollment() {
        if (enrollmentDetails.isEmpty()) {
            Toast.makeText(this, "No subjects to enroll", Toast.LENGTH_SHORT).show()
            return
        }

        val request = EnrollmentRequest(enrollments = enrollmentDetails)

        enrollmentService.submitEnrollment(request).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@EnrollmentActivity, "Enrollment successful", Toast.LENGTH_SHORT).show()
                    enrollmentDetails.clear()
                    enrollmentTable.removeViews(1, enrollmentTable.childCount - 1)
                } else {
                    // Log the error response
                    val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                    val statusCode = response.code()

                    // Log more details to Logcat
                    Log.e("EnrollmentActivity", "Enrollment failed. Status code: $statusCode, Error: $errorMessage")

                    // Show the error message to the user
                    Toast.makeText(this@EnrollmentActivity, "Enrollment failed: $errorMessage", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                // Log the error message and stack trace
                Log.e("EnrollmentActivity", "Error occurred during enrollment: ${t.message}", t)

                // Show the error message to the user
                Toast.makeText(this@EnrollmentActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
