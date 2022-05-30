package ayds.lisboa.songinfo.moredetails.home.model.repository

import ayds.lisboa.songinfo.moredetails.home.model.entities.Card
import ayds.lisboa.songinfo.moredetails.home.model.entities.EmptyCard
import ayds.lisboa.songinfo.moredetails.home.model.entities.LastFMCard
import ayds.lisboa.lastfmdata.lastfm.LastFMService
import ayds.lisboa.songinfo.moredetails.home.model.repository.local.lastfm.LastFMLocalStorage
import ayds.lisboa.lastfmdata.lastfm.entities.LastFMArtist as ExternalArtist

interface ArtistRepository {
    fun getArtistByName(artistName: String): Card
}

internal class ArtistRepositoryImpl(
    private val lastFMLocalStorage: LastFMLocalStorage,
    private val lastFMService: LastFMService
) : ArtistRepository {

    override fun getArtistByName(artistName: String): Card {
        var lastFMArtist = lastFMLocalStorage.getArtistByName(artistName)

        when {
            lastFMArtist != null -> markArtistAsLocal(lastFMArtist)
            else -> {
                try {
                    val externalArtist: ExternalArtist? = lastFMService.getArtist(artistName)
                    externalArtist?.let {
                        lastFMArtist = adaptLastFMArtist(it)
                    }
                    lastFMArtist?.let {
                        lastFMLocalStorage.insertArtist(it)
                    }
                } catch (e: Exception) {
                    lastFMArtist = null
                }
            }
        }

        return lastFMArtist ?: EmptyCard
    }

    private fun markArtistAsLocal(artist: LastFMCard) {
        artist.isLocallyStored = true
    }

    private fun adaptLastFMArtist(lastFMArtist: ExternalArtist): LastFMCard {
        return LastFMCard(
            lastFMArtist.name,
            lastFMArtist.infoUrl,
            lastFMArtist.description,
        )
    }

}