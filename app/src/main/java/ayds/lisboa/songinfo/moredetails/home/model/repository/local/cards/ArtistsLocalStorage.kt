package ayds.lisboa.songinfo.moredetails.home.model.repository.local.cards

import ayds.lisboa.songinfo.moredetails.home.model.entities.Card

interface LocalStorage {
    fun insertCards(cards: List<Card>)
    fun getCardsByName(term: String): List<Card>
}