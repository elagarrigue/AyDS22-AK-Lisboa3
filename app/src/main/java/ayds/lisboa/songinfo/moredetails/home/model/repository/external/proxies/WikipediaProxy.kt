package ayds.lisboa.songinfo.moredetails.home.model.repository.external.proxies

import ayds.lisboa.songinfo.moredetails.home.model.entities.Card
import ayds.lisboa.songinfo.moredetails.home.model.entities.CardImpl
import ayds.lisboa.songinfo.moredetails.home.model.entities.Source
import ayds.winchester2.wikipedia.ExternalRepository
import ayds.winchester2.wikipedia.WikipediaArticle

internal class WikipediaProxy(private val wikipediaService: ExternalRepository) : ProxyService {

    lateinit var artist: String

    override fun getCard(artist: String): Card? {

        this.artist = artist

        return try {
            wikipediaService.getArtistDescription(artist)?.let { createCard(it) }
        } catch (e: Exception) {
            null
        }
    }

    private fun createCard(serviceInfo: WikipediaArticle) =
        CardImpl(
            artist,
            serviceInfo.source,
            serviceInfo.description,
            false,
            Source.WIKIPEDIA,
            serviceInfo.sourceLogo
        )
}