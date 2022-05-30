package ayds.lisboa.songinfo.moredetails.home.view

import ayds.lisboa.songinfo.moredetails.home.model.entities.Card
import java.lang.StringBuilder
import java.util.*

interface LastFMArtistBioParser {
    fun parseArtistBioToDisplayableHtml(card: Card): String
}

private const val WIDTH = 400
private const val HEADER = "<html><div width=$WIDTH>"
private const val FONT = "<font face=\"arial\">"
private const val FOOTER = "</font></div></html>"

internal class LastFMArtistBioParserImpl : LastFMArtistBioParser {

    override fun parseArtistBioToDisplayableHtml(card: Card): String {
        val boldedArtistBio = card.info.getBoldText(card.name)
        return boldedArtistBio.toHtml()
    }

    private fun String.getBoldText(artistName: String): String {
        return this
            .replace("'", " ")
            .replace("\n", "<br>")
            .replace(
                "(?i)" + artistName.toRegex(),
                "<b>" + artistName.uppercase(Locale.getDefault()) + "</b>"
            )
    }

    private fun String.toHtml(): String {
        return StringBuilder().apply {
            append(HEADER)
            append(FONT)
            append(this@toHtml)
            append(FOOTER)
        }.toString()
    }

}