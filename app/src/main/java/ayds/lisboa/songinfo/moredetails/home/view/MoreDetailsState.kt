package ayds.lisboa.songinfo.moredetails.home.view

data class MoreDetailsState (
    val artist: String = "",
    val bio: String = "",
    val url: String = "",
    val actionsEnabled: Boolean = false,
    val imageUrl: String = DEFAULT_IMAGE,

){

    companion object {
        const val DEFAULT_IMAGE ="https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"
    }
}