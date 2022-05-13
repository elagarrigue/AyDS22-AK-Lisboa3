package ayds.lisboa.songinfo.moredetails.home.model.repository.external.lastfm

import ayds.lisboa.songinfo.moredetails.home.model.entities.LastFMArtist
import com.google.gson.Gson
import com.google.gson.JsonObject

interface LastFMToArtistResolver {
    fun getArtistFromExternalData(serviceData: String?): LastFMArtist?
}

private const val ARTIST = "artist"
private const val NAME = "name"
private const val URL = "url"
private const val BIO = "bio"
private const val BIO_CONTENT = "content"

internal class JsonToArtistResolver : LastFMToArtistResolver {
    override fun getArtistFromExternalData(serviceData: String?): LastFMArtist? =
        try {
            serviceData?.getArtistItem()?.let { item ->
                LastFMArtist(
                    item.getName(), item.getUrl(), item.getBioContent()
                )
            }
        } catch (e: Exception) {
            null
        }

    private fun String?.getArtistItem(): JsonObject {
        val jobj = Gson().fromJson(this, JsonObject::class.java)
        return jobj[ARTIST].asJsonObject
    }

    private fun JsonObject.getName(): String {
        return this[NAME].asString
    }

    private fun JsonObject.getUrl(): String {
        return this[URL].asString
    }

    private fun JsonObject.getBioContent(): String {
        val bio = this[BIO].asJsonObject
        return bio[BIO_CONTENT].asString
    }

}