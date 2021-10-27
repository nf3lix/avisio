package com.avisio.dashboard.common.data.database.converters

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.avisio.dashboard.common.data.model.card.CardAnswer
import com.avisio.dashboard.common.data.model.card.question.CardQuestion
import com.avisio.dashboard.common.data.model.card.CardType
import com.google.gson.Gson

@ProvidedTypeConverter
class CardConverter {

    @TypeConverter
    fun questionToString(question: CardQuestion): String {
        val gson = Gson()
        return gson.toJson(question).toString()
    }

    @TypeConverter
    fun stringToQuestion(serializedObject: String): CardQuestion {
        val gson = Gson()
        return gson.fromJson(serializedObject, CardQuestion::class.java)
    }

    @TypeConverter
    fun answerToString(answer: CardAnswer): String {
        val gson = Gson()
        return gson.toJson(answer).toString()
    }

    @TypeConverter
    fun stringToAnswer(serializedObject: String): CardAnswer {
        val gson = Gson()
        return gson.fromJson(serializedObject, CardAnswer::class.java)
    }


    @TypeConverter
    fun enumToInt(type: CardType): Int {
        return type.ordinal
    }

    @TypeConverter
    fun intToEnum(ordinal: Int): CardType {
        return CardType.values()[ordinal]
    }

}