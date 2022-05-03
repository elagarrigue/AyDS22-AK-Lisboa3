package ayds.lisboa.songinfo.moredetails.fulllogic

import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.os.Bundle
import ayds.lisboa.songinfo.R
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonElement
import android.content.Intent
import android.net.Uri
import android.widget.Button
import android.widget.ImageView
import com.squareup.picasso.Picasso
import androidx.core.text.HtmlCompat
import com.google.gson.JsonNull
import retrofit2.Response
import java.lang.StringBuilder
import java.util.*

private const val ARTIST = "artist"
private const val ARTIST_NAME = "artistName"
private const val BIO = "bio"
private const val CONTENT = "content"
private const val URL = "url"
private const val NO_RESULTS = "No Results"
private const val PREFIX = "[*]"
private const val BASE_URL_RETROFIT = "https://ws.audioscrobbler.com/2.0/"
private const val IMAGE_URL =
    "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"

class OtherInfoWindow : AppCompatActivity() {

    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
    }

    private lateinit var imageView: ImageView
    private lateinit var lastFMAPI: LastFMAPI
    private lateinit var viewArticleButton: Button
    private lateinit var descriptionSongPane: TextView
    private lateinit var dataBase: DataBase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)

        initViews()
        initDataBase()
        initArtistInfo()
        initLastFMAPI()
    }

    private fun initViews(){
        descriptionSongPane = findViewById(R.id.textPane2)
        imageView = findViewById(R.id.imageView)
        viewArticleButton = findViewById(R.id.openUrlButton)
    }

    private fun initDataBase() {
        dataBase = DataBase(this)
    }

    private fun initArtistInfo() {
        val artist = intent.getStringExtra(ARTIST_NAME) ?: ""
        loadArtistInfo(artist)
    }

    private fun initLastFMAPI() {
        lastFMAPI = getLastFMAPI()
    }

    private fun loadArtistInfo(artistName: String) {

        Thread {
            val artistInfo = getInfoByArtistName(artistName)
            imageLoaderLastfm()
            updateDescriptionSongPane(artistInfo)
        }.start()
    }

    private fun getInfoByArtistName(artistName: String): String {

        var textArtistInfo: String? = dataBase.getArtistInfo(artistName)

        when {
            textArtistInfo != null -> "$PREFIX$textArtistInfo"
            else -> {
                textArtistInfo = getInfoFromService(artistName)

                textArtistInfo?.let {
                    saveArtistInDatabase(artistName, textArtistInfo)
                }
            }
        }
        return textArtistInfo ?: NO_RESULTS
    }

    private fun getInfoFromService(artistName: String): String? {

        val jobj = getJsonInfo(artistName)
        val artistBio = getBiography(jobj)
        setUrlButtonListener(getUrl(jobj))

        return if (artistBio is JsonNull)
            null
        else parseArtistBio(artistBio, artistName)
    }

    private fun saveArtistInDatabase(artistName: String, textArtistInfo: String?) {
        dataBase.saveArtist(artistName, textArtistInfo)
    }

    private fun getJsonInfo(artistName: String): JsonObject {
        val gson = Gson()
        return gson.fromJson(getCallResponse(artistName).body(), JsonObject::class.java)
    }

    private fun getCallResponse(artistName: String): Response<String> {
        return lastFMAPI.getArtistInfo(artistName).execute()
    }

    private fun getLastFMAPI() = createRetrofit().create(LastFMAPI::class.java)

    private fun createRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL_RETROFIT)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
    }

    private fun getBiography(jobj: JsonObject): JsonElement {
        val artistBio = getArtist(jobj)[BIO].asJsonObject
        return artistBio[CONTENT]
    }

    private fun getArtist(jobj: JsonObject): JsonObject = jobj[ARTIST].asJsonObject

    private fun parseArtistBio(artistBio: JsonElement, artistName: String): String {
        val textArtistInfo = artistBio.asString.replace("\\n", "\n")
        return textToHtml(textArtistInfo, artistName)
    }

    private fun getUrl(jobj: JsonObject): String {
        val url = getArtist(jobj)[URL]
        return url.asString
     }

    private fun setUrlButtonListener(url: String) {
        viewArticleButton.setOnClickListener {
            startActivity(getIntent(url))
        }
    }

    private fun getIntent(urlString: String): Intent {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(urlString)
        return intent
    }

    private fun imageLoaderLastfm() {
        Picasso.get().load(IMAGE_URL).into(imageView)
    }

    private fun updateDescriptionSongPane(text: String) {
        runOnUiThread {
            descriptionSongPane.text = HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_LEGACY)
        }
    }

    private fun textToHtml(text: String, term: String): String {

        val builder = StringBuilder()
        builder.apply {
            append("<html><div width=400>")
            append("<font face=\"arial\">")
        }
        val textWithBold = getTextWithBold(text, term)
        builder.apply {
            append(textWithBold)
            append("</font></div></html>")
        }
        return builder.toString()
    }

    private fun getTextWithBold(text: String, term: String): String {
        return text
            .replace("'", " ")
            .replace("\n", "<br>")
            .replace(
                "(?i)" + term.toRegex(),
                "<b>" + term.uppercase(Locale.getDefault()) + "</b>"
            )
    }

}