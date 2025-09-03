package org.example.app

import android.app.Application
import android.os.Handler
import android.os.Looper
import org.example.app.utils.CrashReporter
import kotlin.system.exitProcess

class MalayalamCalendarApp : Application() {
    private lateinit var crashReporter: CrashReporter

    override fun onCreate() {
        super.onCreate()
        crashReporter = CrashReporter.getInstance(this)

        setupExceptionHandler()
    }

    private fun setupExceptionHandler() {
        val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()

        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            Handler(Looper.getMainLooper()).post {
                val currentActivity = getRunningActivity()
                if (currentActivity != null) {
                    crashReporter.handleException(currentActivity, throwable)
                } else {
                    defaultHandler?.uncaughtException(thread, throwable)
                    exitProcess(1)
                }
            }
        }
    }

    private fun getRunningActivity(): android.app.Activity? {
        try {
            val activityThreadClass = Class.forName("android.app.ActivityThread")
            val activityThread = activityThreadClass.getMethod("currentActivityThread").invoke(null)
            val activitiesField = activityThreadClass.getDeclaredField("mActivities")
            activitiesField.isAccessible = true

            val activities = activitiesField.get(activityThread) as Map<*, *>
            for (activityRecord in activities.values) {
                val activityRecordClass = activityRecord?.javaClass
                val pausedField = activityRecordClass?.getDeclaredField("paused")
                pausedField?.isAccessible = true

                if (pausedField?.getBoolean(activityRecord) == false) {
                    val activityField = activityRecordClass.getDeclaredField("activity")
                    activityField.isAccessible = true
                    return activityField.get(activityRecord) as android.app.Activity
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}
