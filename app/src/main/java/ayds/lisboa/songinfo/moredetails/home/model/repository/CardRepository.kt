package ayds.lisboa.songinfo.moredetails.home.model.repository

import ayds.lisboa.songinfo.moredetails.home.model.entities.Card
import ayds.lisboa.songinfo.moredetails.home.model.entities.EmptyCard
import ayds.lisboa.songinfo.moredetails.home.model.entities.CardImpl
import ayds.lisboa.lastfmdata.lastfm.LastFMService
import ayds.lisboa.songinfo.moredetails.home.model.repository.local.lastfm.LastFMLocalStorage
import ayds.lisboa.lastfmdata.lastfm.entities.LastFMArtist as ExternalArtist

interface CardRepository {
    fun getCardByName(cardName: String): Card
}

internal class CardRepositoryImpl(
    private val lastFMLocalStorage: LastFMLocalStorage,
    private val lastFMService: LastFMService
) : CardRepository {

    override fun getCardByName(cardName: String): Card {
        var lastFMCard = lastFMLocalStorage.getCardByName(cardName)

        when {
            lastFMCard != null -> markArtistAsLocal(lastFMCard)
            else -> {
                try {
                    val externalArtist: ExternalArtist? = lastFMService.getArtist(cardName)
                    externalArtist?.let {
                        lastFMCard = adaptLastFMArtist(it)
                    }
                    lastFMCard?.let {
                        lastFMLocalStorage.insertCard(it)
                    }
                } catch (e: Exception) {
                    lastFMCard = null
                }
            }
        }

        return lastFMCard ?: EmptyCard
    }

    private fun markArtistAsLocal(artist: CardImpl) {
        artist.isLocallyStored = true
    }

    private fun adaptLastFMArtist(lastFMCard: ExternalArtist): CardImpl {
        return CardImpl(
            lastFMCard.name,
            lastFMCard.infoUrl,
            lastFMCard.description,
        )
    }

}