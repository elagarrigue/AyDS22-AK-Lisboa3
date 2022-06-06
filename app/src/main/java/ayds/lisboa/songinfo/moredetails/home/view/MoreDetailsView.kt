package ayds.lisboa.songinfo.moredetails.home.view

import android.os.Bundle
import android.widget.*
import ayds.lisboa.songinfo.R
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import ayds.lisboa.songinfo.moredetails.home.model.MoreDetailsModel
import ayds.lisboa.songinfo.moredetails.home.model.MoreDetailsModelInjector
import ayds.lisboa.songinfo.moredetails.home.model.entities.Card
import ayds.lisboa.songinfo.moredetails.home.model.entities.EmptyCard
import ayds.lisboa.songinfo.moredetails.home.model.entities.CardImpl
import ayds.lisboa.songinfo.utils.UtilsInjector.navigationUtils
import ayds.observer.Observable
import ayds.observer.Subject
import com.squareup.picasso.Picasso

interface MoreDetailsView {

    val moreDetailsEventObservable: Observable<MoreDetailsEvent>
    var moreDetailsState: MoreDetailsState

    fun openArticleLink(url: String)
}

class MoreDetailsViewActivity : AppCompatActivity(), MoreDetailsView {

    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
    }

    private val onActionSubject = Subject<MoreDetailsEvent>()
    private lateinit var moreDetailsModel: MoreDetailsModel

    private lateinit var next: Button

    private lateinit var cards: List<Card>

    private lateinit var sourceActual: TextView

    private lateinit var descriptionSongPane: TextView
    private lateinit var imageView: ImageView
    private lateinit var cardActual: Card

    private lateinit var viewArticleButton: Button

    private val lastFMArtistBioParser: ArtistBioParser =
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


        sourceActual =
            findViewById(R.id.source)

        descriptionSongPane =
            findViewById(R.id.textPane)

        imageView =
            findViewById(R.id.imageView)

        viewArticleButton =
            findViewById(R.id.openUrlButton)

        next =
            findViewById(R.id.button)

    }

    private fun initListeners() {
        viewArticleButton.setOnClickListener {
            notifyOpenURLAction()
        }
        next.setOnClickListener {
            updateNextService()
            updateMoreDetailsInfo(cardActual)
        }

    }

    override fun openArticleLink(url: String) {
        navigationUtils.openExternalUrl(this, url)
    }

    private fun updateNextService() {
        cardActual = if (cardActual != cards.last()) {
            val index = cards.indexOf(cardActual)
            cards[index + 1]
        } else
            cards.first()
    }

    private fun notifyOpenURLAction() {
        onActionSubject.notify(MoreDetailsEvent.OpenURL)
    }

    private fun initObservers() {
        moreDetailsModel.cardObservable
            .subscribe { value -> initMoreDetailsInfo(value) }
    }

    private fun initMoreDetailsInfo(cards: List<Card>) {
        this.cards = cards
        cardActual = cards.first()
        updateMoreDetailsInfo(cardActual)
    }

    private fun updateMoreDetailsInfo(card: Card) {
        updateMoreDetailsState(card)
        updateSourceLabel()
        updateDescriptionSongPane()
        updateImageLoaderLastfm()
    }

    private fun updateSourceLabel() {
        runOnUiThread {
            sourceActual.text = HtmlCompat.fromHtml(moreDetailsState.source.description,
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
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
            bio = lastFMArtistBioParser.parseArtistBioToDisplayableHtml(card),
            url = card.infoUrl,
            source = card.source,
            imageUrl = card.sourceLogoUrl
        )

    }

    private fun updateNoResultsMoreDetailsState() {

        moreDetailsState = moreDetailsState.copy(
            artist = "",
            bio = "",
            url = "",
            imageUrl = ""
        )

    }

    private fun searchAction() {
        notifySearchArtist()
    }


    private fun notifySearchArtist() {
        onActionSubject.notify(MoreDetailsEvent.SearchArtist)
    }

}