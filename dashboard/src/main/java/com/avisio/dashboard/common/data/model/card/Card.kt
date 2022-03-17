package com.avisio.dashboard.common.data.model.card

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import com.avisio.dashboard.common.data.model.box.AvisioBox
import com.avisio.dashboard.common.data.model.card.question.CardQuestion
import java.util.*
import kotlin.collections.ArrayList

@Entity(
    tableName = "card",
    foreignKeys = [ForeignKey(
        entity = AvisioBox::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("boxId"),
        onDelete = CASCADE
    )]
)
data class Card(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,

    @ColumnInfo(name = "boxId")
    val boxId: Long? = null,

    @ColumnInfo(name = "create_date")
    val createDate: Date = Date(System.currentTimeMillis()),

    @ColumnInfo(name = "type")
    val type: CardType = CardType.STANDARD,

    @ColumnInfo(name = "question")
    val question: CardQuestion = CardQuestion(ArrayList()),

    @ColumnInfo(name = "answer")
    val answer: CardAnswer = CardAnswer(ArrayList()),

)