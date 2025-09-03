package org.example.app.data.db

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import java.io.IOException

class MigrationTest {
    private val TEST_DB = "migration-test"

    @get:Rule
    val helper: MigrationTestHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        AppDatabase::class.java.canonicalName,
        FrameworkSQLiteOpenHelperFactory()
    )

    @Test
    @Throws(IOException::class)
    fun migrate1To2() {
        // Create version 1 of the database
        helper.createDatabase(TEST_DB, 1).apply {
            // Insert sample data
            execSQL("""
                INSERT INTO calendar_events (id, date, title, type, description)
                VALUES ('1', 1234567890, 'Test Event', 'LEAVE', 'Description')
            """)
            close()
        }

        // Migrate to version 2
        val dbV2 = helper.runMigrationsAndValidate(TEST_DB, 2, true, Migrations.MIGRATION_1_2)

        // Verify the data
        val cursor = dbV2.query("SELECT * FROM calendar_events")
        assertThat(cursor.columnNames).asList().contains("lastModified")
        cursor.moveToFirst()
        assertThat(cursor.getLong(cursor.getColumnIndex("lastModified"))).isEqualTo(0)
    }

    @Test
    @Throws(IOException::class)
    fun migrate2To3() {
        // Create version 2 of the database with sample data
        helper.createDatabase(TEST_DB, 2).apply {
            execSQL("""
                INSERT INTO leaves (id, date, title, type, description, lastModified)
                VALUES ('1', 1234567890, 'Test Leave', 'SICK', 'Description', 1234567890)
            """)
            close()
        }

        // Migrate to version 3
        val dbV3 = helper.runMigrationsAndValidate(TEST_DB, 3, true, Migrations.MIGRATION_2_3)

        // Verify the data
        val cursor = dbV3.query("SELECT * FROM leaves")
        assertThat(cursor.columnNames).asList().containsAtLeast("isSynced", "syncError")
        cursor.moveToFirst()
        assertThat(cursor.getInt(cursor.getColumnIndex("isSynced"))).isEqualTo(0)
        assertThat(cursor.getString(cursor.getColumnIndex("syncError"))).isNull()
    }

    @Test
    @Throws(IOException::class)
    fun migrate3To4() {
        // Create version 3 of the database
        helper.createDatabase(TEST_DB, 3).apply {
            close()
        }

        // Migrate to version 4
        val dbV4 = helper.runMigrationsAndValidate(TEST_DB, 4, true, Migrations.MIGRATION_3_4)

        // Verify notification preferences
        val cursor = dbV4.query("SELECT * FROM notification_preferences")
        assertThat(cursor.count).isEqualTo(3) // Three default entries
        
        cursor.moveToFirst()
        assertThat(cursor.getString(cursor.getColumnIndex("eventType"))).isEqualTo("LEAVE")
        assertThat(cursor.getInt(cursor.getColumnIndex("reminderMinutes"))).isEqualTo(60)
    }
}
