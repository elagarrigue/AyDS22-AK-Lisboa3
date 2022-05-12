package ayds.lisboa.songinfo.moredetails.home.view

import java.lang.StringBuilder
import java.util.*

interface LastFMArtistBioParser {
    fun parseArtistBioToDisplayableHtml(artistBio: String, artistName: String): String
}

private const val FONT = "arial"
private const val WIDTH = 400

internal class LastFMArtistBioParserImpl : LastFMArtistBioParser {

    override fun parseArtistBioToDisplayableHtml(artistBio: String, artistName: String): String {
        val boldedArtistBio = artistBio.getBoldText(artistName)
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
            append("<html><div width=$WIDTH>")
            append("<font face=\"$FONT\">")
            append(this@toHtml)
            append("</font></div></html>")
        }.toString()
    }

}