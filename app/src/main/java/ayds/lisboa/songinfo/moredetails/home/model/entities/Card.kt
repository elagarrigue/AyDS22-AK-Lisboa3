package ayds.lisboa.songinfo.moredetails.home.model.entities

interface Card {
    val name: String
    val url: String
    val info: String
    var isLocallyStored: Boolean
}

data class LastFMCard(
    override val name: String,
    override val url: String,
    override val info: String,
    override var isLocallyStored: Boolean = false
) : Card

object EmptyCard : Card {
    override val name: String = ""
    override val url: String = ""
    override val info: String = ""
    override var isLocallyStored: Boolean = false
}