package ayds.lisboa.songinfo.moredetails.home.model.repository

import ayds.lisboa.songinfo.moredetails.home.model.entities.Artist
import ayds.lisboa.songinfo.moredetails.home.model.entities.EmptyArtist
import ayds.lisboa.songinfo.moredetails.home.model.entities.LastFMArtist
import ayds.lisboa.songinfo.moredetails.home.model.repository.external.lastfm.LastFMService
import ayds.lisboa.songinfo.moredetails.home.model.repository.local.lastfm.LastFMLocalStorage

interface ArtistRepository {
    fun getArtistByName(artistName: String): Artist
}

internal class ArtistRepositoryImpl(
    private val lastFMLocalStorage: LastFMLocalStorage,
    private val lastFMService: LastFMService
) : ArtistRepository {

    override fun getArtistByName(artistName: String): Artist {
        var lastFMArtist = lastFMLocalStorage.getArtistByName(artistName)

        when {
            lastFMArtist != null -> markArtistAsLocal(lastFMArtist)
            else -> {
                try {
                    lastFMArtist = lastFMService.getArtist(artistName)
                    lastFMArtist?.let {
                        lastFMLocalStorage.insertArtist(it)
                    }
                } catch (e: Exception) {
                    lastFMArtist = null
                }
            }
        }

        return lastFMArtist ?: EmptyArtist
    }

    private fun markArtistAsLocal(artist: LastFMArtist) {
        artist.isLocallyStored = true
    }

}