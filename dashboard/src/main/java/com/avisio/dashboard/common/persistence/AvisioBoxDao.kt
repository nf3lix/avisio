package com.avisio.dashboard.common.persistence

import androidx.lifecycle.LiveData
import androidx.room.*
import com.avisio.dashboard.common.data.model.AvisioBox

@Dao
interface AvisioBoxDao {

    @Query("SELECT * FROM box")
    fun getBoxList(): LiveData<List<AvisioBox>>

    @Insert
    fun insertBox(box: AvisioBox)

    @Delete
    fun deleteBox(box: AvisioBox)

    @Query("DELETE FROM box")
    fun deleteAll()

    @Query("UPDATE box SET name = :name, icon = :iconId WHERE id = :boxId")
    fun updateBox(boxId: Long, name: String, iconId: Int)

}