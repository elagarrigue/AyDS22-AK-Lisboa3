package ayds.lisboa.songinfo.home.view

import ayds.lisboa.songinfo.home.model.entities.EmptySong
import ayds.lisboa.songinfo.home.model.entities.Song

interface SongDescriptionDate {
    fun getReleaseDataByPrecision(song: Song = EmptySong): String
}

internal class SongDescriptionDateImpl : SongDescriptionDate {
    override fun getReleaseDataByPrecision(song: Song,): String {
        return when(song.releaseDatePrecision){
            "day" -> {
                getDateWithDayPrecision(song.releaseDate)
            }
            "month" -> {
                getDateWithMonthPrecision(song.releaseDate)
            }
            "year" -> {
                if(isLeapYear(song.releaseDate.toInt())){
                    getDateWithYearPrecision(song.releaseDate) + " (a leap year)"
                } else {getDateWithYearPrecision(song.releaseDate) + " (not a leap year)"}
            }

            else -> {
                "Date not found"
            }
        }
    }

    private fun getDateWithDayPrecision(date: String): String {
        val arrayDate = date.split("-")
        return arrayDate[2] +"/"+ arrayDate[1]+"/" +arrayDate[0]
    }

    private fun getDateWithMonthPrecision(date: String): String {
        val arrayDate = date.split("-")
        val months = arrayOf("January","February","March","April","May","June","July","August","September","October","November","December")

        return months[arrayDate[1].toInt()-1]+", " +arrayDate[0]
    }

    private fun isLeapYear(n: Int) = (n % 4 == 0) && (n % 100 != 0 || n % 400 == 0)

    private fun getDateWithYearPrecision(date: String): String {
        return date.split("-").first()
    }
}






