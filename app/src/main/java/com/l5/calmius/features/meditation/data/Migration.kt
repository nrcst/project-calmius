package com.l5.calmius.features.meditation.data

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_3 = object : Migration(1, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Create new table with all columns
        database.execSQL("""
            CREATE TABLE meditation_tracks_new (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                title TEXT NOT NULL,
                description TEXT NOT NULL,
                duration INTEGER NOT NULL,
                meditationType TEXT NOT NULL,
                resourceId INTEGER NOT NULL DEFAULT 0
            )
        """.trimIndent())

        // Copy data from old table
        database.execSQL("""
            INSERT INTO meditation_tracks_new (id, title, description, duration, meditationType, resourceId)
            SELECT id, title, description, duration, meditationType, 0
            FROM meditation_tracks
        """.trimIndent())

        // Remove old table
        database.execSQL("DROP TABLE meditation_tracks")

        // Rename new table
        database.execSQL("ALTER TABLE meditation_tracks_new RENAME TO meditation_tracks")
    }
}
