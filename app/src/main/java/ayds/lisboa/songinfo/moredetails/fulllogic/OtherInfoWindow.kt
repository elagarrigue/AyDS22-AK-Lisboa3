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
import android.widget.ImageView
import com.squareup.picasso.Picasso
import androidx.core.text.HtmlCompat
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


class OtherInfoWindow : AppCompatActivity() {

    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
    }

    private lateinit var imageView: ImageView
    private lateinit var lastFMAPI: LastFMAPI
    private lateinit var view: TextView
    private lateinit var descriptionSongPane: TextView
    private lateinit var dataBase: DataBase
    private val imageUrl =
        "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)
        initDescriptionSongPane()
        initDataBase()
        initArtistInfo()
        initImageView()
        initLastFMAPI()
        initView()
    }

    private fun initImageView() {
        imageView = findViewById(R.id.imageView)
    }

    private fun initLastFMAPI() {
        lastFMAPI = getLastFMAPI()
    }

    private fun initView() {
        view = findViewById(R.id.openUrlButton)
    }

    private fun initArtistInfo() {
        val artist = intent.getStringExtra(ARTIST_NAME)
        getArtistInfo(artist)
    }

    private fun initDescriptionSongPane() {
        descriptionSongPane = findViewById(R.id.textPane2)
    }

    private fun initDataBase() {
        dataBase = DataBase(this)
    }

    private fun getArtistInfo(artistName: String?) {

        Thread {
            val databaseInfo = getInfoFromDataBaseOrService(artistName)
            imageLoaderLastfm(databaseInfo)
        }.start()
    }

    private fun getInfoFromDataBaseOrService(artistName: String?): String {

        val textArtistInfo = getInfoFromDatabase(artistName)

        return if (isInDataBase(textArtistInfo))
            "$PREFIX$textArtistInfo"
        else getInfoFromService(artistName)

    }

    private fun isInDataBase(textArtistInfo: String?): Boolean = textArtistInfo != null

    private fun getInfoFromDatabase(artistName: String?) = dataBase.getArtistInfo(artistName!!)

    private fun getInfoFromService(artistName: String?): String {

        val jobj = getJsonInfo(artistName)
        val artistBio = getBiography(jobj)
        openUrlButtonListener(getUrl(jobj))

        return getTextArtistInfo(artistBio, artistName)

    }

    private fun getTextArtistInfo(artistBio: JsonElement, artistName: String?): String {

        val textArtistInfo: String

        if (!existBiography(artistBio)) {
            textArtistInfo = NO_RESULTS
        } else {
            textArtistInfo = setArtistBio(artistBio, artistName)
            saveArtistInDatabase(artistName,textArtistInfo)
        }
        return textArtistInfo
    }

    private fun saveArtistInDatabase(artistName: String?,textArtistInfo: String?) {
        dataBase.saveArtist(artistName, textArtistInfo)
    }

    private fun getJsonInfo(artistName: String?): JsonObject {
        val gson = Gson()
        return gson.fromJson(getCallResponse(artistName).body(), JsonObject::class.java)
    }

    private fun getCallResponse(artistName: String?): Response<String> {
        return lastFMAPI.getArtistInfo(artistName).execute()
    }

    private fun getLastFMAPI() = createRetrofit().create(LastFMAPI::class.java)

    private fun createRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://ws.audioscrobbler.com/2.0/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
    }

    private fun getBiography(jobj: JsonObject): JsonElement {
        val artistBio = getArtist(jobj)[BIO].asJsonObject
        return artistBio[CONTENT]
    }

    private fun getArtist(jobj: JsonObject): JsonObject = jobj[ARTIST].asJsonObject

    private fun setArtistBio(artistBio: JsonElement, artistName: String?): String {
        val textArtistInfo = artistBio.asString.replace("\\n", "\n")
        return textToHtml(textArtistInfo, artistName)
    }

    private fun getUrl(jobj: JsonObject): JsonElement = getArtist(jobj)[URL]

    private fun existBiography(artistBio: JsonElement?): Boolean = artistBio != null

    private fun openUrlButtonListener(url: JsonElement) {
        val urlString = url.asString

        view.setOnClickListener {
            startActivity(getIntent(urlString))
        }
    }

    private fun getIntent(urlString: String): Intent {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(urlString)
        return intent
    }

    private fun imageLoaderLastfm(text: String?) {
        runOnUiThread {
            Picasso.get().load(imageUrl).into(imageView)
            updateDescriptionSongPane(text)
        }
    }

    private fun updateDescriptionSongPane(text: String?) {
        descriptionSongPane.text = HtmlCompat.fromHtml(text!!, HtmlCompat.FROM_HTML_MODE_LEGACY)
    }

    private fun textToHtml(text: String, term: String?): String {

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

    private fun getTextWithBold(text: String, term: String?): String {
        return text
            .replace("'", " ")
            .replace("\n", "<br>")
            .replace(
                "(?i)" + term!!.toRegex(),
                "<b>" + term.uppercase(Locale.getDefault()) + "</b>"
            )
    }

}