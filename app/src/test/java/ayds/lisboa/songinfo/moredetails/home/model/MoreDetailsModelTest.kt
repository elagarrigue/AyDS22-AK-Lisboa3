package ayds.lisboa.songinfo.moredetails.home.model

import ayds.lisboa.songinfo.moredetails.home.model.entities.Card
import ayds.lisboa.songinfo.moredetails.home.model.entities.CardImpl
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
        val card: CardImpl = mockk()
        val cards: List<Card> = listOf(card)

        every { repository.getCardsByName("name") } returns cards

        val artistTester: (Card) -> Unit = mockk(relaxed = true)
        moreDetailsModel.cardObservable.subscribe {
            artistTester(it)
        }

        moreDetailsModel.searchCards("name")

        verify { artistTester(card) }
    }

    @Test
    fun `on search artist it should not notify any result if no card is found`() {
        val cards: List<Card> = listOf()

        every { repository.getCardsByName("name") } returns cards

        val artistTester: (Card) -> Unit = mockk(relaxed = true)
        moreDetailsModel.cardObservable.subscribe {
            artistTester(it)
        }

        moreDetailsModel.searchCards("name")
        verify(exactly = 0) { artistTester(mockk()) }
    }

}