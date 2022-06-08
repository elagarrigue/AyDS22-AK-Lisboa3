package ayds.lisboa.songinfo.moredetails.home.model

import ayds.lisboa.songinfo.moredetails.home.model.entities.Card
import ayds.lisboa.songinfo.moredetails.home.model.entities.EmptyCard
import ayds.lisboa.songinfo.moredetails.home.model.repository.CardRepository
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

    private var cards: Iterator<Card> = listOf<Card>().iterator()

    override fun searchCards(name: String) {
        repository.getCardsByName(name).let {
            cards = it.iterator()
            nextCard()
        }
    }

    override fun nextCard() {
        cardObservable.notify(
            if (cards.hasNext())
                cards.next()
            else
                EmptyCard
        )
    }
}