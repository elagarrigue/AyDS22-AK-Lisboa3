package ayds.lisboa.songinfo.home.view

import ayds.lisboa.songinfo.home.model.entities.EmptySong
import ayds.lisboa.songinfo.home.model.entities.ReleaseDatePrecision
import ayds.lisboa.songinfo.home.model.entities.Song

interface SongDescriptionDate {
    fun getReleaseDataByPrecision(song: Song = EmptySong): String
}

internal class SongDescriptionDateImpl : SongDescriptionDate {

    private val months = arrayOf("January","February","March","April","May","June","July","August",
        "September","October","November","December")

    override fun getReleaseDataByPrecision(song: Song): String {
        return when(song.releaseDatePrecision){
             ReleaseDatePrecision.DAY -> {
                 getDateWithDayPrecision(song.releaseDate)
             }
             ReleaseDatePrecision.MONTH -> {
                 getDateWithMonthPrecision(song.releaseDate)
             }
             ReleaseDatePrecision.YEAR -> {
                 getDateWithYearPrecision(song)
             }

            else -> {
                "Date not found"
            }
        }
    }

    private fun getDateWithDayPrecision(date: String): String {
        val arrayDate = date.split("-")
        val day = arrayDate[2]
        val month = arrayDate[1]
        val year = arrayDate[0]

        return "$day/$month/$year"
    }

    private fun getDateWithMonthPrecision(date: String): String {
        val arrayDate = date.split("-")
        val month = months[arrayDate[1].toInt()-1]
        val year = arrayDate[0]

        return "$month, $year"
    }

    private fun getDateWithYearPrecision(song: Song): String {
        return if (isLeapYear(song.releaseDate.toInt())) {
            getYear(song.releaseDate) + " (a leap year)"
        } else {
            getYear(song.releaseDate) + " (not a leap year)"
        }
    }

    private fun isLeapYear(n: Int) = (n % 4 == 0) && (n % 100 != 0 || n % 400 == 0)

    private fun getYear(date: String): String {
        return date.split("-").first()
    }
}
