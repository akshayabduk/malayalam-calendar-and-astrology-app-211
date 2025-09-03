package org.example.app.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.example.app.R
import org.example.app.data.models.Leave
import java.text.SimpleDateFormat
import java.util.*

/**
 * Adapter for displaying leave items in a list
 */
class LeavesAdapter : RecyclerView.Adapter<LeavesAdapter.LeaveViewHolder>() {
    private var leaves: List<Leave> = emptyList()

    class LeaveViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleText: TextView = view.findViewById(R.id.leaveTitle)
        val dateText: TextView = view.findViewById(R.id.leaveDate)
        val typeText: TextView = view.findViewById(R.id.leaveType)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaveViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_leave, parent, false)
        return LeaveViewHolder(view)
    }

    override fun onBindViewHolder(holder: LeaveViewHolder, position: Int) {
        val leave = leaves[position]
        val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        
        holder.titleText.text = leave.title
        holder.dateText.text = dateFormat.format(leave.date)
        holder.typeText.text = leave.type.name
    }

    override fun getItemCount() = leaves.size

    fun updateLeaves(newLeaves: List<Leave>) {
        leaves = newLeaves
        notifyDataSetChanged()
    }
}
