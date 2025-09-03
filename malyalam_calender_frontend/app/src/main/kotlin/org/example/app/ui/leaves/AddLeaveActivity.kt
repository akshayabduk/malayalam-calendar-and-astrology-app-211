package org.example.app.ui.leaves

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import org.example.app.R
import org.example.app.data.models.Leave
import org.example.app.data.models.LeaveType
import org.example.app.data.repository.CalendarRepository
import org.example.app.data.repository.MockCalendarRepository
import java.text.SimpleDateFormat
import java.util.*

class AddLeaveActivity : AppCompatActivity() {
    private lateinit var titleEdit: TextInputEditText
    private lateinit var dateEdit: TextInputEditText
    private lateinit var typeSpinner: AutoCompleteTextView
    private lateinit var descriptionEdit: TextInputEditText
    private lateinit var saveButton: MaterialButton

    private val repository: CalendarRepository = MockCalendarRepository()
    private val calendar = Calendar.getInstance()
    private val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leave_details)

        initViews()
        setupTypeSpinner()
        setupDatePicker()
        setupSaveButton()
    }

    private fun initViews() {
        titleEdit = findViewById(R.id.titleEdit)
        dateEdit = findViewById(R.id.dateEdit)
        typeSpinner = findViewById(R.id.typeSpinner)
        descriptionEdit = findViewById(R.id.descriptionEdit)
        saveButton = findViewById(R.id.saveButton)

        // Set initial date
        dateEdit.setText(dateFormat.format(calendar.time))
    }

    private fun setupTypeSpinner() {
        val types = arrayOf(
            getString(R.string.leave_type_personal),
            getString(R.string.leave_type_sick),
            getString(R.string.leave_type_casual)
        )
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, types)
        typeSpinner.setAdapter(adapter)
    }

    private fun setupDatePicker() {
        dateEdit.setOnClickListener {
            DatePickerDialog(
                this,
                { _, year, month, day ->
                    calendar.set(year, month, day)
                    dateEdit.setText(dateFormat.format(calendar.time))
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun setupSaveButton() {
        saveButton.setOnClickListener {
            val title = titleEdit.text.toString()
            val type = when (typeSpinner.text.toString()) {
                getString(R.string.leave_type_personal) -> LeaveType.PERSONAL
                getString(R.string.leave_type_sick) -> LeaveType.SICK
                else -> LeaveType.CASUAL
            }
            val description = descriptionEdit.text.toString()

            if (title.isBlank()) {
                titleEdit.error = "Title is required"
                return@setOnClickListener
            }

            val leave = Leave(
                id = UUID.randomUUID().toString(),
                title = title,
                date = calendar.time,
                type = type,
                description = description.takeIf { it.isNotBlank() }
            )

            repository.addLeave(leave)
            Toast.makeText(this, "Leave added successfully", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
