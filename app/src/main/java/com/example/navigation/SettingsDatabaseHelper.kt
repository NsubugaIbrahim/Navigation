package com.example.navigation

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SettingsDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "settings.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "user_settings"
        
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_EMAIL = "email"
        private const val COLUMN_AUTO_ARM = "auto_arm"
        private const val COLUMN_NOTIFICATIONS = "notifications"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT NOT NULL,
                $COLUMN_EMAIL TEXT NOT NULL,
                $COLUMN_AUTO_ARM INTEGER NOT NULL,
                $COLUMN_NOTIFICATIONS INTEGER NOT NULL
            )
        """.trimIndent()
        db.execSQL(createTable)

        val defaultValues = ContentValues().apply {
            put(COLUMN_NAME, "Kayiwa Rahim")
            put(COLUMN_EMAIL, "kayiwa.rahim@students.mak.ac.ug")
            put(COLUMN_AUTO_ARM, 1) // true
            put(COLUMN_NOTIFICATIONS, 0) // false
        }
        db.insert(TABLE_NAME, null, defaultValues)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun getSettings(): Settings {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            null
        )

        return if (cursor.moveToFirst()) {
            Settings(
                name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)),
                autoArm = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_AUTO_ARM)) == 1,
                notifications = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_NOTIFICATIONS)) == 1
            )
        } else {
            Settings()
        }.also {
            cursor.close()
        }
    }

    fun updateSettings(settings: Settings) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, settings.name)
            put(COLUMN_EMAIL, settings.email)
            put(COLUMN_AUTO_ARM, if (settings.autoArm) 1 else 0)
            put(COLUMN_NOTIFICATIONS, if (settings.notifications) 1 else 0)
        }

        val rowsAffected = db.update(TABLE_NAME, values, "$COLUMN_ID = 1", null)
        if (rowsAffected == 0) {
            db.insert(TABLE_NAME, null, values)
        }
    }

    data class Settings(
        val name: String = "Kayiwa Rahim",
        val email: String = "kayiwa.rahim@students.mak.ac.ug",
        val autoArm: Boolean = true,
        val notifications: Boolean = false
    )
} 