package com.avisio.dashboard.common.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.avisio.dashboard.common.data.model.SMCardItem

@Dao
interface SMCardItemDao {

    @Insert
    fun insert(cardItem: SMCardItem)

    @Query("SELECT * FROM smcarditem WHERE cardId = :cardId")
    suspend fun getCardItem(cardId: Long): SMCardItem?

    @Update
    fun update(cardItem: SMCardItem)

    @Query("SELECT COUNT(*) FROM SMCardItem")
    fun getSize(): Int

}