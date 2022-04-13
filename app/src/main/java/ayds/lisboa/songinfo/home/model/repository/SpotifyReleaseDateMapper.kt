package ayds.lisboa.songinfo.home.model.repository

import ayds.lisboa.songinfo.home.model.entities.ReleaseDatePrecision
import java.lang.IllegalArgumentException

interface SpotifyReleaseDateMapper {
    fun mapReleaseDatePrecisionStringToEnum(releaseDatePrecisionString: String): ReleaseDatePrecision

}

internal class SpotifyReleaseDateMapperImpl: SpotifyReleaseDateMapper {

    override fun mapReleaseDatePrecisionStringToEnum(releaseDatePrecisionString: String): ReleaseDatePrecision {
        return try {
            ReleaseDatePrecision.valueOf(releaseDatePrecisionString.uppercase())
        } catch (exception: IllegalArgumentException) {
            ReleaseDatePrecision.UNDEFINED
        }
    }


}