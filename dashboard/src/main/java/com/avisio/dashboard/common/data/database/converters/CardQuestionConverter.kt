package com.avisio.dashboard.common.data.database.converters

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.avisio.dashboard.common.data.model.card.CardQuestion
import com.google.gson.Gson

@ProvidedTypeConverter
class CardQuestionConverter {

    @TypeConverter
    fun objectToString(question: CardQuestion): String {
        val gson = Gson()
        return gson.toJson(question).toString()
    }

    @TypeConverter
    fun stringToObject(serializedObject: String): CardQuestion {
        val gson = Gson()
        return gson.fromJson(serializedObject, CardQuestion::class.java)
    }

}