package ayds.lisboa.songinfo.moredetails.model

import android.content.Context
import ayds.lisboa.lastfmdata.lastfm.*
import ayds.lisboa.songinfo.moredetails.model.repository.CardRepository
import ayds.lisboa.songinfo.moredetails.model.repository.CardRepositoryImpl
import ayds.lisboa.songinfo.moredetails.model.repository.external.Broker
import ayds.lisboa.songinfo.moredetails.model.repository.external.BrokerImpl
import ayds.lisboa.songinfo.moredetails.model.repository.external.proxies.LastFMProxy
import ayds.lisboa.songinfo.moredetails.model.repository.external.proxies.NYTProxy
import ayds.lisboa.songinfo.moredetails.model.repository.external.proxies.ProxyService
import ayds.lisboa.songinfo.moredetails.model.repository.external.proxies.WikipediaProxy
import ayds.lisboa.songinfo.moredetails.model.repository.local.cards.LocalStorage
import ayds.lisboa.songinfo.moredetails.model.repository.local.cards.sqldb.CursorToCardMapperImpl
import ayds.lisboa.songinfo.moredetails.model.repository.local.cards.sqldb.LastFMLocalStorageImpl
import ayds.lisboa.songinfo.moredetails.view.MoreDetailsView
import ayds.ny3.newyorktimes.NytInjector
import ayds.winchester2.wikipedia.WikipediaInjector
import java.util.*

object MoreDetailsModelInjector {

    private lateinit var moreDetailsModel: MoreDetailsModel

    fun getMoreDetailsModel(): MoreDetailsModel = moreDetailsModel

    fun initMoreDetailsModel(moreDetailsView: MoreDetailsView) {
        val localStorage: LocalStorage = LastFMLocalStorageImpl(moreDetailsView as Context, CursorToCardMapperImpl())
        val proxyServiceList = initProxyServiceList()
        val broker: Broker = BrokerImpl(proxyServiceList)
        val repository: CardRepository = CardRepositoryImpl(localStorage, broker)
        moreDetailsModel = MoreDetailsModelImpl(repository)
    }

    private fun initProxyServiceList(): LinkedList<ProxyService>{
        val proxyList: LinkedList<ProxyService> = LinkedList<ProxyService>()

        val lastFMProxy :ProxyService = LastFMProxy(LastFMInjector.lastFMService)
        proxyList.add(lastFMProxy)

        val nytProxy: ProxyService = NYTProxy(NytInjector.nytArticleService)
        proxyList.add(nytProxy)

        val wikipediaProxy: ProxyService = WikipediaProxy(WikipediaInjector.wikipediaService)
        proxyList.add(wikipediaProxy)

        return proxyList
    }

}