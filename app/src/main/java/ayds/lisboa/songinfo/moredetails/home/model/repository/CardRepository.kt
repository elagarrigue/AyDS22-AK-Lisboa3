package ayds.lisboa.songinfo.moredetails.home.model.repository

import ayds.lisboa.songinfo.moredetails.home.model.entities.Card
import ayds.lisboa.songinfo.moredetails.home.model.repository.external.Broker
import ayds.lisboa.songinfo.moredetails.home.model.repository.local.cards.LocalStorage

interface CardRepository {
    fun getCardsByName(cardName: String): List<Card>
}

internal class CardRepositoryImpl(
    private val localStorage: LocalStorage,
    private val infoServices: Broker,
) : CardRepository {

    override fun getCardsByName(cardName: String): List<Card> {
        var cardList = localStorage.getCardsByName(cardName)

        when {
            cardList != null -> markArtistAsLocal(cardList)
            else -> {

                val externalCards = infoServices.getInfoByArtistName(cardName)
                localStorage.insertCards(externalCards)

            }
        }
        return cardList
    }

    private fun markArtistAsLocal(cards: List<Card>) {
        cards.forEach {
            it.isLocallyStored = true
        }
    }

}