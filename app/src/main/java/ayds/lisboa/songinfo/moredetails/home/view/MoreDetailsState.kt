package ayds.lisboa.songinfo.moredetails.home.view

data class MoreDetailsState (
    val artist: String = "artist",
    val bio: String = "bio",
    val url: String = "url",
    val actionsEnabled: Boolean = false,
    val songDescription: String = "",
    val imageUrl: String = DEFAULT_IMAGE,

){

    companion object {
        const val DEFAULT_IMAGE ="https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"
    }
}