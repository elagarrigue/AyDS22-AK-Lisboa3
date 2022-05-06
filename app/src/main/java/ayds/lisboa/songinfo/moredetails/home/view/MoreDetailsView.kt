package ayds.lisboa.songinfo.moredetails.home.view


import android.content.Intent
import android.net.Uri
import android.widget.TextView
import android.os.Bundle
import ayds.lisboa.songinfo.R
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import ayds.lisboa.songinfo.home.model.HomeModel
import ayds.lisboa.songinfo.home.model.HomeModelInjector
import ayds.lisboa.songinfo.home.model.entities.EmptySong
import ayds.lisboa.songinfo.home.model.entities.Song
import ayds.lisboa.songinfo.home.model.entities.SpotifySong
import ayds.lisboa.songinfo.home.view.HomeViewInjector
import ayds.lisboa.songinfo.home.view.SongDescriptionHelper
import ayds.lisboa.songinfo.moredetails.home.model.MoreDetailsModel
import ayds.lisboa.songinfo.moredetails.home.model.MoreDetailsModelInjector
import ayds.lisboa.songinfo.moredetails.home.model.entities.Artist
import ayds.lisboa.songinfo.moredetails.home.model.entities.EmptyArtist
import ayds.lisboa.songinfo.moredetails.home.model.entities.LastFMArtist
import ayds.lisboa.songinfo.moredetails.home.model.repository.external.lastfm.LastFMAPI
import ayds.observer.Observable
import ayds.observer.Subject
import com.squareup.picasso.Picasso
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.*


interface MoreDetailsView {

    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
    }

    val moreDetailsEventObservable: Observable<MoreDetailsEvent>
    var moreDetailsState: MoreDetailsState
}

class MoreDetailsViewActivity : AppCompatActivity(), MoreDetailsView {


    private val onActionSubject = Subject<MoreDetailsEvent>()
    private lateinit var moreDetailsModel: MoreDetailsModel
    private val lastFMArtistBioParser: LastFMArtistBioParser = MoreDetailsViewInjector.LastFMArtistBioParser

    private lateinit var imageView: ImageView
    private lateinit var viewArticleButton: Button
    private lateinit var descriptionSongPane: TextView

    override val moreDetailsEventObservable: Observable<MoreDetailsEvent> = onActionSubject
    override var moreDetailsState: MoreDetailsState = MoreDetailsState()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)
        initObservers()
        initModule()
        initProperties()
        initListeners()
    }
    private fun searchAction() {
        updateDisabledActionsState()
        notifySearchArtist()
    }

    private fun initListeners() {
        viewArticleButton.setOnClickListener {
            startActivity(getIntent(moreDetailsState.url)) //CAMBIAR
            searchAction()// VERIFICAR
        }
    }
    private fun getIntent(urlString: String): Intent {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(urlString)
        return intent
    }

    private fun initModule() {
        MoreDetailsViewInjector.init(this)
        moreDetailsModel = MoreDetailsModelInjector.getMoreDetailsModel()
    }

    private fun initProperties() {
        descriptionSongPane = findViewById(R.id.textPane2)
        imageView = findViewById(R.id.imageView)
        viewArticleButton = findViewById(R.id.openUrlButton)
    }

    private fun notifySearchArtist() {
        onActionSubject.notify(MoreDetailsEvent.SearchArtist)
    }

    private fun initObservers() {
        moreDetailsModel.artistObservable
            .subscribe { value -> updateMoreDetailsInfo(value) }
    }

    private fun updateMoreDetailsInfo(artist:Artist) {
        updateImageLoaderLastfm()
        updateDescriptionSongPane()
        updateMoreDetailsState(artist)
    }

    private fun updateImageLoaderLastfm() {
        runOnUiThread {
            Picasso.get().load(moreDetailsState.imageUrl).into(imageView)
        }
    }

    private fun updateDescriptionSongPane() {
        runOnUiThread {
            descriptionSongPane.text = HtmlCompat.fromHtml(
                moreDetailsState.songDescription,
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
        }
    }

    private fun updateDisabledActionsState() {
        moreDetailsState = moreDetailsState.copy(actionsEnabled = false)
    }

    private fun updateMoreDetailsState(artist: Artist) {
        when (artist) {
            is LastFMArtist -> updateSongMoreDetailsState(artist)
            EmptyArtist -> updateNoResultsMoreDetailsState(artist)
        }
    }

    private fun updateSongMoreDetailsState(artist: Artist) {
        moreDetailsState = moreDetailsState.copy(
            artist = artist.name,
            bio = artist.info,
            url = artist.url,
            actionsEnabled = true
        )
    }

    private fun updateNoResultsMoreDetailsState(artist: Artist) {
        moreDetailsState = moreDetailsState.copy(
            artist = "",
            bio = "",
            url = "",
            actionsEnabled = true
        )
    }

}

