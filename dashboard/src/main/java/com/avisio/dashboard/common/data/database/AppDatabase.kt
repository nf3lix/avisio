package com.avisio.dashboard.common.data.database

import android.content.Context
import androidx.room.*
import com.avisio.dashboard.common.data.database.converters.BoxIconConverter
import com.avisio.dashboard.common.data.database.converters.CardQuestionConverter
import com.avisio.dashboard.common.data.database.converters.DateTimeConverter
import com.avisio.dashboard.common.data.model.box.AvisioBox
import com.avisio.dashboard.common.persistence.AvisioBoxDao

@Database(
    version = 2,
    exportSchema = false,
    entities = [
        AvisioBox::class
    ]
)
@TypeConverters(
    DateTimeConverter::class,
    BoxIconConverter::class,
    CardQuestionConverter::class
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun boxDao(): AvisioBoxDao

    companion object {

        private const val DB_NAME = "app_database"

        @Volatile private var instance: AppDatabase? = null
        private val lock = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(lock) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, AppDatabase::class.java, DB_NAME)
                .addTypeConverter(DateTimeConverter())
                .addTypeConverter(BoxIconConverter())
                .addTypeConverter(CardQuestionConverter())
                .build()

    }

}