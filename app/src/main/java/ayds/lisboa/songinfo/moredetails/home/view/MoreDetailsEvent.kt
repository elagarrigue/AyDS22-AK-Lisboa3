package ayds.lisboa.songinfo.moredetails.home.view

sealed class MoreDetailsEvent {
    object SearchArtist : MoreDetailsEvent()
    object OpenURL: MoreDetailsEvent()
    object NextCard: MoreDetailsEvent()
}