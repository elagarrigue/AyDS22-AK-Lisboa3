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
import com.squareup.picasso.Picasso
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.ImageView
import retrofit2.Response
import java.io.IOException
import java.lang.StringBuilder
import java.util.*

private const val ARTIST ="artist"
private const val ARTIST_NAME ="artistName"
private const val BIO ="bio"
private const val CONTENT ="content"
private const val URL ="url"


class OtherInfoWindow : AppCompatActivity() {

    private var descriptionSongPane: TextView? = null
    private var dataBase: DataBase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)
        descriptionSongPane = findViewById(R.id.textPane2)
        open(intent.getStringExtra(ARTIST_NAME))
    }

    private fun getArtistInfo(artistName: String?) {

        val retrofit = createRetrofit()
        val lastFMAPI = retrofit.create(LastFMAPI::class.java)

        Log.e("TAG", "artistName $artistName")

        Thread {

            var textArtistInfo = DataBase.getInfo(dataBase!!, artistName!!)

            if (textArtistInfo != null) {
                textArtistInfo = "[*]$textArtistInfo"

            } else {
                val callResponse: Response<String>
                try {
                    callResponse = lastFMAPI.getArtistInfo(artistName).execute()
                    Log.e("TAG", "JSON " + callResponse.body())

                    val gson = Gson()
                    val jobj = gson.fromJson(callResponse.body(), JsonObject::class.java)
                    val artist = jobj[ARTIST].asJsonObject
                    val bio = artist[BIO].asJsonObject
                    val extract = bio[CONTENT]
                    val url = artist[URL]

                    if (extract == null) {
                        textArtistInfo = "No Results"
                    } else {
                        textArtistInfo = extract.asString.replace("\\n", "\n")
                        textArtistInfo = textToHtml(textArtistInfo, artistName)

                        DataBase.saveArtist(dataBase!!, artistName, textArtistInfo)
                    }

                    openUrlButtonListener(url)

                } catch (e1: IOException) {
                    Log.e("TAG", "Error $e1")
                    e1.printStackTrace()
                }
            }
            imageLoaderLastfm(textArtistInfo)
        }.start()
    }

    private fun createRetrofit(): Retrofit {
         return Retrofit.Builder()
            .baseUrl("https://ws.audioscrobbler.com/2.0/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
    }

    private fun openUrlButtonListener(url: JsonElement){
        val urlString = url.asString
        findViewById<View>(R.id.openUrlButton).setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(urlString)
            startActivity(intent)
        }
    }

    private fun imageLoaderLastfm(text: String?){
        val imageUrl =
            "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"
        Log.e("TAG", "Get Image from $imageUrl")
        runOnUiThread {
            Picasso.get().load(imageUrl).into(findViewById<View>(R.id.imageView) as ImageView)
            descriptionSongPane!!.text = Html.fromHtml(text)
        }
    }

    private fun open(artist: String?) {
        dataBase = DataBase(this)
        DataBase.saveArtist(dataBase!!, "test", "sarasa")
        Log.e("TAG", "" + DataBase.getInfo(dataBase!!, "test"))
        Log.e("TAG", "" + DataBase.getInfo(dataBase!!, "nada"))
        getArtistInfo(artist)
    }

    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"

        fun textToHtml(text: String, term: String?): String {

            val builder = StringBuilder()
            builder.append("<html><div width=400>")
            builder.append("<font face=\"arial\">")

            var auxText = text
            val textWithBold = getTextWithBold(auxText,term)

            builder.append(textWithBold)
            builder.append("</font></div></html>")

            return builder.toString()
        }

        private fun getTextWithBold(text: String, term: String?): String {
            return text
                .replace("'", " ")
                .replace("\n", "<br>")
                .replace("(?i)" + term!!.toRegex(), "<b>" + term.uppercase(Locale.getDefault()) + "</b>")
        }
    }
}