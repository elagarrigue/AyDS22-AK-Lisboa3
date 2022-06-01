package ayds.lisboa.songinfo.moredetails.home.model.repository.local.lastfm.sqldb

import android.database.Cursor
import android.database.SQLException
import ayds.lisboa.songinfo.home.model.repository.local.spotify.sqldb.RELEASE_DATE_PRECISION_COLUMN
import ayds.lisboa.songinfo.moredetails.home.model.entities.LastFMCard
import ayds.lisboa.songinfo.moredetails.home.model.entities.Source

interface CursorToCardMapper {

    fun map(cursor: Cursor): LastFMCard?
}

internal class CursorToCardMapperImpl : CursorToCardMapper {

    override fun map(cursor: Cursor): LastFMCard? =
        try {
            with(cursor) {
                if (moveToNext()) {
                    LastFMCard(
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