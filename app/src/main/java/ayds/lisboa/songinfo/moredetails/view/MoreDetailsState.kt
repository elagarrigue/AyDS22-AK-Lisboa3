package ayds.lisboa.songinfo.moredetails.view

import ayds.lisboa.songinfo.moredetails.model.entities.Source

data class MoreDetailsState (
    val artist: String = "",
    val bio: String = "",
    val url: String = "",
    val source: Source = Source.UNDEFINED,
    val imageUrl: String = ""

)