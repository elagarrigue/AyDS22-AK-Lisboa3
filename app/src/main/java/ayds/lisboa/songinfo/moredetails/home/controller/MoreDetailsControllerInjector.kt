package ayds.lisboa.songinfo.moredetails.home.controller

import ayds.lisboa.songinfo.moredetails.home.view.MoreDetailsView


object MoreDetailsControllerInjector {

    fun onViewStarted(moreDetailsView: MoreDetailsView) {
        HomeControllerImpl(HomeModelInjector.getHomeModel()).apply {
            setMoreDetailsView(moreDetailsView)
        }
    }
}