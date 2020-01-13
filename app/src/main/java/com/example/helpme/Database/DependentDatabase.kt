package com.example.helpme.Database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import com.example.helpme.Database.DatabaseHelpMe.DBHelpMe.COLUMN_EMAIL
import com.example.helpme.Database.DatabaseHelpMe.DBHelpMe.COLUMN_NAME
import com.example.helpme.Database.DatabaseHelpMe.DBHelpMe.COLUMN_PHONE
import com.example.helpme.Database.DatabaseHelpMe.DBHelpMe.COLUMN_USER
import com.example.helpme.Database.DatabaseHelpMe.DBHelpMe.TABLE_DEP
import com.example.helpme.model.Dependent

object DatabaseHelpMe {

    object DBHelpMe : BaseColumns {
        const val TABLE_DEP = "dependent"
        const val COLUMN_NAME = "name"
        const val COLUMN_EMAIL = "email"
        const val COLUMN_PHONE = "phone"
        const val COLUMN_USER = "userId"
    }
}

private const val SQL_CREATE_ENTRIES = "CREATE TABLE $TABLE_DEP" +
        " (" + "${BaseColumns._ID} INTEGER PRIMARY KEY," +
        "$COLUMN_NAME TEXT," +
        "$COLUMN_EMAIL TEXT," +
        "$COLUMN_PHONE TEXT," +
        "$COLUMN_USER INTEGER)"

private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS $TABLE_DEP"

class DependentDatabase(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    private val write = writableDatabase
    private val read = readableDatabase

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    fun insert(dependent: Dependent): Boolean {
        val values = ContentValues().apply {
            put(COLUMN_NAME, dependent.name)
            put(COLUMN_EMAIL, dependent.email)
            put(COLUMN_PHONE, dependent.phone)
            put(COLUMN_USER, dependent.userId)
        }
        val newRowId = write?.insert(TABLE_DEP, null, values)
        if (newRowId != null) {
            if (newRowId.equals(-1))
                return false
        }
        return true
    }

    fun getAllDependent(): Cursor? {
        return read.rawQuery("SELECT * FROM $TABLE_DEP", null)
    }

    companion object {
        // If you change the database schema, you must increment the database version.
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "dependent.db"
    }
}