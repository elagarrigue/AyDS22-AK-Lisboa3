package ayds.lisboa.songinfo.moredetails.home.model.repository.local.lastfm.sqldb

import android.database.Cursor
import android.database.SQLException
import ayds.lisboa.songinfo.moredetails.home.model.entities.LastFMArtist

interface CursorToArtistMapper {

    fun map(cursor: Cursor): LastFMArtist?
}

internal class CursorToArtistMapperImpl : CursorToArtistMapper {

    override fun map(cursor: Cursor): LastFMArtist? =
        try {
            with(cursor) {
                if (moveToNext()) {
                    LastFMArtist(
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