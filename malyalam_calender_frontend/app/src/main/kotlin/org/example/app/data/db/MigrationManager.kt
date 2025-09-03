package org.example.app.data.db

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class MigrationManager {
    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Add lastModified column to calendar_events
                database.execSQL(
                    "ALTER TABLE calendar_events ADD COLUMN lastModified INTEGER NOT NULL DEFAULT 0"
                )
            }
        }

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Migrate to new event type format
                database.execSQL(
                    "UPDATE calendar_events SET type = 'LEAVE' WHERE type = '0'"
                )
                database.execSQL(
                    "UPDATE calendar_events SET type = 'HOLIDAY' WHERE type = '1'"
                )
            }
        }

        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Add lastModified column to leaves table
                database.execSQL(
                    "ALTER TABLE leaves ADD COLUMN lastModified INTEGER NOT NULL DEFAULT 0"
                )
            }
        }
    }
}
