package ayds.lisboa.songinfo.moredetails.home.model.repository

import ayds.lisboa.songinfo.moredetails.home.model.entities.EmptyCard
import ayds.lisboa.songinfo.moredetails.home.model.entities.CardImpl
import ayds.lisboa.lastfmdata.lastfm.LastFMService
import ayds.lisboa.lastfmdata.lastfm.entities.LastFMArtist as ExternalArtist
import ayds.lisboa.songinfo.moredetails.home.model.repository.local.cards.LastFMLocalStorage
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import org.junit.Assert.*

class CardRepositoryTest {

    private val lastFMLocalStorage: LastFMLocalStorage = mockk(relaxUnitFun = true)
    private val lastFMService: LastFMService = mockk(relaxUnitFun = true)

    private val cardRepository: CardRepository = CardRepositoryImpl(
        lastFMLocalStorage,
        lastFMService
    )

    @Test
    fun `given non existing artist by name should return empty artist`() {
        every { lastFMLocalStorage.getCardByName("name") } returns null
        every { lastFMService.getArtist("name") } returns null

        val result = cardRepository.getCardByName("name")

        assertEquals(EmptyCard, result)
    }

    @Test
    fun `given existing artist by name in the database should return artist and mark it as local`() {
        val artist= CardImpl(
            "name", "url","info", false
        )
        every { lastFMLocalStorage.getCardByName("name") } returns artist

        val result = cardRepository.getCardByName("name")

        assertEquals(artist, result)
        assertTrue(artist.isLocallyStored)
    }

    @Test
    fun `given non existing artist by name should get the artist and store it`() {
        val artist = CardImpl(
            "name", "url","info", false
        )
        val externalArtist = ExternalArtist("name", "url", "info")
        every { lastFMLocalStorage.getCardByName("name") } returns null
        every { lastFMService.getArtist("name") } returns externalArtist

        val result = cardRepository.getCardByName("name")

        assertEquals(artist, result)
        assertFalse(artist.isLocallyStored)
        verify { lastFMLocalStorage.insertCard(artist) }
    }

    @Test
    fun `given service exception should return empty artist`() {
        every { lastFMLocalStorage.getCardByName("name") } returns null
        every { lastFMService.getArtist("name") } throws mockk<Exception>()

        val result = cardRepository.getCardByName("name")

        assertEquals(EmptyCard, result)
    }

}