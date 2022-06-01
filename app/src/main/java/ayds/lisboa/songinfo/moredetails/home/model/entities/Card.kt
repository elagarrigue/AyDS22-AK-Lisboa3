package ayds.lisboa.songinfo.moredetails.home.model.entities

enum class Source {
    WIKIPEDIA, LASTFM, NEW_YORK_TIMES ,UNDEFINED
}

interface Card {
    val name: String
    val infoUrl: String
    val description: String
    var isLocallyStored: Boolean
    val source: Source
    val sourceLogoUrl: String
}

data class LastFMCard(
    override val name: String,
    override val infoUrl: String,
    override val description: String,
    override var isLocallyStored: Boolean = false,
    override val source: Source,
    override val sourceLogoUrl: String,
) : Card

object EmptyCard : Card {
    override val name: String = ""
    override val infoUrl: String = ""
    override val description: String = ""
    override var isLocallyStored: Boolean = false
    override val source: Source = Source.UNDEFINED
    override val sourceLogoUrl: String = ""
}