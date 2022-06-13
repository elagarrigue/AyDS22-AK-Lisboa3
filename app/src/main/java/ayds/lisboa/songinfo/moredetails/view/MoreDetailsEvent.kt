package ayds.lisboa.songinfo.moredetails.view

sealed class MoreDetailsEvent {
    object SearchArtist : MoreDetailsEvent()
    object OpenURL: MoreDetailsEvent()
    object NextCard: MoreDetailsEvent()
}