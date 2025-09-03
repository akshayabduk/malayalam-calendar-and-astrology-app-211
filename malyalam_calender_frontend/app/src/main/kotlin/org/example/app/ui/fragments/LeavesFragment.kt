package org.example.app.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.example.app.R
import org.example.app.analytics.AnalyticsManager
import org.example.app.data.repository.CalendarRepository
import org.example.app.data.repository.MockCalendarRepository
import org.example.app.ui.dialogs.AddLeaveDialog
import org.example.app.utils.ErrorHandler

class LeavesFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var addButton: FloatingActionButton
    private lateinit var analytics: AnalyticsManager
    private val repository: CalendarRepository = MockCalendarRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        analytics = AnalyticsManager.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_leaves, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.leavesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        addButton = view.findViewById(R.id.addLeaveButton)
        addButton.setOnClickListener {
            showAddLeaveDialog()
            analytics.logEvent(getString(R.string.analytics_event_add_leave))
        }

        try {
            updateLeavesList()
            analytics.logEvent(getString(R.string.analytics_event_view_leaves))
        } catch (e: Exception) {
            ErrorHandler.handleError(
                requireContext(),
                view,
                e
            ) { updateLeavesList() }
        }
    }

    private fun showAddLeaveDialog() {
        AddLeaveDialog().show(childFragmentManager, "addLeave")
    }

    private fun updateLeavesList() {
        // Add leaves list implementation
    }
}
