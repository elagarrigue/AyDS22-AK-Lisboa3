package ayds.lisboa.songinfo.moredetails.home.model.repository

import ayds.lisboa.songinfo.home.model.entities.EmptySong
import ayds.lisboa.songinfo.home.model.entities.SpotifySong
import ayds.lisboa.songinfo.moredetails.home.model.entities.EmptyArtist
import ayds.lisboa.songinfo.moredetails.home.model.entities.LastFMArtist
import ayds.lisboa.songinfo.moredetails.home.model.repository.external.lastfm.LastFMService
import ayds.lisboa.songinfo.moredetails.home.model.repository.local.lastfm.LastFMLocalStorage
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
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
    fun `given existing artist by name in the database should return artist and mark it as local`() {
        val artist= LastFMArtist(
            "name", "url","info", false
        )
        every { lastFMLocalStorage.getArtistByName("name") } returns artist

        val result = artistRepository.getArtistByName("name")

        assertEquals(artist, result)
        assertTrue(artist.isLocallyStored)
    }

    @Test
    fun `given non existing artist by name should get the artist and store it`() {
        val artist = LastFMArtist(
            "name", "url","info", false
        )
        every { lastFMLocalStorage.getArtistByName("name") } returns null
        every { lastFMService.getArtist("name") } returns artist

        val result = artistRepository.getArtistByName("name")

        assertEquals(artist, result)
        assertFalse(artist.isLocallyStored)
        verify { lastFMLocalStorage.insertArtist(artist) }
    }

    @Test
    fun `given service exception should return empty artist`() {
        every { lastFMLocalStorage.getArtistByName("name") } returns null
        every { lastFMService.getArtist("name") } throws mockk<Exception>()

        val result = artistRepository.getArtistByName("name")

        assertEquals(EmptyArtist, result)
    }

}