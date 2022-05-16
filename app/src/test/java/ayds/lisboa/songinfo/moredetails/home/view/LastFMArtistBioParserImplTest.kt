package ayds.lisboa.songinfo.moredetails.home.view

import androidx.core.text.HtmlCompat
import ayds.lisboa.songinfo.moredetails.home.model.entities.Artist
import ayds.lisboa.songinfo.moredetails.home.model.entities.LastFMArtist
import org.junit.Assert
import org.junit.Test

class LastFMArtistBioParserImplTest {

    private val lastFMArtistBioParserImpl by lazy { LastFMArtistBioParserImpl() }

    @Test
    fun `given an artist it should return the bio formatted`() {
        val artist: Artist = LastFMArtist(
            "name",
            "url",
            "bio",
            true,
        )

        val result = lastFMArtistBioParserImpl.parseArtistBioToDisplayableHtml(artist)

        val expected =
            HtmlCompat.fromHtml(
                "bio", HtmlCompat.FROM_HTML_MODE_LEGACY
            )


        Assert.assertEquals(expected, result)
    }

}