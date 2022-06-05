package ayds.lisboa.songinfo.moredetails.home.model.repository.external.proxies

import ayds.lisboa.songinfo.moredetails.home.model.entities.Card

interface ProxyService {
    fun getCard(artist:String): Card
}