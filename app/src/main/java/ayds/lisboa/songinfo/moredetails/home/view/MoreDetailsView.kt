package ayds.lisboa.songinfo.moredetails.home.view

import android.widget.TextView
import android.os.Bundle
import ayds.lisboa.songinfo.R
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import ayds.lisboa.songinfo.moredetails.home.model.MoreDetailsModel
import ayds.lisboa.songinfo.moredetails.home.model.MoreDetailsModelInjector
import ayds.lisboa.songinfo.moredetails.home.model.entities.Artist
import ayds.lisboa.songinfo.moredetails.home.model.entities.EmptyArtist
import ayds.lisboa.songinfo.moredetails.home.model.entities.LastFMArtist
import ayds.lisboa.songinfo.utils.UtilsInjector.navigationUtils
import ayds.observer.Observable
import ayds.observer.Subject
import com.squareup.picasso.Picasso

interface MoreDetailsView {

    val moreDetailsEventObservable: Observable<MoreDetailsEvent>
    val moreDetailsState: MoreDetailsState

    fun openArticleLink(url: String)
}

class MoreDetailsViewActivity : AppCompatActivity(), MoreDetailsView {

    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
    }

    private val onActionSubject = Subject<MoreDetailsEvent>()
    private lateinit var moreDetailsModel: MoreDetailsModel

    private lateinit var imageView: ImageView
    private lateinit var viewArticleButton: Button
    private lateinit var descriptionSongPane: TextView
    private val lastFMArtistBioParser: LastFMArtistBioParser =
        MoreDetailsViewInjector.LastFMArtistBioParser
    override val moreDetailsEventObservable: Observable<MoreDetailsEvent> = onActionSubject
    override var moreDetailsState: MoreDetailsState = MoreDetailsState()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)

        initModule()
        initState()
        initProperties()
        initListeners()
        initObservers()
        searchAction()
    }

    private fun initModule() {
        MoreDetailsViewInjector.init(this)
        moreDetailsModel = MoreDetailsModelInjector.getMoreDetailsModel()
    }

    private fun initState() {
        val artistName = intent.getStringExtra(ARTIST_NAME_EXTRA) ?: ""
        moreDetailsState = moreDetailsState.copy(artist = artistName)
    }

    private fun initProperties() {
        descriptionSongPane = findViewById(R.id.textPane2)
        imageView = findViewById(R.id.imageView)
        viewArticleButton = findViewById(R.id.openUrlButton)
    }

    private fun initListeners() {
        viewArticleButton.setOnClickListener {
            notifyOpenURLAction()
        }
    }

    override fun openArticleLink(url: String) {
        navigationUtils.openExternalUrl(this, url)
    }

    private fun notifyOpenURLAction() {
        onActionSubject.notify(MoreDetailsEvent.OpenURL)
    }

    private fun initObservers() {
        moreDetailsModel.artistObservable
            .subscribe { value -> updateMoreDetailsInfo(value) }
    }

    private fun updateMoreDetailsInfo(artist: Artist) {
        updateMoreDetailsState(artist)
        updateDescriptionSongPane()
        updateImageLoaderLastfm()
    }

    private fun updateDescriptionSongPane() {
        runOnUiThread {
            descriptionSongPane.text =
                HtmlCompat.fromHtml(
                    moreDetailsState.bio, HtmlCompat.FROM_HTML_MODE_LEGACY
                )
        }
    }

    private fun updateImageLoaderLastfm() {
        runOnUiThread {
            Picasso.get().load(moreDetailsState.imageUrl).into(imageView)
        }
    }

    private fun updateMoreDetailsState(artist: Artist) {
        when (artist) {
            is LastFMArtist -> updateSongMoreDetailsState(artist)
            EmptyArtist -> updateNoResultsMoreDetailsState()
        }
    }

    private fun updateSongMoreDetailsState(artist: Artist) {
        moreDetailsState = moreDetailsState.copy(
            artist = artist.name,
            bio = lastFMArtistBioParser.parseArtistBioToDisplayableHtml(artist),
            url = artist.url,
            actionsEnabled = true
        )
    }

    private fun updateNoResultsMoreDetailsState() {
        moreDetailsState = moreDetailsState.copy(
            artist = "",
            bio = "",
            url = "",
            actionsEnabled = true
        )
    }

    private fun searchAction() {
        updateDisabledActionsState()
        notifySearchArtist()
    }

    private fun updateDisabledActionsState() {
        moreDetailsState = moreDetailsState.copy(actionsEnabled = false)
    }

    private fun notifySearchArtist() {
        onActionSubject.notify(MoreDetailsEvent.SearchArtist)
    }

}