package ayds.lisboa.songinfo.moredetails.home.model.repository.external.lastfm

import ayds.lisboa.songinfo.moredetails.home.model.entities.Artist
import ayds.lisboa.songinfo.moredetails.home.model.entities.LastFMArtist
import retrofit2.Response

interface LastFMService {

    fun getArtist(artistName: String): Artist?
}

class LastFMServiceImpl(
    private val lastFMAPI: LastFMAPI,
    private val lastFMToArtistResolver: LastFMToArtistResolver,
): LastFMService {

    override fun getArtist(artistName: String): LastFMArtist? {
        val callResponse = getArtistFromService(artistName)
        return lastFMToArtistResolver.getArtistFromExternalData(callResponse.body())
    }

    private fun getArtistFromService(artistName: String): Response<String> {
        return lastFMAPI.getArtistInfo(artistName).execute()
    }

}