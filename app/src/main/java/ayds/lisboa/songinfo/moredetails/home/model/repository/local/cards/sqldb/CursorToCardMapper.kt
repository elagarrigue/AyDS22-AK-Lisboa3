package ayds.lisboa.songinfo.moredetails.home.model.repository.local.cards.sqldb

import android.database.Cursor
import android.database.SQLException
import ayds.lisboa.songinfo.home.model.repository.local.spotify.sqldb.RELEASE_DATE_PRECISION_COLUMN
import ayds.lisboa.songinfo.moredetails.home.model.entities.CardImpl
import ayds.lisboa.songinfo.moredetails.home.model.entities.Source

interface CursorToCardMapper {

    fun map(cursor: Cursor): CardImpl?
}

internal class CursorToCardMapperImpl : CursorToCardMapper {

    override fun map(cursor: Cursor): CardImpl? =
        try {
            with(cursor) {
                if (moveToNext()) {
                    CardImpl(
                        name = getString(getColumnIndexOrThrow(NAME_COLUMN)),
                        infoUrl = getString(getColumnIndexOrThrow(URL_COLUMN)),
                        description = getString(getColumnIndexOrThrow(DESCRIPTION_COLUMN)),
                        source = getCardSource(),
                        sourceLogoUrl = getString(getColumnIndexOrThrow(LOGO_COLUMN))
                    )
                } else {
                    null
                }
            }
        } catch (e: SQLException) {
            e.printStackTrace()
            null
        }

    private fun Cursor.getCardSource() =
        Source.values()[
                getInt(
                    getColumnIndexOrThrow(
                        RELEASE_DATE_PRECISION_COLUMN
                    )
                )
        ]
}