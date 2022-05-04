package ayds.lisboa.songinfo.moredetails.home.model.repository

import ayds.lisboa.songinfo.moredetails.home.model.entities.Artist
import ayds.lisboa.songinfo.moredetails.home.model.entities.EmptyArtist
import ayds.lisboa.songinfo.moredetails.home.model.entities.LastFMArtist
import ayds.lisboa.songinfo.moredetails.home.model.repository.external.lastfm.LastFMService
import ayds.lisboa.songinfo.moredetails.home.model.repository.local.lastfm.LastFMLocalStorage

interface ArtistRepository {
    fun getArtistByName(term: String): Artist
}

internal class ArtistRepositoryImpl(
    private val lastFMLocalStorage: LastFMLocalStorage,
    private val lastFMService: LastFMService
) : ArtistRepository {

    override fun getArtistByName(term: String): Artist {
        var lastFMArtist = lastFMLocalStorage.getArtistByName(term)

        when {
            lastFMArtist != null -> markArtistAsLocal(lastFMArtist)
            else -> {
                try {
                    lastFMArtist = lastFMService.getArtist(term)
                    /*
                    lastFMArtist?.let {
                        when {
                            it.isSavedArtist() -> lastFMLocalStorage.updateArtistTerm(term, it.id)
                            else -> lastFMLocalStorage.insertArtist(term, it)
                        }
                    }
                    */
                } catch (e: Exception) {
                    lastFMArtist = null
                }
            }
        }

        return lastFMArtist ?: EmptyArtist
    }

    //private fun LastFMArtist.isSavedArtist() = lastFMLocalStorage.getArtistById(id)

    private fun markArtistAsLocal(artist: LastFMArtist) {
        artist.isLocallyStored = true
    }

}