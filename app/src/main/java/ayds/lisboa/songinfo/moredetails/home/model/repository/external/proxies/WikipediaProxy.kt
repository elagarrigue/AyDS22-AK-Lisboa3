package ayds.lisboa.songinfo.moredetails.home.model.repository.external.proxies

import ayds.lisboa.songinfo.moredetails.home.model.entities.Card
import ayds.lisboa.songinfo.moredetails.home.model.entities.CardImpl
import ayds.lisboa.songinfo.moredetails.home.model.entities.EmptyCard
import ayds.lisboa.songinfo.moredetails.home.model.entities.Source
import ayds.winchester2.wikipedia.ExternalRepository
import ayds.winchester2.wikipedia.WikipediaArticle

internal class WikipediaProxy(private val wikipediaService: ExternalRepository): ProxyService {

    lateinit var artist: String

    override fun getCard(artist: String): Card {

        this.artist = artist
        var artistCard: CardImpl? = null

        try {
            val serviceInfo: WikipediaArticle? = wikipediaService.getArtistDescription(artist)
            serviceInfo?.let {
                artistCard = createCard(it)
            }

        } catch (e: Exception) {
            artistCard = null
        }

        return artistCard ?: EmptyCard
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