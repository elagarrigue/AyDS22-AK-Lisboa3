package ayds.lisboa.songinfo.moredetails.home.model.repository.local.cards.sqldb

import android.database.Cursor
import android.database.SQLException
import ayds.lisboa.songinfo.moredetails.home.model.entities.Card
import ayds.lisboa.songinfo.moredetails.home.model.entities.CardImpl
import ayds.lisboa.songinfo.moredetails.home.model.entities.Source

interface CursorToCardMapper {

    fun map(cursor: Cursor): List<Card>
}

internal class CursorToCardMapperImpl : CursorToCardMapper {

    override fun map(cursor: Cursor): List<Card> {
        val cards = mutableListOf<Card>()
        try {
            with(cursor) {
                while (moveToNext()) {
                    cards.add(getCard())
                }
            }
        } catch (e: SQLException) {
        }
        return cards
    }

    private fun Cursor.getCard() = CardImpl(
        name = getString(getColumnIndexOrThrow(NAME_COLUMN)),
        infoUrl = getString(getColumnIndexOrThrow(URL_COLUMN)),
        description = getString(getColumnIndexOrThrow(DESCRIPTION_COLUMN)),
        source = Source.values()[getInt(
            getColumnIndexOrThrow(
                SOURCE_COLUMN
            )
        )],
        sourceLogoUrl = getString(getColumnIndexOrThrow(LOGO_COLUMN))
    )
}