package ayds.lisboa.songinfo.moredetails.home.model

import ayds.lisboa.songinfo.moredetails.home.model.entities.Card
import ayds.lisboa.songinfo.moredetails.home.model.repository.CardRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class MoreDetailsModelTest {

    private val repository: CardRepository = mockk()

    private val moreDetailsModel: MoreDetailsModel = MoreDetailsModelImpl(repository)

    @Test
    fun `on search artist it should notify the result`() {
        val card: Card = mockk()
        every { repository.getCardByName("name") } returns card
        val artistTester: (Card) -> Unit = mockk(relaxed = true)
        moreDetailsModel.cardObservable.subscribe {
            artistTester(card)
        }

        moreDetailsModel.searchCard("name")

        verify { artistTester(card) }
    }

}