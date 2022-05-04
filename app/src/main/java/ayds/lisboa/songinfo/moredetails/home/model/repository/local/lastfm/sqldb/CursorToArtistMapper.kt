package ayds.lisboa.songinfo.moredetails.home.model.repository.local.lastfm.sqldb

import android.database.Cursor
import android.database.SQLException
import ayds.lisboa.songinfo.moredetails.home.model.entities.Artist
import ayds.lisboa.songinfo.moredetails.home.model.entities.LastFMArtist

interface CursorToArtistMapper {

    fun map(cursor: Cursor): Artist?
}

internal class CursorToArtistMapperImpl : CursorToArtistMapper {

    override fun map(cursor: Cursor): Artist? =
        try {
            with(cursor) {
                if (moveToNext()) {
                    LastFMArtist(
                        name = getString(getColumnIndexOrThrow(ID_COLUMN)),
                        url = getString(getColumnIndexOrThrow(ARTIST_COLUMN)),
                        bio = getString(getColumnIndexOrThrow(INFO_COLUMN)),
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