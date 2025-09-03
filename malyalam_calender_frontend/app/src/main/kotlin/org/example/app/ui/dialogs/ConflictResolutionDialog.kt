package org.example.app.ui.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.example.app.R
import org.example.app.analytics.AnalyticsManager

class ConflictResolutionDialog : DialogFragment() {
    private var onResolveCallback: ((Resolution) -> Unit)? = null
    private lateinit var analytics: AnalyticsManager

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        analytics = AnalyticsManager.getInstance(requireContext())

        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.sync_conflict_title)
            .setMessage(R.string.sync_conflict_message)
            .setPositiveButton(R.string.sync_keep_local) { _, _ ->
                analytics.logEvent("conflict_resolved_local")
                onResolveCallback?.invoke(Resolution.KEEP_LOCAL)
            }
            .setNegativeButton(R.string.sync_keep_remote) { _, _ ->
                analytics.logEvent("conflict_resolved_remote")
                onResolveCallback?.invoke(Resolution.KEEP_REMOTE)
            }
            .setNeutralButton(R.string.sync_merge) { _, _ ->
                analytics.logEvent("conflict_resolved_merge")
                onResolveCallback?.invoke(Resolution.MERGE)
            }
            .create()
    }

    fun setOnResolveListener(callback: (Resolution) -> Unit) {
        onResolveCallback = callback
    }

    enum class Resolution {
        KEEP_LOCAL,
        KEEP_REMOTE,
        MERGE
    }

    companion object {
        const val TAG = "ConflictResolutionDialog"
    }
}
