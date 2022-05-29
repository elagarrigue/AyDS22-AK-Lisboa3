package ayds.lisboa.songinfo.moredetails.home.model

import ayds.lisboa.songinfo.moredetails.home.model.entities.Artist
import ayds.lisboa.songinfo.moredetails.home.model.repository.ArtistRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class MoreDetailsModelTest {

    private val repository: ArtistRepository = mockk()

    private val moreDetailsModel: MoreDetailsModel = MoreDetailsModelImpl(repository)

    @Test
    fun `on search artist it should notify the result`() {
        val artist: Artist = mockk()
        every { repository.getArtistByName("name") } returns artist
        val artistTester: (Artist) -> Unit = mockk(relaxed = true)
        moreDetailsModel.artistObservable.subscribe {
            artistTester(artist)
        }

        moreDetailsModel.searchArtist("name")

        verify { artistTester(artist) }
    }

}