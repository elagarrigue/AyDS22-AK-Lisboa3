package ayds.lisboa.songinfo.moredetails.model.repository.external

import ayds.lisboa.songinfo.moredetails.model.entities.Card
import ayds.lisboa.songinfo.moredetails.model.entities.CardImpl
import ayds.lisboa.songinfo.moredetails.model.repository.external.proxies.ProxyService

interface Broker {
    fun getInfoByArtistName(name: String): List<Card>
}

internal class BrokerImpl(private val proxyList: List<ProxyService>) : Broker {

    private val infoList = mutableListOf<Card>()

    override fun getInfoByArtistName(name: String): List<Card> {
        proxyList.forEach {
            val card = it.getCard(name)
            insertCardFromProxy(card)
        }
        return infoList
    }

    private fun insertCardFromProxy(card: Card?) {
        if (card is CardImpl)
            this.infoList.add(card)
    }

}