package ayds.lisboa.songinfo.moredetails.home.model

import android.content.Context
import ayds.lisboa.lastfmdata.lastfm.*
import ayds.lisboa.songinfo.moredetails.home.model.repository.CardRepository
import ayds.lisboa.songinfo.moredetails.home.model.repository.CardRepositoryImpl
import ayds.lisboa.songinfo.moredetails.home.model.repository.external.Broker
import ayds.lisboa.songinfo.moredetails.home.model.repository.external.BrokerImpl
import ayds.lisboa.songinfo.moredetails.home.model.repository.external.proxies.LastFMProxy
import ayds.lisboa.songinfo.moredetails.home.model.repository.external.proxies.NYTProxy
import ayds.lisboa.songinfo.moredetails.home.model.repository.external.proxies.ProxyService
import ayds.lisboa.songinfo.moredetails.home.model.repository.external.proxies.WikipediaProxy
import ayds.lisboa.songinfo.moredetails.home.model.repository.local.cards.LocalStorage
import ayds.lisboa.songinfo.moredetails.home.model.repository.local.cards.sqldb.CursorToCardMapperImpl
import ayds.lisboa.songinfo.moredetails.home.model.repository.local.cards.sqldb.LastFMLocalStorageImpl
import ayds.lisboa.songinfo.moredetails.home.view.MoreDetailsView
import ayds.ny3.newyorktimes.NytArticleService
import ayds.ny3.newyorktimes.NytInjector
import ayds.winchester2.wikipedia.ExternalRepository
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

        initLastFMProxy(proxyList)
        initNytProxy(proxyList)
        initWikipediaProxy(proxyList)

        return proxyList
    }

    private fun initLastFMProxy(proxyList: LinkedList<ProxyService>){
        val lastFMInjector = LastFMInjector
        val lastFMService: LastFMService = lastFMInjector.lastFMService
        val lastFMProxy :ProxyService = LastFMProxy(lastFMService)
        proxyList.add(lastFMProxy)
    }

    private fun initNytProxy(proxyList: LinkedList<ProxyService>){
        val nytInjector = NytInjector
        val nytArticleService: NytArticleService = nytInjector.nytArticleService
        val nytProxy: ProxyService = NYTProxy(nytArticleService)
        proxyList.add(nytProxy)
    }

    private fun initWikipediaProxy(proxyList: LinkedList<ProxyService>){
        val wikipediaInjector = WikipediaInjector
        val wikipediaService: ExternalRepository = wikipediaInjector.wikipediaService
        val wikipediaProxy: ProxyService = WikipediaProxy(wikipediaService)
        proxyList.add(wikipediaProxy)
    }

}