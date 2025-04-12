package com.intprog.schoolkkd

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProspectusAdapter(
    private val subjects: List<SubjectWithSchedules>,
    private val onAddClicked: (SubjectWithSchedules) -> Unit
) : RecyclerView.Adapter<ProspectusAdapter.ProspectusViewHolder>() {

    class ProspectusViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val code: TextView = view.findViewById(R.id.tvSubjectCode)
        val description: TextView = view.findViewById(R.id.tvDescription)
        val units: TextView = view.findViewById(R.id.tvUnits)
        val addButton: Button = view.findViewById(R.id.btnAdd)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProspectusViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_prospectus, parent, false)
        return ProspectusViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProspectusViewHolder, position: Int) {
        val subject = subjects[position]
        holder.code.text = subject.Subject_Code
        holder.description.text = subject.Description
        holder.units.text = subject.Schedules.firstOrNull()?.Units?.toString() ?: "0"
        holder.addButton.setOnClickListener {
            onAddClicked(subject)
        }
    }

    override fun getItemCount(): Int = subjects.size
}