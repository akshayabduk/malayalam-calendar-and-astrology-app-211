package org.example.app.data.db

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import org.example.app.data.models.EventType
import org.example.app.data.models.LeaveType

object DatabaseMigration {
    // Migration from version 1 to 2
    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // Add lastModified column to events table
            database.execSQL(
                "ALTER TABLE events ADD COLUMN lastModified INTEGER NOT NULL DEFAULT 0"
            )
            
            // Add lastModified column to leaves table
            database.execSQL(
                "ALTER TABLE leaves ADD COLUMN lastModified INTEGER NOT NULL DEFAULT 0"
            )
        }
    }

    // Migration from version 2 to 3
    val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // Create new events table with updated schema
            database.execSQL(
                """
                CREATE TABLE IF NOT EXISTS events_new (
                    id TEXT PRIMARY KEY NOT NULL,
                    date INTEGER NOT NULL,
                    title TEXT NOT NULL,
                    type TEXT NOT NULL,
                    description TEXT,
                    lastModified INTEGER NOT NULL,
                    isSynced INTEGER NOT NULL DEFAULT 0
                )
                """
            )

            // Copy data from old table to new table
            database.execSQL(
                """
                INSERT INTO events_new (id, date, title, type, description, lastModified)
                SELECT id, date, title, type, description, lastModified
                FROM events
                """
            )

            // Drop old table
            database.execSQL("DROP TABLE events")

            // Rename new table to events
            database.execSQL("ALTER TABLE events_new RENAME TO events")

            // Create index for better query performance
            database.execSQL(
                "CREATE INDEX IF NOT EXISTS index_events_date ON events(date)"
            )
        }
    }
}
