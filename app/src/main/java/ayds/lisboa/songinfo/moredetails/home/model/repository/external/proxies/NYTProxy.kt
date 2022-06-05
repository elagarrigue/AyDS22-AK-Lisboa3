package ayds.lisboa.songinfo.moredetails.home.model.repository.external.proxies

import ayds.lisboa.songinfo.moredetails.home.model.entities.Card
import ayds.lisboa.songinfo.moredetails.home.model.entities.CardImpl
import ayds.lisboa.songinfo.moredetails.home.model.entities.EmptyCard
import ayds.lisboa.songinfo.moredetails.home.model.entities.Source
import ayds.ny3.newyorktimes.NytArticleService
import ayds.ny3.newyorktimes.NytArtistInfo

internal class NYTProxy(private val nytArticleService: NytArticleService) : ProxyService {

    override fun getCard(artist: String): Card {

        var artistCard: CardImpl? = null

        try {
            val serviceInfo: NytArtistInfo? = nytArticleService.getArtistInfo(artist)
            serviceInfo?.let {
                artistCard = createCard(it)
            }

        } catch (e: Exception) {
            artistCard = null
        }

        return artistCard ?: EmptyCard
    }

    private fun createCard(serviceInfo: NytArtistInfo) =
        CardImpl(
            serviceInfo.artistName,
            serviceInfo.artistURL,
            serviceInfo.artistInfo,
            false,
            Source.NEW_YORK_TIMES,
            serviceInfo.nytLogo
        )
}