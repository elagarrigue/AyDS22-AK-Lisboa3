package ayds.lisboa.songinfo.moredetails.model.repository.local.cards.sqldb

const val ARTISTS_TABLE = "artists"
const val ID_COLUMN = "id"
const val NAME_COLUMN = "artist"
const val URL_COLUMN = "info_url"
const val DESCRIPTION_COLUMN = "description"
const val SOURCE_COLUMN = "source"
const val LOGO_COLUMN = "source_logo_url"

const val createArtistTableQuery: String =
    "create table $ARTISTS_TABLE (" +
            "$ID_COLUMN integer PRIMARY KEY, " +
            "$NAME_COLUMN string, " +
            "$URL_COLUMN string, " +
            "$DESCRIPTION_COLUMN string, " +
            "$SOURCE_COLUMN integer, " +
            "$LOGO_COLUMN string)"