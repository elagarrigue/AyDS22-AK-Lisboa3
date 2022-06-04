package ayds.lisboa.songinfo.moredetails.home.model.repository.local.lastfm

import ayds.lisboa.songinfo.moredetails.home.model.entities.CardImpl

interface LastFMLocalStorage {
    fun insertCard(card: CardImpl)

    fun getCardByName(term: String): CardImpl?

}