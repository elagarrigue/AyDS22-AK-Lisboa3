package ayds.lisboa.songinfo.moredetails.home.model.repository.local.lastfm

import ayds.lisboa.songinfo.moredetails.home.model.entities.LastFMCard

interface LastFMLocalStorage {
    fun insertCard(card: LastFMCard)

    fun getCardByName(term: String): LastFMCard?

}