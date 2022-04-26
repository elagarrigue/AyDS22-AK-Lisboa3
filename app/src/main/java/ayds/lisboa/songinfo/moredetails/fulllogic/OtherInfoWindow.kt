package ayds.lisboa.songinfo.moredetails.fulllogic

import android.annotation.SuppressLint
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
import com.squareup.picasso.Picasso
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.ImageView
import retrofit2.Response
import java.io.IOException
import java.lang.StringBuilder
import java.util.*

private const val ARTIST = "artist"
private const val ARTIST_NAME = "artistName"
private const val BIO = "bio"
private const val CONTENT = "content"
private const val URL = "url"

class OtherInfoWindow : AppCompatActivity() {

    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
    }

    private var descriptionSongPane: TextView? = null
    private var dataBase: DataBase? = null
    private val imageUrl =
        "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)
        descriptionSongPane = findViewById(R.id.textPane2)
        open(intent.getStringExtra(ARTIST_NAME))
    }

    private fun open(artist: String?) {
        dataBase = DataBase(this)
        getArtistInfo(artist)
    }

    private fun getArtistInfo(artistName: String?) {

        Thread {
            var textArtistInfo = getInfoFromDatabase(artistName)

            textArtistInfo =
                if (isInDataBase(textArtistInfo))
                    "[*]$textArtistInfo"
                else getInfoFromService(artistName)

            imageLoaderLastfm(textArtistInfo)
        }.start()
    }

    private fun isInDataBase(textArtistInfo: String?): Boolean = textArtistInfo != null

    private fun getInfoFromDatabase(artistName: String?) = dataBase?.getArtistInfo(artistName!!)

    private fun getInfoFromService(artistName: String?): String {

        var textArtistInfo = ""

        try {
            val jobj = getJsonInfo(artistName)
            val artistBio = getBiography(jobj)

            if (!existBiography(artistBio)) {
                textArtistInfo = "No Results"
            } else {
                textArtistInfo = setArtistBio(artistBio, artistName)
                dataBase?.saveArtist(artistName, textArtistInfo)
            }

            openUrlButtonListener(getUrl(jobj))

        } catch (e1: IOException) {
            Log.e("TAG", "Error $e1")
            e1.printStackTrace()
        }

        return textArtistInfo
    }

    private fun getJsonInfo(artistName: String?): JsonObject {
        val gson = Gson()
        return gson.fromJson(getCallResponse(artistName).body(), JsonObject::class.java)
    }

    private fun getCallResponse(artistName: String?): Response<String> {
        return getLastFMAPI().getArtistInfo(artistName).execute()
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

        findViewById<View>(R.id.openUrlButton).setOnClickListener {
            startActivity(getIntent(urlString))
        }
    }

    private fun getIntent(urlString: String): Intent {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(urlString)
        return intent
    }

    @SuppressLint("NewApi")
    private fun imageLoaderLastfm(text: String?) {
        runOnUiThread {
            Picasso.get().load(imageUrl).into(findViewById<View>(R.id.imageView) as ImageView)
            descriptionSongPane!!.text = Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY)
        }
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
            .replace("(?i)" + term!!.toRegex(),
                "<b>" + term.uppercase(Locale.getDefault()) + "</b>")
    }

}