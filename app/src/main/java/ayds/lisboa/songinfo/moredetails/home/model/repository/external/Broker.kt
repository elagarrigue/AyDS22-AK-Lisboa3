package ayds.lisboa.songinfo.moredetails.home.model.repository.external

import ayds.lisboa.songinfo.moredetails.home.model.entities.Card

interface Broker {
    fun getInfoByArtistName(name: String): List<Card>
}