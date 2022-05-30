package ayds.lisboa.songinfo.moredetails.home.model

import ayds.lisboa.songinfo.moredetails.home.model.entities.Card
import ayds.lisboa.songinfo.moredetails.home.model.repository.ArtistRepository
import ayds.observer.Observable
import ayds.observer.Subject

interface MoreDetailsModel {
    val cardObservable: Observable<Card>
    fun searchArtist(name: String)
}

internal class MoreDetailsModelImpl(
    private val repository: ArtistRepository
) : MoreDetailsModel {

    override val cardObservable = Subject<Card>()

    override fun searchArtist(name: String) {
        repository.getArtistByName(name).let {
            cardObservable.notify(it)
        }
    }
}