package ayds.lisboa.songinfo.moredetails.home.model.repository.local.cards

import ayds.lisboa.songinfo.moredetails.home.model.entities.CardImpl

interface LastFMLocalStorage {
    fun insertCard(card: CardImpl)

    fun getCardByName(term: String): CardImpl?

}