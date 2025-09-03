package org.example.app.ui.leaves

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.example.app.R
import org.example.app.data.models.Leave
import org.example.app.data.models.LeaveType
import java.text.SimpleDateFormat
import java.util.*

/**
 * Adapter for displaying leave items in a RecyclerView
 */
class LeaveListAdapter(
    private val onItemClick: (Leave) -> Unit
) : ListAdapter<Leave, LeaveListAdapter.LeaveViewHolder>(LeaveDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaveViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_leave, parent, false)
        return LeaveViewHolder(view, onItemClick)
    }

    override fun onBindViewHolder(holder: LeaveViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class LeaveViewHolder(
        itemView: View,
        private val onItemClick: (Leave) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val titleText: TextView = itemView.findViewById(R.id.titleText)
        private val dateText: TextView = itemView.findViewById(R.id.dateText)
        private val typeIndicator: View = itemView.findViewById(R.id.typeIndicator)
        private val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

        fun bind(leave: Leave) {
            titleText.text = leave.title
            dateText.text = dateFormat.format(leave.date)

            val colorRes = when (leave.type) {
                LeaveType.PERSONAL -> R.color.leave_personal
                LeaveType.SICK -> R.color.leave_sick
                LeaveType.CASUAL -> R.color.leave_casual
            }
            typeIndicator.setBackgroundColor(
                ContextCompat.getColor(itemView.context, colorRes)
            )

            itemView.setOnClickListener { onItemClick(leave) }
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
}
