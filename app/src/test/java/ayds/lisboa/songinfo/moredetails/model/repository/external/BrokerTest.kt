package ayds.lisboa.songinfo.moredetails.model.repository.external

import ayds.lisboa.songinfo.moredetails.model.entities.EmptyCard
import ayds.lisboa.songinfo.moredetails.model.entities.Card
import ayds.lisboa.songinfo.moredetails.model.entities.CardImpl
import ayds.lisboa.songinfo.moredetails.model.entities.Source
import ayds.lisboa.songinfo.moredetails.model.repository.external.proxies.ProxyService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import org.junit.Assert.*

class BrokerTest {

    private val proxies: List<ProxyService> = listOf(mockk(), mockk(), mockk())
    private val broker: Broker = BrokerImpl(proxies)

    @Test
    fun `given artist name should return list of artist info card`() {
        val card: Card =
            CardImpl("name", "infoUrl", "description", false, Source.UNDEFINED, "logoUrl")
        proxies.forEach {
            every { it.getCard("name") } returns card
        }

        val result = broker.getInfoByArtistName("name")

        assertEquals(listOf(card, card, card), result)
        proxies.forEach {
            verify { it.getCard("name") }
        }
    }

    @Test
    fun `given service exception should return empty list`() {
        proxies.forEach {
            every { it.getCard("name") } returns EmptyCard
        }

        val result = broker.getInfoByArtistName("name")

        assertEquals(listOf<Card>(), result)
        proxies.forEach {
            verify { it.getCard("name") }
        }
    }
}