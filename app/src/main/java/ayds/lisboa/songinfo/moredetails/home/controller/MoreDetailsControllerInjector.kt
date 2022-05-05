package ayds.lisboa.songinfo.moredetails.home.controller

import ayds.lisboa.songinfo.home.controller.HomeControllerImpl
import ayds.lisboa.songinfo.home.model.HomeModelInjector
import ayds.lisboa.songinfo.moredetails.home.model.MoreDetailsModelInjector
import ayds.lisboa.songinfo.moredetails.home.view.MoreDetailsView


object MoreDetailsControllerInjector {

    fun onViewStarted(moreDetailsView: MoreDetailsView) {
        MoreDetailsControllerImpl(MoreDetailsModelInjector.getMoreDetailsModel()).apply {
            setMoreDetailsView(moreDetailsView)
        }
    }
}