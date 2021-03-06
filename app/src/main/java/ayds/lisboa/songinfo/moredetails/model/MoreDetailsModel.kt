package ayds.lisboa.songinfo.moredetails.model

import ayds.lisboa.songinfo.moredetails.model.entities.Card
import ayds.lisboa.songinfo.moredetails.model.repository.CardRepository
import ayds.observer.Observable
import ayds.observer.Subject

interface MoreDetailsModel {
    val cardObservable: Observable<Card>
    fun searchCards(name: String)
    fun nextCard()
}

internal class MoreDetailsModelImpl(
    private val repository: CardRepository
) : MoreDetailsModel {

    override val cardObservable = Subject<Card>()

    private var cards: List<Card> = listOf()
    private var cardIndex = 0

    override fun searchCards(name: String) {
        repository.getCardsByName(name).let {
            cards = it
            nextCard()
        }
    }

    override fun nextCard() {
        with(cards) {
            if (isNotEmpty()) {
                val cardToNotify = get(cardIndex)
                cardObservable.notify(cardToNotify)
                cardIndex = (cardIndex + 1) % size
            }
        }
    }
}