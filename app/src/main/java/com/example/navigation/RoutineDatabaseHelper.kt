package com.example.navigation

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class RoutineDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "routines.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "routines"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_TIME = "time"
        private const val COLUMN_RECURRENCE = "recurrence"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT NOT NULL,
                $COLUMN_TIME TEXT NOT NULL,
                $COLUMN_RECURRENCE TEXT NOT NULL
            )
        """.trimIndent()
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addRoutine(routine: Routine): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, routine.name)
            put(COLUMN_TIME, routine.time)
            put(COLUMN_RECURRENCE, routine.recurrence)
        }
        return db.insert(TABLE_NAME, null, values)
    }

    fun getAllRoutines(): List<Routine> {
        val routines = mutableListOf<Routine>()
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_NAME,
            arrayOf(COLUMN_ID, COLUMN_NAME, COLUMN_TIME, COLUMN_RECURRENCE),
            null, null, null, null, null
        )

        with(cursor) {
            while (moveToNext()) {
                val name = getString(getColumnIndexOrThrow(COLUMN_NAME))
                val time = getString(getColumnIndexOrThrow(COLUMN_TIME))
                val recurrence = getString(getColumnIndexOrThrow(COLUMN_RECURRENCE))
                routines.add(Routine(name, time, recurrence))
            }
        }
        cursor.close()
        return routines
    }

    fun updateRoutine(id: Int, routine: Routine): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, routine.name)
            put(COLUMN_TIME, routine.time)
            put(COLUMN_RECURRENCE, routine.recurrence)
        }
        return db.update(
            TABLE_NAME,
            values,
            "$COLUMN_ID = ?",
            arrayOf(id.toString())
        )
    }

    fun deleteRoutine(id: Int): Int {
        val db = this.writableDatabase
        return db.delete(
            TABLE_NAME,
            "$COLUMN_ID = ?",
            arrayOf(id.toString())
        )
    }
} 