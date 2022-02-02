package com.avisio.dashboard.persistence.dao

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import com.avisio.dashboard.common.data.database.AppDatabase
import com.avisio.dashboard.common.data.model.box.AvisioFolder
import com.avisio.dashboard.common.persistence.FolderDao
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FolderDaoTest : DaoTest() {

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    private lateinit var folderDao: FolderDao
    private lateinit var database: AppDatabase

    @Before
    fun recreateDatabase() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = AppDatabase(context)
        folderDao = database.folderDao()
        database.clearAllTables()
    }

    @Test
    fun getFoldersTest() {
        val folder1 = AvisioFolder(id = 1, parentFolder = null)
        val folder2 = AvisioFolder(id = 2, parentFolder = 1)
        val folder3 = AvisioFolder(id = 3, parentFolder = 2)
        folderDao.createFolder(folder1)
        folderDao.createFolder(folder2)
        folderDao.createFolder(folder3)
        val fetchedFolders = folderDao.getAll().blockingObserve()
        Assert.assertEquals(fetchedFolders?.size, 3)
    }

    @Test
    fun deleteFolderTest() {
        val folder1 = AvisioFolder(id = 1, parentFolder = null)
        folderDao.createFolder(folder1)
        val fetchedFolders1 = folderDao.getAll().blockingObserve()
        Assert.assertEquals(fetchedFolders1?.size, 1)
        folderDao.deleteFolder(folder1)
        val fetchedFolders2 = folderDao.getAll().blockingObserve()
        Assert.assertEquals(fetchedFolders2?.size, 0)
    }

    @Test
    fun moveFolderTest() {
        val folder1 = AvisioFolder(id = 1, parentFolder = null)
        val folder2 = AvisioFolder(id = 2, parentFolder = null)
        val folder3 = AvisioFolder(id = 3, parentFolder = 1)
        folderDao.createFolder(folder1)
        folderDao.createFolder(folder2)
        folderDao.createFolder(folder3)
        val fetchedFolders1 = folderDao.getAll().blockingObserve()
        Assert.assertEquals(fetchedFolders1?.get(2), AvisioFolder(3, 1))
        folderDao.moveFolder(3, 2)
        val fetchedFolders2 = folderDao.getAll().blockingObserve()
        Assert.assertEquals(fetchedFolders2?.get(2), AvisioFolder(3, 2))
    }

}