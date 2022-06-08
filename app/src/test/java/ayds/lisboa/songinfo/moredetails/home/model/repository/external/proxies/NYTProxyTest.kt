package ayds.lisboa.songinfo.moredetails.home.model.repository.external.proxies

import ayds.lisboa.lastfmdata.lastfm.LastFMInjector
import ayds.lisboa.lastfmdata.lastfm.LastFMService
import ayds.lisboa.lastfmdata.lastfm.entities.LastFMArtist
import ayds.lisboa.songinfo.moredetails.home.model.entities.Card
import ayds.lisboa.songinfo.moredetails.home.model.entities.CardImpl
import ayds.lisboa.songinfo.moredetails.home.model.entities.Source
import ayds.ny3.newyorktimes.NytArticleService
import ayds.ny3.newyorktimes.NytArtistInfo
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.*
import org.junit.Test

class NYTProxyTest {

    private val service: NytArticleService = mockk()
    private val proxyService: ProxyService = NYTProxy(service)

    @Test
    fun `given artist name should return card`() {
        val nytArtist = NytArtistInfo("name", "description", "infoUrl", "sourceLogoUrl")
        every { service.getArtistInfo("name") } returns nytArtist

        val result = proxyService.getCard("name")
        val expectedCard = CardImpl("name", "infoUrl", "description", false, Source.NEW_YORK_TIMES, "sourceLogoUrl")

        assertEquals(expectedCard, result)
        verify { service.getArtistInfo("name") }
    }

    @Test
    fun `given service exception should return null`() {
        every { service.getArtistInfo("name") } throws Exception()

        val result = proxyService.getCard("name")

        assertEquals(null, result)
        verify { service.getArtistInfo("name") }
    }

}