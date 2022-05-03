package ayds.lisboa.songinfo.moredetails.home.controller

import ayds.lisboa.songinfo.moredetails.home.view.MoreDetailsEvent
import ayds.lisboa.songinfo.moredetails.home.view.MoreDetailsView
import ayds.observer.Observer


interface MoreDetailsController{

    fun setMoreDetailsView(moreDetailsView: MoreDetailsView)
}

internal class MoreDetailsControllerImpl(
    //private val moreDetailsModel: MoreDetailsModel
) : MoreDetailsController {

   private lateinit var moreDetailsView: MoreDetailsView
    override fun setMoreDetailsView(moreDetailsView: MoreDetailsView) {
        this.moreDetailsView = moreDetailsView
        //moreDetailsView.moreDetailsEventObservable.subscribe(observer)
    }

    private val observer: Observer<MoreDetailsView> =
        Observer { value ->
            when (value) {
                //MoreDetailsEvent.GetArtistInfo -> getInfoByArtistName()
            }
        }
    private fun getInfoByArtistName() {
        //moreDetailsView.getInfoByArtistName(moreDetailsView.moreDetailsState.artistName)
    }


}

