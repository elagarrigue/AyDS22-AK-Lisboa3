package ayds.lisboa.songinfo.moredetails.model.repository.local.cards

import ayds.lisboa.songinfo.moredetails.model.entities.Card

interface LocalStorage {
    fun insertCards(cards: List<Card>)
    fun getCardsByName(term: String): List<Card>
}