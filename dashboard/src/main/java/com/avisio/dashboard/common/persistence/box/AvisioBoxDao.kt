package com.avisio.dashboard.common.persistence.box

import androidx.lifecycle.LiveData
import androidx.room.*
import com.avisio.dashboard.common.data.model.box.AvisioBox

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

    @Query("SELECT name FROM box")
    fun getBoxNameList(): List<String>

    @Query("SELECT * FROM box WHERE id = :boxId")
    fun getBoxById(boxId: Long): AvisioBox

    @Query("DELETE FROM box WHERE id = :boxId")
    fun deleteBox(boxId: Long)

    @Query("UPDATE box SET folderId = NULL WHERE id = :boxToMove")
    fun moveToRootFolder(boxToMove: Long)

    @Query("UPDATE box SET folderId = :destinationFolder WHERE id = :boxToMove")
    fun moveBox(boxToMove: Long, destinationFolder: Long)

}