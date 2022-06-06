package ayds.lisboa.songinfo.moredetails.home.view

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import ayds.lisboa.songinfo.R
import ayds.lisboa.songinfo.moredetails.home.model.MoreDetailsModel
import ayds.lisboa.songinfo.moredetails.home.model.MoreDetailsModelInjector
import ayds.lisboa.songinfo.moredetails.home.model.entities.Card
import ayds.lisboa.songinfo.moredetails.home.model.entities.CardImpl
import ayds.lisboa.songinfo.moredetails.home.model.entities.EmptyCard
import ayds.lisboa.songinfo.moredetails.home.model.entities.Source
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

    private lateinit var sourceActual: TextView
    private lateinit var imageView: ImageView
    private lateinit var viewArticleButton: Button
    private lateinit var descriptionSongPane: TextView
    private val artistBioParser: ArtistBioParser =
        MoreDetailsViewInjector.artistBioParser
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
        sourceActual = findViewById(R.id.sourceActual)
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
        moreDetailsModel.cardObservable
            .subscribe { value -> updateMoreDetailsInfo(value) }
    }

    private fun updateMoreDetailsInfo(card: Card) {
        updateMoreDetailsState(card)
        updateSourceLabel(card)
        updateDescriptionSongPane()
        updateImageLoaderLastfm()
    }

    private fun updateSourceLabel(card: Card) {
        when(card.source) {
            Source.WIKIPEDIA -> {
                sourceActual.text=HtmlCompat.fromHtml(
                    "Wikipedia", HtmlCompat.FROM_HTML_MODE_LEGACY
                )
            }
            Source.LASTFM  -> {
                sourceActual.text=HtmlCompat.fromHtml(
                    "LastFm", HtmlCompat.FROM_HTML_MODE_LEGACY
                )
            }
            Source.NEW_YORK_TIMES  -> {
                sourceActual.text=HtmlCompat.fromHtml(
                    "New York Times", HtmlCompat.FROM_HTML_MODE_LEGACY
                )
            }
            else -> {
                sourceActual.text=HtmlCompat.fromHtml(
                    "Indefinido", HtmlCompat.FROM_HTML_MODE_LEGACY
                )
            }
        }
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

    private fun updateMoreDetailsState(card: Card) {
        when (card) {
            is CardImpl -> updateSongMoreDetailsState(card)
            EmptyCard -> updateNoResultsMoreDetailsState()
        }
    }

    private fun updateSongMoreDetailsState(card: Card) {
        moreDetailsState = moreDetailsState.copy(
            artist = card.name,
            bio = artistBioParser.parseArtistBioToDisplayableHtml(card),
            url = card.infoUrl,
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