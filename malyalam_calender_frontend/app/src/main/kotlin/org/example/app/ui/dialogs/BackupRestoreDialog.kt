package org.example.app.ui.dialogs

import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import org.example.app.MalayalamCalendarApp
import org.example.app.R
import org.example.app.backup.BackupManager
import org.example.app.security.BiometricAuthManager

class BackupRestoreDialog : DialogFragment() {
    private lateinit var biometricAuthManager: BiometricAuthManager
    private lateinit var backupManager: BackupManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        biometricAuthManager = BiometricAuthManager(requireContext())
        backupManager = BackupManager(requireContext())
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.restore_started)
            .setMessage(R.string.auth_description)
            .setPositiveButton(R.string.restore_started) { _, _ ->
                if (biometricAuthManager.canAuthenticate()) {
                    authenticateAndRestore()
                } else {
                    Toast.makeText(
                        context,
                        R.string.auth_error_no_biometric,
                        Toast.LENGTH_LONG
                    ).show()
                    dismiss()
                }
            }
            .setNegativeButton(R.string.cancel) { _, _ -> dismiss() }
            .create()
    }

    private fun authenticateAndRestore() {
        biometricAuthManager.showBiometricPrompt(
            requireActivity(),
            onSuccess = {
                lifecycleScope.launch {
                    try {
                        val success = backupManager.restoreBackup()
                        val message = if (success) {
                            R.string.restore_success
                        } else {
                            R.string.restore_failed
                        }
                        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                    } catch (e: Exception) {
                        Toast.makeText(
                            context,
                            R.string.restore_failed,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    dismiss()
                }
            },
            onError = { error ->
                Toast.makeText(context, error, Toast.LENGTH_LONG).show()
                dismiss()
            },
            onFailed = {
                Toast.makeText(context, R.string.auth_error, Toast.LENGTH_LONG).show()
                dismiss()
            }
        )
    }

    companion object {
        const val TAG = "BackupRestoreDialog"
    }
}
