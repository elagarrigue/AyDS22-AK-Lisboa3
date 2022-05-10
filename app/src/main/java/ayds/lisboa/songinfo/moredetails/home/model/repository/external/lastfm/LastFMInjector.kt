package ayds.lisboa.songinfo.moredetails.home.model.repository.external.lastfm

import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

object LastFMInjector {

    private const val LASTFM_URL = "https://ws.audioscrobbler.com/2.0/"
    private val lastFMAPIRetrofit = Retrofit.Builder()
        .baseUrl(LASTFM_URL)
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()
    private val lastFMAPI = lastFMAPIRetrofit.create(LastFMAPI::class.java)
    private val lastFMToArtistResolver: LastFMToArtistResolver = JsonToArtistResolver()

    val lastFMService: LastFMService = LastFMServiceImpl(
        lastFMAPI,
        lastFMToArtistResolver,
    )
}