package ayds.lisboa.songinfo.moredetails.view

import ayds.lisboa.songinfo.moredetails.model.entities.Card
import ayds.lisboa.songinfo.moredetails.model.entities.CardImpl
import ayds.lisboa.songinfo.moredetails.model.entities.EmptyCard.source
import org.junit.Assert
import org.junit.Test

class CardBioParserImplTest {

    private val artistBioParserImpl by lazy { ArtistBioParserImpl() }

    @Test
    fun `given an artist it should return the bio formatted`() {
        val card: Card = CardImpl(
            "name",
            "infoUrl",
            "description",
            true,
            source,
            "sourceLogoUrl"
        )

        val result = artistBioParserImpl.parseArtistBioToDisplayableHtml(card)

        val expected =
            "<html><div width=400>" +
            "<font face=\"arial\">" +
            "description" +
            "</font></div></html>"

        Assert.assertEquals(expected, result)
    }

}