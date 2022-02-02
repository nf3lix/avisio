package com.avisio.dashboard.common.persistence

import androidx.room.*
import com.avisio.dashboard.common.data.model.sm.SMCardItem

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

    @Query("DELETE FROM smcarditem")
    fun deleteAll()

}