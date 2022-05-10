package ayds.lisboa.songinfo.moredetails.home.view

import ayds.lisboa.songinfo.moredetails.home.controller.MoreDetailsControllerInjector
import ayds.lisboa.songinfo.moredetails.home.model.MoreDetailsModelInjector


object MoreDetailsViewInjector {

    val LastFMArtistBioParser: LastFMArtistBioParser = LastFMArtistBioParserImpl()

    fun init(moreDetailsView: MoreDetailsView) {
        MoreDetailsModelInjector.initMoreDetailsModel(moreDetailsView)
        MoreDetailsControllerInjector.onViewStarted(moreDetailsView)
    }
}