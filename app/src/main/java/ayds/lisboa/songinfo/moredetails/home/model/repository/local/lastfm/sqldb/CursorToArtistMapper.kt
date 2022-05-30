package ayds.lisboa.songinfo.moredetails.home.model.repository.local.lastfm.sqldb

import android.database.Cursor
import android.database.SQLException
import ayds.lisboa.songinfo.moredetails.home.model.entities.LastFMCard

interface CursorToArtistMapper {

    fun map(cursor: Cursor): LastFMCard?
}

internal class CursorToArtistMapperImpl : CursorToArtistMapper {

    override fun map(cursor: Cursor): LastFMCard? =
        try {
            with(cursor) {
                if (moveToNext()) {
                    LastFMCard(
                        name = getString(getColumnIndexOrThrow(NAME_COLUMN)),
                        url = getString(getColumnIndexOrThrow(URL_COLUMN)),
                        info = getString(getColumnIndexOrThrow(INFO_COLUMN)),
                    )
                } else {
                    null
                }
            }
        } catch (e: SQLException) {
            e.printStackTrace()
            null
        }
}