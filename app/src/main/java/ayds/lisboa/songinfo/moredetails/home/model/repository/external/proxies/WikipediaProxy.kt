package ayds.lisboa.songinfo.moredetails.home.model.repository.external.proxies

import ayds.lisboa.songinfo.moredetails.home.model.entities.Card
import ayds.winchester2.wikipedia.ExternalRepository

internal class WikipediaProxy(private val wikipediaService: ExternalRepository): ProxyService {

    override fun getCard(artist: String): Card {
        TODO("Not yet implemented")
    }
}