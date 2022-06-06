package ayds.lisboa.songinfo.moredetails.home.model.repository.external

import ayds.lisboa.songinfo.moredetails.home.model.entities.EmptyCard
import ayds.lisboa.songinfo.moredetails.home.model.entities.Card
import ayds.lisboa.songinfo.moredetails.home.model.entities.CardImpl
import ayds.lisboa.songinfo.moredetails.home.model.entities.Source
import ayds.lisboa.songinfo.moredetails.home.model.repository.external.proxies.ProxyService
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import org.junit.Assert.*

class BrokerTest {

    private val proxy: ProxyService = mockk()
    private val broker: Broker = BrokerImpl(listOf(proxy))

    @Test
    fun `given artist name should return list of artist info card`() {
        val card: Card =
            CardImpl("name", "infoUrl", "description", false, Source.UNDEFINED, "logoUrl")
        every { proxy.getCard("name") } returns card

        val result = broker.getInfoByArtistName("name")

        assertEquals(listOf(card), result)
    }

    @Test
    fun `given service exception should return empty list`() {
        every { proxy.getCard("name") } returns EmptyCard

        val result = broker.getInfoByArtistName("name")

        assertEquals(listOf<Card>(), result)
    }

}