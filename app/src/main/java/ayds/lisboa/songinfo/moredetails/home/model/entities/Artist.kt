package ayds.lisboa.songinfo.moredetails.home.model.entities

interface Artist {
    val id: Int
    val info: String
    var isLocallyStored: Boolean
}

data class LastFMArtist(
    override val id: Int,
    override val info: String,
    override var isLocallyStored: Boolean = false
) : Artist

object EmptyArtist : Artist {
    override val id: Int = 0
    override val info: String = ""
    override var isLocallyStored: Boolean = false
}