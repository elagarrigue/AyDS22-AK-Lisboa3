package ayds.lisboa.songinfo.moredetails.model.repository.external.proxies

import ayds.lisboa.songinfo.moredetails.model.entities.Card
import ayds.lisboa.songinfo.moredetails.model.entities.CardImpl
import ayds.lisboa.songinfo.moredetails.model.entities.Source
import ayds.ny3.newyorktimes.NytArticleService
import ayds.ny3.newyorktimes.NytArtistInfo

internal class NYTProxy(private val nytArticleService: NytArticleService) : ProxyService {

    override fun getCard(artist: String): Card? {

        return try {
            nytArticleService.getArtistInfo(artist)?.let { createCard(it) }
        } catch (e: Exception) {
            null
        }

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