package com.avisio.dashboard.persistence

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.avisio.dashboard.activity.box_list.AvisioBoxListAdapter
import com.avisio.dashboard.common.data.database.AppDatabase
import com.avisio.dashboard.common.data.model.AvisioBox
import com.avisio.dashboard.common.data.model.AvisioBoxViewModel
import com.avisio.dashboard.common.persistence.AvisioBoxDao
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class AvisioBoxDaoTest {

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    private lateinit var boxDao: AvisioBoxDao
    private lateinit var database: AppDatabase

    @Before
    fun recreateDatabase() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = AppDatabase(context)
        boxDao = database.boxDao()
        boxDao.deleteAll()
    }

    @Test
    fun getBoxListTest() {
        val box1 = AvisioBox(name = "BOX_NAME_1", createDate = Date(1600000000))
        val box2 = AvisioBox(name = "BOX_NAME_2", createDate = Date(1600003000))
        boxDao.insertBox(box1)
        boxDao.insertBox(box2)
        val fetchedBox = boxDao.getBoxList().blockingObserve()
        assertEquals(fetchedBox?.size, 2)
        assertTrue(AvisioBoxDaoTestUtils.contentEquals(fetchedBox?.get(0)!!, box1))
        assertTrue(AvisioBoxDaoTestUtils.contentEquals(fetchedBox[1], box2))
    }

    @Test
    fun insertBoxTest() {
        val box = AvisioBox(name = "BOX_NAME", createDate = Date(1600000000))
        boxDao.insertBox(box)
        val fetchedBox = boxDao.getBoxList().blockingObserve()
        assertEquals(fetchedBox?.size, 1)
    }

    @Test
    fun deleteBoxTest() {
        val box = AvisioBox(name = "BOX_NAME_DELETE_TEST", createDate = Date(1600000000))
        boxDao.insertBox(box)
        val boxToDelete = boxDao.getBoxList().blockingObserve()?.get(0)!!
        boxDao.deleteBox(boxToDelete)
        val newBoxList = boxDao.getBoxList().blockingObserve()
        assertEquals(newBoxList?.size, 0)
    }

    @Test
    fun deleteAllTest() {
        val box1 = AvisioBox(name = "BOX_NAME_1", createDate = Date(1600000000))
        val box2 = AvisioBox(name = "BOX_NAME_2", createDate = Date(1600000000))
        boxDao.insertBox(box1)
        boxDao.insertBox(box2)
        boxDao.deleteAll()
        val fetchedBox = boxDao.getBoxList().blockingObserve()
        assertEquals(fetchedBox?.size, 0)
    }

    // source: https://stackoverflow.com/questions/44270688/unit-testing-room-and-livedata
    private fun <T> LiveData<T>.blockingObserve(): T? {
        var value: T? = null
        val latch = CountDownLatch(1)
        val observer = Observer<T> { t ->
            value = t
            latch.countDown()
        }
        observeForever(observer)
        latch.await(2, TimeUnit.SECONDS)
        return value
    }

}