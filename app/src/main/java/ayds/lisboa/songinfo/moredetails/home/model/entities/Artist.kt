package ayds.lisboa.songinfo.moredetails.home.model.entities

interface Artist {
    val name: String
    val url: String
    val bio: String
    var isLocallyStored: Boolean
}

data class LastFMArtist(
    override val name: String,
    override val url: String,
    override val bio: String,
    override var isLocallyStored: Boolean = false
) : Artist

object EmptyArtist : Artist {
    override val name: String = ""
    override val url: String = ""
    override val bio: String = ""
    override var isLocallyStored: Boolean = false
}