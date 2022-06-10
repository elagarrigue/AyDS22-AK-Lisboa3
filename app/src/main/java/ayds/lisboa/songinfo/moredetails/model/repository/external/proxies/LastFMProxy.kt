package ayds.lisboa.songinfo.moredetails.model.repository.external.proxies

import ayds.lisboa.lastfmdata.lastfm.LastFMService
import ayds.lisboa.lastfmdata.lastfm.entities.LastFMArtist
import ayds.lisboa.songinfo.moredetails.model.entities.Card
import ayds.lisboa.songinfo.moredetails.model.entities.CardImpl
import ayds.lisboa.songinfo.moredetails.model.entities.Source

internal class LastFMProxy(private val lastFMService: LastFMService) : ProxyService {

    override fun getCard(artist: String): Card? {
        return try {
            lastFMService.getArtist(artist)?.let { createCard(it) }
        } catch (e: Exception) {
            null
        }
    }

    private fun createCard(serviceInfo: LastFMArtist): CardImpl {
        return CardImpl(
            serviceInfo.name,
            serviceInfo.infoUrl,
            serviceInfo.description,
            false,
            Source.LASTFM,
            serviceInfo.sourceLogoUrl
        )
    }
}