package ayds.lisboa.songinfo.moredetails.home.model.entities


enum class Source {
    WIKIPEDIA, LASTFM, NEW_YORK_TIMES ,UNDEFINED
}

interface Card {
    val name: String
    val url: String
    val info: String
    var isLocallyStored: Boolean
    val description: String
    val infoUrl: String
    val source: Source
    val sourceLogoUrl: String
}

data class LastFMCard(
    override val name: String,
    override val url: String,
    override val info: String,
    override var isLocallyStored: Boolean = false,
    override val description: String,//preguntar
    override val infoUrl: String,//preguntar
    override val source: Source,
    override val sourceLogoUrl: String,//preguntar
) : Card

object EmptyCard : Card {
    override val name: String = ""
    override val url: String = ""
    override val info: String = ""
    override var isLocallyStored: Boolean = false
    override val description: String = ""//preguntar
    override val infoUrl: String = ""//preguntar
    override val source: Source = Source.UNDEFINED
    override val sourceLogoUrl: String = ""//preguntar
}