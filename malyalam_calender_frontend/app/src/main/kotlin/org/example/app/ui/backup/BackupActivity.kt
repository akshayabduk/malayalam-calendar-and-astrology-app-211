package org.example.app.ui.backup

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.example.app.R
import java.text.SimpleDateFormat
import java.util.*

class BackupActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyView: View
    private lateinit var backupButton: FloatingActionButton
    private lateinit var adapter: BackupAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_backup)

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        
        recyclerView = findViewById(R.id.backupList)
        emptyView = findViewById(R.id.emptyView)
        backupButton = findViewById(R.id.backupButton)

        setupRecyclerView()
        setupBackupButton()
        loadBackups()
    }

    private fun setupRecyclerView() {
        adapter = BackupAdapter(
            onRestore = { backup -> restoreBackup(backup) },
            onDelete = { backup -> deleteBackup(backup) }
        )
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun setupBackupButton() {
        backupButton.setOnClickListener {
            createBackup()
        }
    }

    private fun loadBackups() {
        // TODO: Load actual backups from storage
        val backups = listOf<BackupInfo>() // Empty for now
        updateBackupList(backups)
    }

    private fun updateBackupList(backups: List<BackupInfo>) {
        adapter.submitList(backups)
        emptyView.visibility = if (backups.isEmpty()) View.VISIBLE else View.GONE
        recyclerView.visibility = if (backups.isEmpty()) View.GONE else View.VISIBLE
    }

    private fun createBackup() {
        // TODO: Implement backup creation
    }

    private fun restoreBackup(backup: BackupInfo) {
        // TODO: Implement backup restoration
    }

    private fun deleteBackup(backup: BackupInfo) {
        // TODO: Implement backup deletion
    }
}

data class BackupInfo(
    val id: String,
    val timestamp: Date,
    val size: Long
)

class BackupAdapter(
    private val onRestore: (BackupInfo) -> Unit,
    private val onDelete: (BackupInfo) -> Unit
) : RecyclerView.Adapter<BackupAdapter.ViewHolder>() {
    private var backups = listOf<BackupInfo>()
    private val dateFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())

    fun submitList(list: List<BackupInfo>) {
        backups = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): ViewHolder {
        val view = android.view.LayoutInflater.from(parent.context)
            .inflate(R.layout.item_backup, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(backups[position])
    }

    override fun getItemCount() = backups.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val dateText: android.widget.TextView = view.findViewById(R.id.dateText)
        private val sizeText: android.widget.TextView = view.findViewById(R.id.sizeText)
        private val restoreButton: android.widget.Button = view.findViewById(R.id.restoreButton)
        private val deleteButton: android.widget.Button = view.findViewById(R.id.deleteButton)

        fun bind(backup: BackupInfo) {
            dateText.text = dateFormat.format(backup.timestamp)
            sizeText.text = formatSize(backup.size)
            
            restoreButton.setOnClickListener { onRestore(backup) }
            deleteButton.setOnClickListener { onDelete(backup) }
        }

        private fun formatSize(bytes: Long): String {
            return when {
                bytes < 1024 -> "$bytes B"
                bytes < 1024 * 1024 -> "${bytes / 1024} KB"
                else -> "${bytes / (1024 * 1024)} MB"
            }
        }
    }
}
