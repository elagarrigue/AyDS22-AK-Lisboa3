package ayds.lisboa.songinfo.moredetails.home.view

class MoreDetailsState (
    val artist: String = "artist",
    val artistName: String = "artistName",
    val bio: String = "bio",
    val content: String = "content",
    val url: String = "url",
    val songDescription: String = "",
    val imageUrl: String = DEFAULT_IMAGE,
    val baseUrlRetrofit: String = BASE_URL_RETROFIT

){

    companion object {
        const val BASE_URL_RETROFIT = "https://ws.audioscrobbler.com/2.0/"
        const val DEFAULT_IMAGE ="https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"
    }
}