package com.avisio.dashboard.common.persistence

import androidx.lifecycle.LiveData
import androidx.room.*
import com.avisio.dashboard.common.data.model.card.Card

@Dao
interface CardDao {

    @Query("SELECT * FROM card")
    fun getAllAsLiveData(): LiveData<List<Card>>

    @Query("SELECT * FROM card WHERE boxId = :boxId")
    fun getCardsLiveDataByBox(boxId: Long): LiveData<List<Card>>

    @Query("SELECT * FROM card")
    suspend fun getAll(): List<Card>

    @Query("SELECT * FROM card WHERE boxId = :boxId")
    suspend fun getCardsByBox(boxId: Long): List<Card>

    @Insert
    fun insert(card: Card)

    @Query("DELETE FROM card")
    fun deleteAll()

    @Delete
    fun deleteCard(card: Card)

    @Update
    fun updateCard(card: Card)

}