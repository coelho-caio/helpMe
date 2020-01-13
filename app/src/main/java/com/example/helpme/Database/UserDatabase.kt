import DatabaseHelpMe.DBHelpMe.COLUMN_LOGIN
import DatabaseHelpMe.DBHelpMe.COLUMN_PASS
import DatabaseHelpMe.DBHelpMe.TABLE_USER
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import com.example.helpme.model.User

object DatabaseHelpMe {

    object DBHelpMe : BaseColumns {
        const val TABLE_USER = "user"
        const val COLUMN_LOGIN = "login"
        const val COLUMN_PASS = "password"
    }
}

private const val SQL_CREATE_ENTRIES = "CREATE TABLE $TABLE_USER" +
        " (" + "${BaseColumns._ID} INTEGER PRIMARY KEY," +
        "$COLUMN_LOGIN TEXT," +
        "$COLUMN_PASS TEXT)"

private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS $TABLE_USER"

class UserDatabase(context: Context) :
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

    fun insert(user: User): Boolean {
        val values = ContentValues().apply {
            put(COLUMN_LOGIN, user.login)
            put(COLUMN_PASS, user.password)
        }
        val newRowId = write?.insert(TABLE_USER, null, values)
        if (newRowId != null) {
            if (newRowId.equals(-1))
                return false
        }
        return true
    }

    fun readEmail(email: String): Boolean {
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.

        val projection = arrayOf(BaseColumns._ID, COLUMN_LOGIN, COLUMN_PASS)

        // Filter results WHERE "title" = 'My Title'
        val selection = "$COLUMN_LOGIN = ?"
        val selectionArgs = arrayOf(email)

        val cursor = read.query(
            TABLE_USER,   // The table to query
            projection,             // The array of columns to return (pass null to get all)
            selection,              // The columns for the WHERE clause
            selectionArgs,          // The values for the WHERE clause
            null,                   // don't group the rows
            null,                   // don't filter by row groups
            null               // The sort order
        )
        return cursor.count > 0
    }

    fun readPassword(password: String): Boolean {

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        val projection = arrayOf(BaseColumns._ID, COLUMN_LOGIN, COLUMN_PASS)

        // Filter results WHERE "title" = 'My Title'
        val selection = "$COLUMN_PASS = ?"
        val selectionArgs = arrayOf(password)

        val cursor = read.query(
            TABLE_USER,   // The table to query
            projection,             // The array of columns to return (pass null to get all)
            selection,              // The columns for the WHERE clause
            selectionArgs,          // The values for the WHERE clause
            null,                   // don't group the rows
            null,                   // don't filter by row groups
            null               // The sort order
        )
        return cursor.count > 0
    }

    companion object {
        // If you change the database schema, you must increment the database version.
        const val DATABASE_VERSION = 2
        const val DATABASE_NAME = "userLogin.db"
    }
}