package ayds.lisboa.songinfo.moredetails.home.model.repository.external.proxies

import ayds.lisboa.lastfmdata.lastfm.LastFMService
import ayds.lisboa.songinfo.moredetails.home.model.entities.Card

internal class LastFMProxy(private val lastFMService: LastFMService): ProxyService {


    override fun getCard(artist: String): Card {
        TODO("Not yet implemented")
    }
}