package org.example.app.ui.dialogs

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch
import org.example.app.MalayalamCalendarApp
import org.example.app.R
import org.example.app.data.models.Leave
import org.example.app.data.models.LeaveType
import java.text.SimpleDateFormat
import java.util.*

class AddLeaveDialog : DialogFragment() {
    private lateinit var titleInput: TextInputEditText
    private lateinit var dateInput: TextInputEditText
    private lateinit var typeInput: AutoCompleteTextView
    private val calendar = Calendar.getInstance()
    private val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    private val repository by lazy { 
        (requireActivity().application as MalayalamCalendarApp).repository 
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = requireActivity().layoutInflater.inflate(R.layout.dialog_add_leave, null)

        titleInput = view.findViewById(R.id.titleInput)
        dateInput = view.findViewById(R.id.dateInput)
        typeInput = view.findViewById(R.id.typeInput)

        setupDatePicker()
        setupTypeDropdown()

        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.add_leave)
            .setView(view)
            .setPositiveButton(R.string.save) { _, _ ->
                saveLeave()
            }
            .setNegativeButton(R.string.cancel, null)
            .create()
    }

    private fun setupDatePicker() {
        dateInput.setText(dateFormat.format(calendar.time))
        dateInput.setOnClickListener {
            DatePickerDialog(
                requireContext(),
                { _, year, month, day ->
                    calendar.set(year, month, day)
                    dateInput.setText(dateFormat.format(calendar.time))
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun setupTypeDropdown() {
        val types = LeaveType.values().map { it.name }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, types)
        typeInput.setAdapter(adapter)
        typeInput.setText(types[0], false)
    }

    private fun saveLeave() {
        val title = titleInput.text.toString()
        val type = LeaveType.valueOf(typeInput.text.toString())
        
        val leave = Leave(
            id = UUID.randomUUID().toString(),
            date = calendar.time,
            title = title,
            type = type
        )
        
        lifecycleScope.launch {
            try {
                repository.addLeave(leave)
                // Optional: Show success message
            } catch (e: Exception) {
                // Optional: Show error message
            }
        }
    }
}
