package ayds.lisboa.songinfo.moredetails.home.model.repository.external.proxies

import ayds.lisboa.songinfo.moredetails.home.model.entities.Card
import ayds.ny3.newyorktimes.NytArticleService

internal class NYTProxy(nytArticleService: NytArticleService): ProxyService {
    override fun getCard(artist: String): Card {
        TODO("Not yet implemented")
    }
}