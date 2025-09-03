package org.example.app.ui.leaves

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import org.example.app.R
import org.example.app.data.models.LeaveType

class LeaveTypeAdapter(context: Context) : ArrayAdapter<LeaveType>(
    context,
    android.R.layout.simple_spinner_item,
    LeaveType.values()
) {
    init {
        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(android.R.layout.simple_spinner_item, parent, false)

        getItem(position)?.let { type ->
            (view as TextView).text = getDisplayName(type)
        }

        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(android.R.layout.simple_spinner_dropdown_item, parent, false)

        getItem(position)?.let { type ->
            (view as TextView).text = getDisplayName(type)
        }

        return view
    }

    private fun getDisplayName(type: LeaveType): String {
        return when (type) {
            LeaveType.PERSONAL -> context.getString(R.string.leave_type_personal)
            LeaveType.SICK -> context.getString(R.string.leave_type_sick)
            LeaveType.CASUAL -> context.getString(R.string.leave_type_casual)
        }
    }
}
