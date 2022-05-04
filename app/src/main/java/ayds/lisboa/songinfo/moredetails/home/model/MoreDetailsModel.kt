package ayds.lisboa.songinfo.moredetails.home.model

import ayds.lisboa.songinfo.moredetails.home.model.entities.Artist
import ayds.lisboa.songinfo.moredetails.home.model.repository.ArtistRepository
import ayds.observer.Observable
import ayds.observer.Subject

interface MoreDetailsModel {
    val artistObservable: Observable<Artist>
    fun searchArtist(name: String)
}

internal class MoreDetailsModelImpl(
    private val repository: ArtistRepository
) : MoreDetailsModel {

    override val artistObservable = Subject<Artist>()

    override fun searchArtist(name: String) {
        repository.getArtistByName(name).let {
            artistObservable.notify(it)
        }
    }
}