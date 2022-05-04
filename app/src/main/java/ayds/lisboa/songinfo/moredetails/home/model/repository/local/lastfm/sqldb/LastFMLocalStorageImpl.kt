package ayds.lisboa.songinfo.moredetails.home.model.repository.local.lastfm.sqldb

import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteDatabase
import android.content.ContentValues
import android.content.Context
import ayds.lisboa.songinfo.moredetails.home.model.entities.LastFMArtist
import ayds.lisboa.songinfo.moredetails.home.model.repository.local.lastfm.LastFMLocalStorage

const val DATABASE_NAME = "dictionary.db"
const val DATABASE_VERSION = 1

class LastFMLocalStorageImpl(
    context: Context,
    private val cursorToArtistMapper: CursorToArtistMapper
) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION),
    LastFMLocalStorage{

    private val projection = arrayOf(
        ID_COLUMN,
        URL_COLUMN,
        NAME_COLUMN,
        INFO_COLUMN
    )

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(createArtistTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    override fun insertArtist(query: String, artist: LastFMArtist) {
        val values = ContentValues().apply {
            put(NAME_COLUMN, artist.name)
            put(TERM_COLUMN, query)
            put(URL_COLUMN, artist.url)
            put(INFO_COLUMN, artist.info)
            put(SOURCE_COLUMN, 1)
        }

        writableDatabase.insert(ARTISTS_TABLE, null, values)
    }

    override fun getArtistByName(artist: String): LastFMArtist? {
        val cursor = readableDatabase.query(
            ARTISTS_TABLE,
            projection,
            "$NAME_COLUMN = ?",
            arrayOf(artist),
            null,
            null,
            "$NAME_COLUMN DESC"
        )

        return cursorToArtistMapper.map(cursor)
    }

}