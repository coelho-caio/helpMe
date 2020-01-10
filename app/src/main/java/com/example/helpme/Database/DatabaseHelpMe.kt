package com.example.helpme.Database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import com.example.helpme.Database.DatabaseHelpMe.DBHelpMe.COLUMN_1
import com.example.helpme.Database.DatabaseHelpMe.DBHelpMe.COLUMN_2
import com.example.helpme.Database.DatabaseHelpMe.DBHelpMe.TABLE_NAME

object DatabaseHelpMe {

    object DBHelpMe : BaseColumns {
        const val TABLE_NAME = "Usuarios"
        const val COLUMN_1 = "login"
        const val COLUMN_2 = "senha"
    }
}
    private const val SQL_CREATE_ENTRIES = "CREATE TABLE ${TABLE_NAME}" +
            " ("+"${BaseColumns._ID} INTEGER PRIMARY KEY," +
            "${COLUMN_1} TEXT," +
            "${COLUMN_2} TEXT)"

    private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${TABLE_NAME}"

class LoginDataBaseHelper(context:Context): SQLiteOpenHelper (context, DATABASE_NAME, null, DATABASE_VERSION){

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

    fun insert(login:String, password:String): Boolean {
        val db = writableDatabase
        val values= ContentValues().apply {
            put(COLUMN_1,login)
            put(COLUMN_2,password)
        }
        val newRowId = db?.insert(TABLE_NAME,null,values)
        if (newRowId != null) {
            if( newRowId.equals(-1))
                return false
        }
        return true

    }

    fun readEmail(email:String) : Boolean{
        val db = readableDatabase

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        val projection = arrayOf(BaseColumns._ID, COLUMN_1, COLUMN_2)

        // Filter results WHERE "title" = 'My Title'
        val selection = "${COLUMN_1} = ?"
        val selectionArgs = arrayOf(email)

        val cursor = db.query(
            TABLE_NAME,   // The table to query
            projection,             // The array of columns to return (pass null to get all)
            selection,              // The columns for the WHERE clause
            selectionArgs,          // The values for the WHERE clause
            null,                   // don't group the rows
            null,                   // don't filter by row groups
            null               // The sort order
        )
        return cursor.count>0
    }

    fun readPassword(password: String) : Boolean{
        val db = readableDatabase

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        val projection = arrayOf(BaseColumns._ID, COLUMN_1, COLUMN_2)

        // Filter results WHERE "title" = 'My Title'
        val selection = "${COLUMN_2} = ?"
        val selectionArgs = arrayOf(password)

        val cursor = db.query(
            TABLE_NAME,   // The table to query
            projection,             // The array of columns to return (pass null to get all)
            selection,              // The columns for the WHERE clause
            selectionArgs,          // The values for the WHERE clause
            null,                   // don't group the rows
            null,                   // don't filter by row groups
            null               // The sort order
        )
        return cursor.count>0
    }
    companion object {
        // If you change the database schema, you must increment the database version.
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "Login.db"
    }
}

