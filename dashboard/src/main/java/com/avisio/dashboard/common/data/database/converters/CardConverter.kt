package com.avisio.dashboard.common.data.database.converters

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.avisio.dashboard.common.data.model.card.CardAnswer
import com.avisio.dashboard.common.data.model.card.CardQuestion
import com.google.gson.Gson

@ProvidedTypeConverter
class CardConverter {

    @TypeConverter
    fun cardToString(question: CardQuestion): String {
        val gson = Gson()
        return gson.toJson(question).toString()
    }

    @TypeConverter
    fun stringToCard(serializedObject: String): CardQuestion {
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

}