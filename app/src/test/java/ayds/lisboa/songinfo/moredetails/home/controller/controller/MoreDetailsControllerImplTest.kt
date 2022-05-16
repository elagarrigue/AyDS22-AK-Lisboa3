package ayds.lisboa.songinfo.moredetails.home.controller.controller

import ayds.lisboa.songinfo.moredetails.home.controller.MoreDetailsControllerImpl
import ayds.lisboa.songinfo.moredetails.home.model.MoreDetailsModel
import ayds.lisboa.songinfo.moredetails.home.view.MoreDetailsEvent
import ayds.lisboa.songinfo.moredetails.home.view.MoreDetailsState
import ayds.lisboa.songinfo.moredetails.home.view.MoreDetailsView
import ayds.observer.Subject
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test


class MoreDetailsControllerImplTest {

    private val moreDetailsModel: MoreDetailsModel = mockk(relaxUnitFun = true)

    private val onActionSubject = Subject<MoreDetailsEvent>()
    private val moreDetailsView: MoreDetailsView = mockk(relaxUnitFun = true) {
        every { moreDetailsEventObservable } returns onActionSubject
    }

    private val moreDetailsController by lazy {
        MoreDetailsControllerImpl(moreDetailsModel)
    }

    @Before
    fun setup() {
        moreDetailsController.setMoreDetailsView(moreDetailsView)
    }

    @Test
    fun `on search event should search artist info`() {
        every { moreDetailsView.moreDetailsState } returns MoreDetailsState(artist = "artist")

        onActionSubject.notify(MoreDetailsEvent.SearchArtist)

        verify { moreDetailsModel.searchArtist("artist") }
    }


    @Test
    fun `on open song url event should open external link`() {
        every { moreDetailsView.moreDetailsState } returns MoreDetailsState(url = "url")

        onActionSubject.notify(MoreDetailsEvent.OpenURL)

        verify { moreDetailsView.openArticleLink("url") }
    }
}