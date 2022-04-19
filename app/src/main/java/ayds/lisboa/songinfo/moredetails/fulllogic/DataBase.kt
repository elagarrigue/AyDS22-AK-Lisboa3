package ayds.lisboa.songinfo.moredetails.fulllogic

import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteDatabase
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.util.Log
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

const val DATABASE_NAME = "dictionary.db"
const val DATABASE_VERSION = 1

const val ARTISTS_TABLE = "artists"
const val ID_COLUMN = "id"
const val ARTIST_COLUMN = "artist"
const val INFO_COLUMN = "info"
const val SOURCE_COLUMN = "source"

const val createArtistTableQuery: String =
    "create table $ARTISTS_TABLE (" +
            "$ID_COLUMN integer PRIMARY KEY, " +
            "$ARTIST_COLUMN string, " +
            "$INFO_COLUMN string, " +
            "$SOURCE_COLUMN integer)"

class DataBase(context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(createArtistTableQuery)
        Log.i("DB", "DB created")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    companion object {

        private val projection = arrayOf(
            "id",
            "artist",
            "info"
        )

        fun testDB() {
            var connection: Connection? = null
            try {
                // create a database connection
                connection = DriverManager.getConnection("jdbc:sqlite:./dictionary.db")
                val statement = connection.createStatement()
                statement.queryTimeout = 30 // set timeout to 30 sec.

                val rs = statement.executeQuery("select * from artists")
                while (rs.next()) {
                    // read the result set
                    println("id = " + rs.getInt("id"))
                    println("artist = " + rs.getString("artist"))
                    println("info = " + rs.getString("info"))
                    println("source = " + rs.getString("source"))
                }
            } catch (e: SQLException) {
                // if the error message is "out of memory",
                // it probably means no database file is found
                System.err.println(e.message)
            } finally {
                try {
                    connection?.close()
                } catch (e: SQLException) {
                    // connection close failed.
                    System.err.println(e)
                }
            }
        }

        fun saveArtist(dbHelper: DataBase, artist: String?, info: String?) {
            val db = dbHelper.writableDatabase

            val values = ContentValues().apply {
                put(ARTIST_COLUMN, artist)
                put(INFO_COLUMN, info)
                put(SOURCE_COLUMN, 1)
            }

            db.insert(ARTISTS_TABLE, null, values)
        }

        fun getInfo(dbHelper: DataBase, artist: String): String? {
            return getFirstInfoRow(getCursorForArtists(dbHelper, artist))
        }

        private fun getCursorForArtists(dbHelper: DataBase, artist: String): Cursor {
            val db = dbHelper.readableDatabase
            return db.query(
                ARTISTS_TABLE,
                projection,
                "$ARTIST_COLUMN = ?",
                arrayOf(artist),
                null,
                null,
                "$ARTIST_COLUMN DESC"
            )
        }

        private fun getFirstInfoRow(cursor: Cursor): String? {
            cursor.apply {
                return if (moveToNext()) {
                    val info = getString(
                        getColumnIndexOrThrow("$INFO_COLUMN")
                    )
                    close()
                    info
                } else
                    null
            }

        }
    }
}