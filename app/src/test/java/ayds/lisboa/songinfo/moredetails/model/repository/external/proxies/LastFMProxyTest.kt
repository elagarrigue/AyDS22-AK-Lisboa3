package ayds.lisboa.songinfo.moredetails.model.repository.external.proxies

import ayds.lisboa.lastfmdata.lastfm.LastFMService
import ayds.lisboa.lastfmdata.lastfm.entities.LastFMArtist
import ayds.lisboa.songinfo.moredetails.model.entities.CardImpl
import ayds.lisboa.songinfo.moredetails.model.entities.Source
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.*
import org.junit.Test

class LastFMProxyTest {

    private val service: LastFMService = mockk()
    private val proxyService: ProxyService = LastFMProxy(service)

    @Test
    fun `given artist name should return card`() {
        val lastFMArtist = LastFMArtist("name", "description", "infoUrl", "sourceLogoUrl")
        every { service.getArtist("name") } returns lastFMArtist

        val result = proxyService.getCard("name")
        val expectedCard = CardImpl("name", "infoUrl", "description", false, Source.LASTFM, "sourceLogoUrl")

        assertEquals(expectedCard, result)
        verify { service.getArtist("name") }
    }

    @Test
    fun `given service exception should return null`() {
        every { service.getArtist("name") } throws Exception()

        val result = proxyService.getCard("name")

        assertEquals(null, result)
        verify { service.getArtist("name") }
    }

}