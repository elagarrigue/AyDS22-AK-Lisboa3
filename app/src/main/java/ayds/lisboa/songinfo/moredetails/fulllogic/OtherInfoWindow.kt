package ayds.lisboa.songinfo.moredetails.fulllogic

import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.os.Bundle
import ayds.lisboa.songinfo.R
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import ayds.lisboa.songinfo.moredetails.fulllogic.LastFMAPI
import ayds.lisboa.songinfo.moredetails.fulllogic.DataBase
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonElement
import ayds.lisboa.songinfo.moredetails.fulllogic.OtherInfoWindow
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

class OtherInfoWindow : AppCompatActivity() {
    private var textPane2: TextView? = null

    //private JPanel imagePanel;
    // private JLabel posterImageLabel;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)
        textPane2 = findViewById(R.id.textPane2)
        open(intent.getStringExtra("artistName"))
    }

    fun getArtistInfo(artistName: String?) {

        // create
        val retrofit = createRetrofit()

        val lastFMAPI = retrofit.create(LastFMAPI::class.java)

        Log.e("TAG", "artistName $artistName")

        Thread {

            var text = DataBase.getInfo(dataBase!!, artistName!!)
            if (text != null) { // exists in db
                text = "[*]$text"

            } else { // get from service
                val callResponse: Response<String>
                try {
                    callResponse = lastFMAPI.getArtistInfo(artistName).execute()
                    Log.e("TAG", "JSON " + callResponse.body())

                    val gson = Gson()
                    val jobj = gson.fromJson(callResponse.body(), JsonObject::class.java)
                    val artist = jobj["artist"].asJsonObject
                    val bio = artist["bio"].asJsonObject
                    val extract = bio["content"]
                    val url = artist["url"]

                    if (extract == null) {
                        text = "No Results"
                    } else {
                        text = extract.asString.replace("\\n", "\n")
                        text = textToHtml(text, artistName)


                        // save to DB  <o/
                        DataBase.saveArtist(dataBase!!, artistName, text)
                    }

                    openUrlButtonListener(url)

                } catch (e1: IOException) {
                    Log.e("TAG", "Error $e1")
                    e1.printStackTrace()
                }
            }
            imageLoaderLastfm(text)
        }.start()
    }

    /** Refactor1 **/
    private fun createRetrofit(): Retrofit {
         return Retrofit.Builder()
            .baseUrl("https://ws.audioscrobbler.com/2.0/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
    }

    /** Refactor2 **/
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
        val finalText = text
        runOnUiThread {
            Picasso.get().load(imageUrl).into(findViewById<View>(R.id.imageView) as ImageView)
            textPane2!!.text = Html.fromHtml(finalText)
        }
    }

    private var dataBase: DataBase? = null
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
            val textWithBold = text
                .replace("'", " ")
                .replace("\n", "<br>")
                .replace("(?i)" + term!!.toRegex(), "<b>" + term.toUpperCase() + "</b>")
            builder.append(textWithBold)
            builder.append("</font></div></html>")
            return builder.toString()
        }
    }

}