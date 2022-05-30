package ayds.lisboa.songinfo.moredetails.home.model.repository.local.lastfm

import ayds.lisboa.songinfo.moredetails.home.model.entities.LastFMCard

interface LastFMLocalStorage {
    fun insertArtist(artist: LastFMCard)

    fun getArtistByName(term: String): LastFMCard?

}