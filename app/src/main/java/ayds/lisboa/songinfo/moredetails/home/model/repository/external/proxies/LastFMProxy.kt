package ayds.lisboa.songinfo.moredetails.home.model.repository.external.proxies

import ayds.lisboa.lastfmdata.lastfm.LastFMService
import ayds.lisboa.lastfmdata.lastfm.entities.LastFMArtist
import ayds.lisboa.songinfo.moredetails.home.model.entities.Card
import ayds.lisboa.songinfo.moredetails.home.model.entities.CardImpl
import ayds.lisboa.songinfo.moredetails.home.model.entities.EmptyCard
import ayds.lisboa.songinfo.moredetails.home.model.entities.Source

internal class LastFMProxy(private val lastFMService: LastFMService) : ProxyService {

    override fun getCard(artist: String): Card {

        var artistCard: CardImpl? = null

        try {
            val serviceInfo: LastFMArtist? = lastFMService.getArtist(artist)
            serviceInfo?.let {
                artistCard = createCard(it)
            }

        } catch (e: Exception) {
            artistCard = null
        }

        return artistCard ?: EmptyCard
    }

    private fun createCard(serviceInfo: LastFMArtist) =
        CardImpl(
            serviceInfo.name,
            serviceInfo.infoUrl,
            serviceInfo.description,
            false,
            Source.LASTFM,
            serviceInfo.sourceLogoUrl
        )

}