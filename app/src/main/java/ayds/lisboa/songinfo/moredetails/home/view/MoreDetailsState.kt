package ayds.lisboa.songinfo.moredetails.home.view

import ayds.lisboa.songinfo.moredetails.home.model.entities.Source

data class MoreDetailsState (
    val artist: String = "",
    val bio: String = "",
    val url: String = "",
    val source: Source = Source.UNDEFINED,
    val imageUrl: String = ""

)