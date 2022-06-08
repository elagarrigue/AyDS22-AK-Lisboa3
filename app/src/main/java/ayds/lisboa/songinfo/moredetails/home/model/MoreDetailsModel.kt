package ayds.lisboa.songinfo.moredetails.home.model

import ayds.lisboa.songinfo.moredetails.home.model.entities.Card
import ayds.lisboa.songinfo.moredetails.home.model.repository.CardRepository
import ayds.observer.Observable
import ayds.observer.Subject

interface MoreDetailsModel {
    val cardObservable: Observable<List<Card>>
    fun searchCards(name: String)
}

internal class MoreDetailsModelImpl(
    private val repository: CardRepository
) : MoreDetailsModel {

    override val cardObservable = Subject<List<Card>>()

    override fun searchCards(name: String) {
        repository.getCardsByName(name).let {
            cardObservable.notify(it)
        }
    }
}