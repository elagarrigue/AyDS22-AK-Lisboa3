package ayds.lisboa.songinfo.moredetails.home.model.repository.local.lastfm.sqldb

const val ARTISTS_TABLE = "artists"
const val ID_COLUMN = "id"
const val NAME_COLUMN = "artist"
const val INFO_COLUMN = "info"
const val SOURCE_COLUMN = "source"

const val createArtistTableQuery: String =
    "create table $ARTISTS_TABLE (" +
            "$ID_COLUMN integer PRIMARY KEY, " +
            "$NAME_COLUMN string, " +
            "$INFO_COLUMN string, " +
            "$SOURCE_COLUMN integer)"