package ayds.lisboa.songinfo.moredetails.home.model

import android.content.Context
import ayds.lisboa.songinfo.moredetails.home.model.repository.CardRepository
import ayds.lisboa.songinfo.moredetails.home.model.repository.CardRepositoryImpl
import ayds.lisboa.lastfmdata.lastfm.LastFMInjector
import ayds.lisboa.lastfmdata.lastfm.LastFMService
import ayds.lisboa.songinfo.moredetails.home.model.repository.local.lastfm.LastFMLocalStorage
import ayds.lisboa.songinfo.moredetails.home.model.repository.local.lastfm.sqldb.CursorToCardMapperImpl
import ayds.lisboa.songinfo.moredetails.home.model.repository.local.lastfm.sqldb.LastFMLocalStorageImpl
import ayds.lisboa.songinfo.moredetails.home.view.MoreDetailsView

object MoreDetailsModelInjector {

    private lateinit var moreDetailsModel: MoreDetailsModel

    fun getMoreDetailsModel(): MoreDetailsModel = moreDetailsModel

    fun initMoreDetailsModel(moreDetailsView: MoreDetailsView) {
        val lastFMLocalStorage: LastFMLocalStorage = LastFMLocalStorageImpl(
            moreDetailsView as Context, CursorToCardMapperImpl()
        )
        val lastFMService: LastFMService = LastFMInjector.lastFMService
        val repository: CardRepository =
            CardRepositoryImpl(lastFMLocalStorage, lastFMService)
        moreDetailsModel = MoreDetailsModelImpl(repository)
    }

}