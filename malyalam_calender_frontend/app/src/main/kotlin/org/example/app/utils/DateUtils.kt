package org.example.app.utils

import java.util.Date

object DateUtils {
    fun Date.toTimestamp(): Long = this.time
    
    fun Long.toDate(): Date = Date(this)
    
    fun now(): Date = Date()
}
