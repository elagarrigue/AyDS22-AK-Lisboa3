package ayds.lisboa.songinfo.moredetails.model.repository.local.cards.sqldb

import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteDatabase
import android.content.ContentValues
import android.content.Context
import ayds.lisboa.songinfo.moredetails.model.entities.Card
import ayds.lisboa.songinfo.moredetails.model.repository.local.cards.LocalStorage

const val DATABASE_NAME = "dictionary.db"
const val DATABASE_VERSION = 1

class LastFMLocalStorageImpl(
    context: Context,
    private val cursorToCardMapper: CursorToCardMapper
) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION),
    LocalStorage {

    private val projection = arrayOf(
        ID_COLUMN,
        URL_COLUMN,
        NAME_COLUMN,
        DESCRIPTION_COLUMN,
        SOURCE_COLUMN,
        LOGO_COLUMN
    )

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(createArtistTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    override fun insertCards(cards: List<Card>) {
        cards.forEach {
            val values = getValues(it)
            writableDatabase.insert(ARTISTS_TABLE, null, values)
        }
    }

    private fun getValues(card: Card): ContentValues {
        val values = ContentValues().apply {
            put(NAME_COLUMN, card.name)
            put(URL_COLUMN, card.infoUrl)
            put(DESCRIPTION_COLUMN, card.description)
            put(SOURCE_COLUMN, card.source.ordinal)
            put(LOGO_COLUMN, card.sourceLogoUrl)
        }
        return values
    }

    override fun getCardsByName(term: String): List<Card> {
        val cursor = readableDatabase.query(
            ARTISTS_TABLE,
            projection,
            "$NAME_COLUMN = ?",
            arrayOf(term),
            null,
            null,
            "$NAME_COLUMN DESC"
        )

        return cursorToCardMapper.map(cursor)
    }

}