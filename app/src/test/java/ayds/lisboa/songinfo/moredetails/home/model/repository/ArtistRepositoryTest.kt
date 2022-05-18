package ayds.lisboa.songinfo.moredetails.home.model.repository

import ayds.lisboa.songinfo.moredetails.home.model.entities.EmptyArtist
import ayds.lisboa.songinfo.moredetails.home.model.repository.external.lastfm.LastFMService
import ayds.lisboa.songinfo.moredetails.home.model.repository.local.lastfm.LastFMLocalStorage
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import org.junit.Assert.*

class ArtistRepositoryTest {

    private val lastFMLocalStorage: LastFMLocalStorage = mockk(relaxUnitFun = true)
    private val lastFMService: LastFMService = mockk(relaxUnitFun = true)

    private val artistRepository: ArtistRepository = ArtistRepositoryImpl(
        lastFMLocalStorage,
        lastFMService
    )

    @Test
    fun `given non existing artist by name should return empty artist`() {
        every { lastFMLocalStorage.getArtistByName("name") } returns null
        every { lastFMService.getArtist("name") } returns null

        val result = artistRepository.getArtistByName("name")

        assertEquals(EmptyArtist, result)
    }

    @Test
    fun `given service exception should return empty artist`() {
        every { lastFMLocalStorage.getArtistByName("name") } returns null
        every { lastFMService.getArtist("name") } throws mockk<Exception>()

        val result = artistRepository.getArtistByName("name")

        assertEquals(EmptyArtist, result)
    }

}