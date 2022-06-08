package ayds.lisboa.songinfo.moredetails.home.model.repository.external.proxies

import ayds.lisboa.lastfmdata.lastfm.entities.LastFMArtist
import ayds.lisboa.songinfo.moredetails.home.model.entities.CardImpl
import ayds.lisboa.songinfo.moredetails.home.model.entities.Source
import ayds.winchester2.wikipedia.WikipediaArticle
import ayds.winchester2.wikipedia.ExternalRepository as WikipediaService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.*
import org.junit.Test

class WikipediaProxyTest {

    private val service: WikipediaService = mockk()
    private val proxyService: ProxyService = WikipediaProxy(service)

    @Test
    fun `given artist name should return card`() {
        val wikipediaArticle = WikipediaArticle("infoUrl", "description", "sourceLogoUrl")
        every { service.getArtistDescription("name") } returns wikipediaArticle

        val result = proxyService.getCard("name")
        val expectedCard = CardImpl("name", "infoUrl", "description", false, Source.WIKIPEDIA, "sourceLogoUrl")

        assertEquals(expectedCard, result)
        verify { service.getArtistDescription("name") }
    }

    @Test
    fun `given service exception should return null`() {
        every { service.getArtistDescription("name") } throws Exception()

        val result = proxyService.getCard("name")

        assertEquals(null, result)
        verify { service.getArtistDescription("name") }
    }

}