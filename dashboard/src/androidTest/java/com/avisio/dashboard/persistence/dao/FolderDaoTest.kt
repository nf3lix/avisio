package com.avisio.dashboard.persistence.dao

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import com.avisio.dashboard.common.data.database.AppDatabase
import com.avisio.dashboard.common.data.model.box.AvisioBox
import com.avisio.dashboard.common.data.model.box.AvisioFolder
import com.avisio.dashboard.common.persistence.folder.FolderDao
import com.avisio.dashboard.common.persistence.box.AvisioBoxDao
import com.avisio.dashboard.usecase.crud_box.common.BoxIcon
import com.avisio.dashboard.usecase.crud_box.read.dashboard_item.DashboardItem
import com.avisio.dashboard.usecase.crud_box.read.dashboard_item.DashboardItemType
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FolderDaoTest : DaoTest() {

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    private lateinit var folderDao: FolderDao
    private lateinit var boxDao: AvisioBoxDao
    private lateinit var database: AppDatabase

    @Before
    fun recreateDatabase() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = AppDatabase(context)
        folderDao = database.folderDao()
        boxDao = database.boxDao()
        database.clearAllTables()
    }

    @Test
    fun getFoldersTest() {
        val folder1 = AvisioFolder(id = 1)
        val folder2 = AvisioFolder(id = 2, parentFolder = 1)
        val folder3 = AvisioFolder(id = 3, parentFolder = 2)
        folderDao.insertFolder(folder1)
        folderDao.insertFolder(folder2)
        folderDao.insertFolder(folder3)
        val fetchedFolders = folderDao.getAll().blockingObserve()
        Assert.assertEquals(fetchedFolders?.size, 3)
    }

    @Test
    fun deleteFolderTest() {
        val folder1 = AvisioFolder(id = 1)
        folderDao.insertFolder(folder1)
        val fetchedFolders1 = folderDao.getAll().blockingObserve()
        Assert.assertEquals(fetchedFolders1?.size, 1)
        folderDao.deleteFolder(folder1)
        val fetchedFolders2 = folderDao.getAll().blockingObserve()
        Assert.assertEquals(fetchedFolders2?.size, 0)
    }

    @Test
    fun moveFolderTest() {
        val folder1 = AvisioFolder(id = 1)
        val folder2 = AvisioFolder(id = 2)
        val folder3 = AvisioFolder(id = 3, parentFolder = 1)
        folderDao.insertFolder(folder1)
        folderDao.insertFolder(folder2)
        folderDao.insertFolder(folder3)
        val fetchedFolders1 = folderDao.getAll().blockingObserve()
        Assert.assertEquals(fetchedFolders1?.get(2), AvisioFolder(3, 1))
        folderDao.moveFolder(3, 2)
        val fetchedFolders2 = folderDao.getAll().blockingObserve()
        Assert.assertEquals(fetchedFolders2?.get(2), AvisioFolder(3, 2))
    }

    @Test
    fun onDeleteCascadeBoxes() {
        val folder = AvisioFolder(id = 1)
        val box1 = AvisioBox(id = 1, name = "BOX_1", parentFolder = 1)
        val box2 = AvisioBox(id = 2, name = "BOX_2", parentFolder = 1)
        val box3 = AvisioBox(id = 3, name = "BOX_3")
        folderDao.insertFolder(folder)
        boxDao.insertBox(box1)
        boxDao.insertBox(box2)
        boxDao.insertBox(box3)
        folderDao.deleteFolder(folder)
        val fetchedBoxes = boxDao.getBoxList().blockingObserve()
        Assert.assertEquals(fetchedBoxes?.size, 1)
        Assert.assertEquals(fetchedBoxes?.get(0), box3)
    }

    @Test
    fun getAllFoldersAndBoxesTest() {
        val folder0 = AvisioFolder(id = 1)
        val folder1 = AvisioFolder(id = 2, parentFolder = 1, name = "FOLDER_1")
        val folder2 = AvisioFolder(id = 3, parentFolder = 1, name = "FOLDER_2")
        val box1 = AvisioBox(id = 1, parentFolder = 1, name = "BOX_1")
        val box2 = AvisioBox(id = 2, parentFolder = 1, name = "BOX_2")
        folderDao.insertFolder(folder0)
        folderDao.insertFolder(folder1)
        folderDao.insertFolder(folder2)
        boxDao.insertBox(box1)
        boxDao.insertBox(box2)
        val result = folderDao.getAllFromParentId(1).blockingObserve()
        Assert.assertEquals(result?.size, 4)
        Assert.assertEquals(result?.get(0), DashboardItem(id = 2, parentFolder = 1, DashboardItemType.FOLDER, "FOLDER_1", icon = -1))
        Assert.assertEquals(result?.get(1), DashboardItem(id = 3, parentFolder = 1, DashboardItemType.FOLDER, "FOLDER_2", icon = -1))
        Assert.assertEquals(result?.get(2), DashboardItem(id = 1, parentFolder = 1, DashboardItemType.BOX, "BOX_1", icon = BoxIcon.DEFAULT.iconId))
        Assert.assertEquals(result?.get(3), DashboardItem(id = 2, parentFolder = 1, DashboardItemType.BOX, "BOX_2", icon = BoxIcon.DEFAULT.iconId))
    }

    @Test
    fun getAllFoldersWithBoxesInRoot() {
        val folder1 = AvisioFolder(id = 1, name = "FOLDER_1")
        val folder2 = AvisioFolder(id = 2, name = "FOLDER_2")
        val box1 = AvisioBox(id = 1, "BOX_1")
        val box2 = AvisioBox(id = 2, "BOX_2")
        folderDao.insertFolder(folder1)
        folderDao.insertFolder(folder2)
        boxDao.insertBox(box1)
        boxDao.insertBox(box2)
        val result = folderDao.getAllFromRoot().blockingObserve()
        Assert.assertEquals(result?.size, 4)
        Assert.assertEquals(result?.get(0), DashboardItem(id = 1, parentFolder = null, DashboardItemType.FOLDER, "FOLDER_1", icon = -1))
        Assert.assertEquals(result?.get(1), DashboardItem(id = 2, parentFolder = null, DashboardItemType.FOLDER, "FOLDER_2", icon = -1))
        Assert.assertEquals(result?.get(2), DashboardItem(id = 1, parentFolder = null, DashboardItemType.BOX, "BOX_1", icon = BoxIcon.DEFAULT.iconId))
        Assert.assertEquals(result?.get(3), DashboardItem(id = 2, parentFolder = null, DashboardItemType.BOX, "BOX_2", icon = BoxIcon.DEFAULT.iconId))
    }

}