package ayds.lisboa.songinfo.moredetails.home.model.repository.local.lastfm.sqldb

import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteDatabase
import android.content.ContentValues
import android.content.Context
import android.database.Cursor

const val DATABASE_NAME = "dictionary.db"
const val DATABASE_VERSION = 1

class LastFMLocalStorageImpl(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    private val projection = arrayOf(
        ID_COLUMN,
        ARTIST_COLUMN,
        INFO_COLUMN
    )

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(createArtistTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    fun insertArtist(artist: String?, info: String?) {
        val values = ContentValues().apply {
            put(ARTIST_COLUMN, artist)
            put(INFO_COLUMN, info)
            put(SOURCE_COLUMN, 1)
        }

        writableDatabase.insert(ARTISTS_TABLE, null, values)
    }

    fun getArtistInfoByArtistName(artist: String): String? {
        return getFirstInfoRow(getCursorForArtists(artist))
    }

    private fun getCursorForArtists(artist: String): Cursor {
        return readableDatabase.query(
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
                    getColumnIndexOrThrow(INFO_COLUMN)
                )
                close()
                info
            } else
                null
        }
    }

}