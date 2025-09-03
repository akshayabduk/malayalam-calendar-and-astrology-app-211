package org.example.app.ui.leaves

import android.app.Activity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import org.example.app.R
import org.example.app.data.models.Leave
import org.example.app.data.models.LeaveType
import org.example.app.leaves.LeaveManager
import java.text.SimpleDateFormat
import java.util.*

class LeaveDetailsActivity : AppCompatActivity() {
    private lateinit var leaveManager: LeaveManager
    private lateinit var titleEdit: EditText
    private lateinit var dateEdit: EditText
    private lateinit var typeSpinner: Spinner
    private lateinit var descriptionEdit: EditText
    
    private var currentLeave: Leave? = null
    private var selectedDate = Date()
    private val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leave_details)

        leaveManager = LeaveManager.getInstance(this)

        // Setup toolbar
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Initialize views
        titleEdit = findViewById(R.id.titleEdit)
        dateEdit = findViewById(R.id.dateEdit)
        typeSpinner = findViewById(R.id.typeSpinner)
        descriptionEdit = findViewById(R.id.descriptionEdit)

        // Setup date picker
        dateEdit.setOnClickListener {
            showDatePicker()
        }

        // Load leave if editing
        intent.getStringExtra("leave_id")?.let { leaveId ->
            loadLeave(leaveId)
        }

        updateDateDisplay()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_leave_details, menu)
        menu.findItem(R.id.action_delete)?.isVisible = currentLeave != null
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            R.id.action_save -> {
                saveLeave()
                true
            }
            R.id.action_delete -> {
                showDeleteConfirmation()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showDatePicker() {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText(getString(R.string.select_date))
            .setSelection(selectedDate.time)
            .build()

        datePicker.addOnPositiveButtonClickListener { selection ->
            selectedDate = Date(selection)
            updateDateDisplay()
        }

        datePicker.show(supportFragmentManager, "datePicker")
    }

    private fun updateDateDisplay() {
        dateEdit.setText(dateFormat.format(selectedDate))
    }

    private fun loadLeave(leaveId: String) {
        lifecycleScope.launch {
            currentLeave = leaveManager.getLeave(leaveId)
            currentLeave?.let { leave ->
                titleEdit.setText(leave.title)
                selectedDate = leave.date
                updateDateDisplay()
                typeSpinner.setSelection(leave.type.ordinal)
                descriptionEdit.setText(leave.description)
            }
        }
    }

    private fun saveLeave() {
        val title = titleEdit.text.toString()
        if (title.isBlank()) {
            titleEdit.error = getString(R.string.error_title_required)
            return
        }

        val type = LeaveType.values()[typeSpinner.selectedItemPosition]
        val description = descriptionEdit.text.toString()

        lifecycleScope.launch {
            try {
                if (currentLeave != null) {
                    leaveManager.updateLeave(
                        currentLeave!!.id,
                        title,
                        selectedDate,
                        type,
                        description
                    )
                } else {
                    leaveManager.createLeave(
                        title,
                        selectedDate,
                        type,
                        description
                    )
                }
                setResult(Activity.RESULT_OK)
                finish()
            } catch (e: Exception) {
                // Show error
            }
        }
    }

    private fun showDeleteConfirmation() {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.delete_leave_title)
            .setMessage(R.string.delete_leave_message)
            .setPositiveButton(R.string.delete) { _, _ ->
                deleteLeave()
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    private fun deleteLeave() {
        currentLeave?.let { leave ->
            lifecycleScope.launch {
                leaveManager.deleteLeave(leave.id)
                setResult(Activity.RESULT_OK)
                finish()
            }
        }
    }
}
