package com.avisio.dashboard.common.persistence

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.avisio.dashboard.common.data.model.AvisioBox

@Dao
interface AvisioBoxDao {

    @Query("SELECT * FROM box")
    fun getBoxList(): List<AvisioBox>

    @Insert
    fun insertBox(box: AvisioBox)

    @Delete
    fun deleteBox(box: AvisioBox)

    @Query("DELETE FROM box")
    fun deleteAll()

}