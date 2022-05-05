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
import ayds.lisboa.songinfo.home.view.HomeViewInjector
import ayds.lisboa.songinfo.moredetails.home.model.MoreDetailsModel
import ayds.lisboa.songinfo.moredetails.home.model.MoreDetailsModelInjector
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

    private lateinit var imageView: ImageView
    private lateinit var lastFMAPI: LastFMAPI
    private lateinit var viewArticleButton: Button
    private lateinit var descriptionSongPane: TextView

    override val moreDetailsEventObservable: Observable<MoreDetailsEvent> = onActionSubject
    override var moreDetailsState: MoreDetailsState = MoreDetailsState()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)

        initModule()
        initViews()
        initArtistInfo()
        initLastFMAPI()
    }

    private fun initModule() {
        MoreDetailsViewInjector.init(this)
        moreDetailsModel = MoreDetailsModelInjector.getMoreDetailsModel()
    }

    private fun initViews() {
        descriptionSongPane = findViewById(R.id.textPane2)
        imageView = findViewById(R.id.imageView)
        viewArticleButton = findViewById(R.id.openUrlButton)
    }

    private fun initArtistInfo() {
        val artist = intent.getStringExtra(moreDetailsState.artistName) ?: ""
        // loadArtistInfo(artist)
    }

    private fun initLastFMAPI() {
        lastFMAPI = getLastFMAPI()
    }

    private fun notifyGetArtistInfo() {
        onActionSubject.notify(MoreDetailsEvent.GetArtistInfo)
    }

    private fun getLastFMAPI() = createRetrofit().create(LastFMAPI::class.java)

    private fun getIntent(urlString: String): Intent {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(urlString)
        return intent
    }

    private fun createRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(moreDetailsState.baseUrlRetrofit)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
    }

    private fun updateMoreDetailsInfo() {
        updateImageLoaderLastfm()
        updateDescriptionSongPane()
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

}

