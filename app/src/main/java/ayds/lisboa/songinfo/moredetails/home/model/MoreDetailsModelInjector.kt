package ayds.lisboa.songinfo.moredetails.home.model

import android.content.Context
import ayds.lisboa.songinfo.moredetails.home.model.repository.CardRepository
import ayds.lisboa.songinfo.moredetails.home.model.repository.CardRepositoryImpl
import ayds.lisboa.songinfo.moredetails.home.model.repository.external.Broker
import ayds.lisboa.songinfo.moredetails.home.model.repository.external.BrokerImpl
import ayds.lisboa.songinfo.moredetails.home.model.repository.local.cards.LocalStorage
import ayds.lisboa.songinfo.moredetails.home.model.repository.local.cards.sqldb.CursorToCardMapperImpl
import ayds.lisboa.songinfo.moredetails.home.model.repository.local.cards.sqldb.LastFMLocalStorageImpl
import ayds.lisboa.songinfo.moredetails.home.view.MoreDetailsView

object MoreDetailsModelInjector {

    private lateinit var moreDetailsModel: MoreDetailsModel

    fun getMoreDetailsModel(): MoreDetailsModel = moreDetailsModel

    fun initMoreDetailsModel(moreDetailsView: MoreDetailsView) {
        val localStorage: LocalStorage = LastFMLocalStorageImpl(
            moreDetailsView as Context, CursorToCardMapperImpl()
        )
        val broker: Broker = BrokerImpl()
        val repository: CardRepository =
            CardRepositoryImpl(localStorage, broker)
        moreDetailsModel = MoreDetailsModelImpl(repository)
    }

}