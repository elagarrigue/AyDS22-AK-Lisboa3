package ayds.lisboa.songinfo.moredetails.home.model.repository.external

import ayds.lisboa.songinfo.moredetails.home.model.entities.Card
import ayds.lisboa.songinfo.moredetails.home.model.entities.CardImpl
import ayds.lisboa.songinfo.moredetails.home.model.repository.external.proxies.ProxyService
import java.util.*

interface Broker {
    fun getInfoByArtistName(name: String): List<Card>
}

internal class BrokerImpl(private var proxyList: LinkedList<ProxyService>) : Broker {

    private var infoList = LinkedList<CardImpl>()

    override fun getInfoByArtistName(name: String): List<CardImpl> {

        var card: Card
        proxyList.forEach {
            card = it.getCard(name)
            insertCardFromProxy(card)
        }
        return infoList
    }

    private fun insertCardFromProxy(card: Card) {
        if (card is CardImpl)
            this.infoList.add(card)
    }

}