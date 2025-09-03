package org.example.app.data.db

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object Migrations {
    // Migration from version 1 to 2: Add lastModified field to events and leaves
    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // Add lastModified column to calendar_events
            database.execSQL(
                "ALTER TABLE calendar_events ADD COLUMN lastModified INTEGER NOT NULL DEFAULT 0"
            )
            
            // Add lastModified column to leaves
            database.execSQL(
                "ALTER TABLE leaves ADD COLUMN lastModified INTEGER NOT NULL DEFAULT 0"
            )
        }
    }

    // Migration from version 2 to 3: Add sync status fields
    val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // Add sync fields to calendar_events
            database.execSQL(
                "ALTER TABLE calendar_events ADD COLUMN isSynced INTEGER NOT NULL DEFAULT 0"
            )
            database.execSQL(
                "ALTER TABLE calendar_events ADD COLUMN syncError TEXT"
            )
            
            // Add sync fields to leaves
            database.execSQL(
                "ALTER TABLE leaves ADD COLUMN isSynced INTEGER NOT NULL DEFAULT 0"
            )
            database.execSQL(
                "ALTER TABLE leaves ADD COLUMN syncError TEXT"
            )
        }
    }

    // Migration from version 3 to 4: Add notification preferences
    val MIGRATION_3_4 = object : Migration(3, 4) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // Create notification preferences table
            database.execSQL("""
                CREATE TABLE IF NOT EXISTS notification_preferences (
                    eventType TEXT NOT NULL PRIMARY KEY,
                    isEnabled INTEGER NOT NULL DEFAULT 1,
                    reminderMinutes INTEGER NOT NULL DEFAULT 60,
                    soundEnabled INTEGER NOT NULL DEFAULT 1,
                    vibrationEnabled INTEGER NOT NULL DEFAULT 1
                )
            """)
            
            // Insert default preferences
            database.execSQL("""
                INSERT INTO notification_preferences (eventType, isEnabled, reminderMinutes)
                VALUES 
                    ('LEAVE', 1, 60),
                    ('HOLIDAY', 1, 1440),
                    ('ASTROLOGY', 1, 0)
            """)
        }
    }
}
