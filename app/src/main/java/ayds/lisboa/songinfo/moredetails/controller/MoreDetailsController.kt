package ayds.lisboa.songinfo.moredetails.controller

import ayds.lisboa.songinfo.moredetails.model.MoreDetailsModel
import ayds.lisboa.songinfo.moredetails.view.MoreDetailsEvent
import ayds.lisboa.songinfo.moredetails.view.MoreDetailsView
import ayds.observer.Observer


interface MoreDetailsController{
    fun setMoreDetailsView(moreDetailsView: MoreDetailsView)
}

internal class MoreDetailsControllerImpl(
    private val moreDetailsModel: MoreDetailsModel
) : MoreDetailsController {

   private lateinit var moreDetailsView: MoreDetailsView
    override fun setMoreDetailsView(moreDetailsView: MoreDetailsView) {
        this.moreDetailsView = moreDetailsView
        moreDetailsView.moreDetailsEventObservable.subscribe(observer)
    }

    private val observer: Observer<MoreDetailsEvent> =
        Observer { value ->
            when (value) {
                MoreDetailsEvent.SearchArtist -> searchArtist()
                MoreDetailsEvent.OpenURL -> openSongUrl()
                MoreDetailsEvent.NextCard -> nextCard()
            }
        }
    private fun searchArtist() {
        // Warning: Never use Thread in android! Use coroutines
        Thread {
            moreDetailsModel.searchCards(moreDetailsView.moreDetailsState.artist)
        }.start()
    }

    private fun openSongUrl() {
        moreDetailsView.openArticleLink(moreDetailsView.moreDetailsState.url)
    }

    private fun nextCard() {
        moreDetailsModel.nextCard()
    }
}

