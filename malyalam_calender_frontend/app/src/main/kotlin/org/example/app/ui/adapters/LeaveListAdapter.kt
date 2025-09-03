package org.example.app.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.example.app.R
import org.example.app.data.models.Leave
import java.text.SimpleDateFormat
import java.util.*

class LeaveListAdapter(
    private val onLeaveClick: (Leave) -> Unit
) : ListAdapter<Leave, LeaveListAdapter.LeaveViewHolder>(LeaveDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaveViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_leave, parent, false)
        return LeaveViewHolder(view, onLeaveClick)
    }

    override fun onBindViewHolder(holder: LeaveViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class LeaveViewHolder(
        itemView: View,
        private val onLeaveClick: (Leave) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val titleText: TextView = itemView.findViewById(R.id.leaveTitle)
        private val dateText: TextView = itemView.findViewById(R.id.leaveDate)
        private val typeText: TextView = itemView.findViewById(R.id.leaveType)
        private val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

        fun bind(leave: Leave) {
            titleText.text = leave.title
            dateText.text = dateFormat.format(leave.date)
            typeText.text = leave.type.name

            itemView.setOnClickListener { onLeaveClick(leave) }
        }
    }
}

private class LeaveDiffCallback : DiffUtil.ItemCallback<Leave>() {
    override fun areItemsTheSame(oldItem: Leave, newItem: Leave): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Leave, newItem: Leave): Boolean {
        return oldItem == newItem
    }
}
