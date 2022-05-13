package ayds.lisboa.songinfo.moredetails.home.model.repository.local.lastfm

import ayds.lisboa.songinfo.moredetails.home.model.entities.LastFMArtist

interface LastFMLocalStorage {
    fun insertArtist(artist: LastFMArtist)

    fun getArtistByName(term: String): LastFMArtist?

}