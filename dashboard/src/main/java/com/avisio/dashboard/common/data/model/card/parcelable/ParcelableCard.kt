package com.avisio.dashboard.common.data.model.card.parcelable

import android.os.Parcel
import android.os.Parcelable
import com.avisio.dashboard.common.data.database.converters.CardConverter
import com.avisio.dashboard.common.data.model.card.Card
import com.avisio.dashboard.common.data.model.card.CardAnswer
import com.avisio.dashboard.common.data.model.card.CardQuestion
import com.avisio.dashboard.common.data.model.card.CardType
import java.util.*

private fun Parcel.writeDate(date: Date?) {
    writeLong(date?.time ?: -1)
}

private fun Parcel.readDate(): Date? {
    val time = readLong()
    return if(time != -1L) Date(time) else null
}

private fun Parcel.writeQuestion(question: CardQuestion) {
    writeString(CardConverter().questionToString(question))
}

private fun Parcel.readQuestion(): CardQuestion {
    val serializedString = readString()
    return CardConverter().stringToQuestion(serializedString!!)
}

private fun Parcel.writeAnswer(answer: CardAnswer) {
    writeString(CardConverter().answerToString(answer))
}

private fun Parcel.readAnswer(): CardAnswer {
    val serializedString = readString()
    return CardConverter().stringToAnswer(serializedString!!)
}

data class ParcelableCard(
    val id: Long = 0,
    val boxId: Long = 0,
    val createDate: Date = Date(System.currentTimeMillis()),
    val type: CardType = CardType.STANDARD,
    val question: CardQuestion = CardQuestion(arrayListOf()),
    val answer: CardAnswer = CardAnswer(arrayListOf())
): Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readLong(),
        parcel.readDate()!!,
        CardType.values()[parcel.readInt()],
        parcel.readQuestion(),
        parcel.readAnswer()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) = with(parcel) {
        writeLong(id)
        writeLong(boxId)
        writeDate(createDate)
        writeInt(type.ordinal)
        writeQuestion(question)
        writeAnswer(answer)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ParcelableCard> {

        fun createFromEntity(card: Card): ParcelableCard {
            return ParcelableCard(card.id, card.boxId, card.createDate, card.type, card.question, card.answer)
        }

        fun createBlank(): ParcelableCard {
            return ParcelableCard()
        }

        override fun createFromParcel(parcel: Parcel): ParcelableCard {
            return ParcelableCard(parcel)
        }

        override fun newArray(size: Int): Array<ParcelableCard?> {
            return arrayOfNulls(size)
        }
    }

}