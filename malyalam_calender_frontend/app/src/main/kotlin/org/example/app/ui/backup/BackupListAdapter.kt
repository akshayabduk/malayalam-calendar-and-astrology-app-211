package org.example.app.ui.backup

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.example.app.R
import java.text.SimpleDateFormat
import java.util.*

class BackupListAdapter(
    private val onRestoreClick: (String) -> Unit,
    private val onDeleteClick: (String) -> Unit
) : ListAdapter<String, BackupListAdapter.ViewHolder>(BackupDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_backup, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val nameText: TextView = view.findViewById(R.id.backupName)
        private val dateText: TextView = view.findViewById(R.id.backupDate)
        private val restoreButton: ImageButton = view.findViewById(R.id.restoreButton)
        private val deleteButton: ImageButton = view.findViewById(R.id.deleteButton)

        fun bind(filename: String) {
            // Extract date from filename (format: backup_yyyyMMdd_HHmmss.json)
            val dateStr = filename.substring(7, 22)
            val date = try {
                SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).parse(dateStr)
            } catch (e: Exception) {
                Date()
            }

            nameText.text = filename
            dateText.text = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
                .format(date)

            restoreButton.setOnClickListener { onRestoreClick(filename) }
            deleteButton.setOnClickListener { onDeleteClick(filename) }
        }
    }

    private class BackupDiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String) = oldItem == newItem
        override fun areContentsTheSame(oldItem: String, newItem: String) = oldItem == newItem
    }
}
