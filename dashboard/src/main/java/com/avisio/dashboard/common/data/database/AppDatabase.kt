package com.avisio.dashboard.common.data.database

import android.content.Context
import androidx.room.*
import com.avisio.dashboard.common.data.database.converters.BoxIconConverter
import com.avisio.dashboard.common.data.database.converters.CardConverter
import com.avisio.dashboard.common.data.database.converters.DateTimeConverter
import com.avisio.dashboard.common.data.database.converters.ForgettingCurveConverter
import com.avisio.dashboard.common.data.model.sm.SMCardItem
import com.avisio.dashboard.common.data.model.box.AvisioBox
import com.avisio.dashboard.common.data.model.box.AvisioFolder
import com.avisio.dashboard.common.data.model.card.Card
import com.avisio.dashboard.common.persistence.box.AvisioBoxDao
import com.avisio.dashboard.common.persistence.CardDao
import com.avisio.dashboard.common.persistence.ForgettingCurveDao
import com.avisio.dashboard.common.persistence.SMCardItemDao
import com.avisio.dashboard.common.data.model.sm.ForgettingCurveEntity

@Database(
    version = 1,
    exportSchema = false,
    entities = [
        AvisioBox::class,
        AvisioFolder::class,
        Card::class,
        ForgettingCurveEntity::class,
        SMCardItem::class
    ]
)
@TypeConverters(
    DateTimeConverter::class,
    BoxIconConverter::class,
    CardConverter::class,
    ForgettingCurveConverter::class,
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun boxDao(): AvisioBoxDao
    abstract fun cardDao(): CardDao
    abstract fun forgettingCurveDao(): ForgettingCurveDao
    abstract fun smCardItemDao(): SMCardItemDao

    companion object {

        private const val DB_NAME = "app_database"

        @Volatile private var instance: AppDatabase? = null
        private val lock = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(lock) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DB_NAME)
                .addTypeConverter(DateTimeConverter())
                .addTypeConverter(BoxIconConverter())
                .addTypeConverter(CardConverter())
                .addTypeConverter(ForgettingCurveConverter())
                .build()
        }

    }

}