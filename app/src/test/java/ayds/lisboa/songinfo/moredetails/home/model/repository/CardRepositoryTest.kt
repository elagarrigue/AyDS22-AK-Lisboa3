package ayds.lisboa.songinfo.moredetails.home.model.repository

import ayds.lisboa.songinfo.moredetails.home.model.entities.CardImpl
import ayds.lisboa.songinfo.moredetails.home.model.repository.external.Broker
import ayds.lisboa.songinfo.moredetails.home.model.repository.local.cards.LocalStorage
import ayds.lisboa.songinfo.moredetails.home.model.entities.Card
import ayds.lisboa.songinfo.moredetails.home.model.entities.Source
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import org.junit.Assert.*

class CardRepositoryTest {

    private val artistsLocalStorage: LocalStorage = mockk(relaxUnitFun = true)
    private val artistsInfoBroker: Broker = mockk(relaxUnitFun = true)

    private val cardRepository: CardRepository = CardRepositoryImpl(
        artistsLocalStorage,
        artistsInfoBroker
    )

    @Test
    fun `given non existing artist by name should return empty cards list`() {
        every { artistsLocalStorage.getCardsByName("name") } returns listOf<Card>()
        every { artistsInfoBroker.getInfoByArtistName("name") } returns listOf<Card>()

        val result = cardRepository.getCardsByName("name")
        val emptyListOfCards = listOf<Card>()

        assertEquals(emptyListOfCards, result)
    }

    @Test
    fun `given existing artist by name in the database should return artist and mark it as local`() {
        val artist = CardImpl(
            "name", "url","info", true, Source.LASTFM, "logoUrl"
        )
        val artists = listOf<Card>(artist)
        every { artistsLocalStorage.getCardsByName("name") } returns artists

        val result = cardRepository.getCardsByName("name")

        assertEquals(artists, result)
        assertTrue(artist.isLocallyStored)
    }

    @Test
    fun `given non existing artist by name should get the artist and store it`() {
        val artist = CardImpl(
            "name", "url","info", false, Source.LASTFM, "logoUrl"
        )
        val artists = listOf<Card>(artist)

        every { artistsLocalStorage.getCardsByName("name") } returns listOf()
        every { artistsInfoBroker.getInfoByArtistName("name") } returns artists

        val result = cardRepository.getCardsByName("name")

        assertEquals(artists, result)
        assertFalse(artist.isLocallyStored)
        verify { artistsLocalStorage.insertCards(artists) }
    }
}