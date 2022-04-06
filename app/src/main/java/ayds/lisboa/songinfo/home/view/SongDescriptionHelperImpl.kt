package ayds.lisboa.songinfo.home.view

import ayds.lisboa.songinfo.home.model.entities.EmptySong
import ayds.lisboa.songinfo.home.model.entities.Song
import ayds.lisboa.songinfo.home.model.entities.SpotifySong

interface SongDescriptionHelper {
    fun getSongDescriptionText(song: Song = EmptySong): String
}

internal class SongDescriptionHelperImpl : SongDescriptionHelper {
    override fun getSongDescriptionText(song: Song): String {
        return when (song) {
            is SpotifySong ->
                "${
                    "Song: ${song.songName} " +
                            if (song.isLocallyStored) "[*]" else ""
                }\n" +
                        "Artist: ${song.artistName}\n" +
                        "Album: ${song.albumName}\n" +
                            if(isPresicionDay(song.releaseDate))
                                    "Release date: ${paraDia(song.releaseDate)}"
                            else
                               if(isPresicionMonth(song.releaseDate))
                                    "Release date: ${paraMes(song.releaseDate)}"
                               else
                                    if(esBisiesto(song.releaseDate.toInt()))
                                        "Release date: ${paraAnio(song.releaseDate)}" + "(a leap year)"
                                    else
                                        "Release date: ${paraAnio(song.releaseDate)}" + "(not a leap year)"

                       // "Year: ${song.year}"
            else -> "Song not found"
        }
    }
    private fun isPresicionDay(date: String): Boolean {
        return date=="day"
    }

    private fun isPresicionMonth(date: String): Boolean {
        return date=="month"
    }
    private fun paraDia(date: String): String {
        var arregloFecha = date.split("-")
        return arregloFecha[2] +"/"+ arregloFecha[1]+"/" +arregloFecha[0]
    }

    private fun paraMes(date: String): String {
        var arregloFecha = date.split("-")
        var Meses = arrayOf("January","February","March","April","May","June","July","August","September","October","November","December")
        var resultado: String= Meses[arregloFecha[1].toInt()]+", " +arregloFecha[0]

        return resultado
    }

    private fun esBisiesto(n: Int) = (n % 4 == 0) && (n % 100 != 0 || n % 400 == 0)

    private fun paraAnio(date: String): String {
        return date.split("-").first()
    }






}