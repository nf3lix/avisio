package com.avisio.dashboard.common.persistence

import androidx.lifecycle.LiveData
import androidx.room.*
import com.avisio.dashboard.common.data.model.card.Card

@Dao
interface CardDao {

    @Query("SELECT * FROM card")
    fun getAll(): LiveData<List<Card>>

    @Query("SELECT * FROM card WHERE boxId = :boxId")
    fun getCardsByBox(boxId: Int): LiveData<List<Card>>

    @Insert
    fun insert(card: Card)

    @Query("DELETE FROM card")
    fun deleteAll()

    @Delete
    fun deleteCard(card: Card)

    @Update
    fun updateCard(card: Card)

}