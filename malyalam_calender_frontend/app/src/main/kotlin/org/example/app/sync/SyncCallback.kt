package org.example.app.sync

interface SyncCallback {
    fun onSyncStarted()
    fun onSyncProgress(message: String)
    fun onSyncComplete()
    fun onSyncError(error: String)
    fun onSyncConflict(
        localVersion: String,
        remoteVersion: String,
        resolution: (String) -> Unit
    )

    companion object {
        val DEFAULT = object : SyncCallback {
            override fun onSyncStarted() {}
            override fun onSyncProgress(message: String) {}
            override fun onSyncComplete() {}
            override fun onSyncError(error: String) {}
            override fun onSyncConflict(
                localVersion: String,
                remoteVersion: String,
                resolution: (String) -> Unit
            ) {
                // Default to keeping remote version
                resolution(remoteVersion)
            }
        }
    }
}
