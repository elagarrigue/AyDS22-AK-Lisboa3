package ayds.lisboa.songinfo.moredetails.home.view

import ayds.lisboa.songinfo.moredetails.home.model.entities.Card
import ayds.lisboa.songinfo.moredetails.home.model.entities.LastFMCard
import org.junit.Assert
import org.junit.Test

class LastFMCardBioParserImplTest {

    private val lastFMArtistBioParserImpl by lazy { LastFMArtistBioParserImpl() }

    @Test
    fun `given an artist it should return the bio formatted`() {
        val card: Card = LastFMCard(
            "name",
            "url",
            "bio",
            true,
        )

        val result = lastFMArtistBioParserImpl.parseArtistBioToDisplayableHtml(card)

        val expected =
            "<html><div width=400>" +
            "<font face=\"arial\">" +
            "bio" +
            "</font></div></html>"

        Assert.assertEquals(expected, result)
    }

}