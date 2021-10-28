package com.avisio.dashboard.persistence.dao

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.avisio.dashboard.common.data.database.AppDatabase
import com.avisio.dashboard.common.data.model.card.*
import com.avisio.dashboard.common.data.model.card.question.CardQuestion
import com.avisio.dashboard.common.data.model.card.question.CardQuestionToken
import com.avisio.dashboard.common.data.model.card.question.CardQuestionTokenType
import com.avisio.dashboard.common.persistence.CardDao
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

class CardDaoTest : DaoTest() {

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    private lateinit var cardDao: CardDao
    private lateinit var database: AppDatabase

    @Before
    fun recreateDatabase() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = AppDatabase(context)
        cardDao = database.cardDao()
        cardDao.deleteAll()
    }

    @Test
    fun getAllCards() {
        val card1 = Card(boxId = 1)
        val card2 = Card(boxId = 2)
        cardDao.insert(card1)
        cardDao.insert(card2)
        val fetchedCard = cardDao.getAll().blockingObserve()
        Assert.assertEquals(fetchedCard?.size, 2)
        Assert.assertTrue(DaoTestUtils.cardsEquals(card1, fetchedCard?.get(0)!!))
        Assert.assertTrue(DaoTestUtils.cardsEquals(card2, fetchedCard[1]))
    }

    @Test
    fun getCardsByBox() {
        val card1 = Card(boxId = 1)
        val card2 = Card(boxId = 1)
        val card3 = Card(boxId = 2)
        val card4 = Card(boxId = 2)
        cardDao.insert(card1)
        cardDao.insert(card2)
        cardDao.insert(card3)
        cardDao.insert(card4)
        val fetchedCardBox1 = cardDao.getCardsByBox(1).blockingObserve()
        val fetchedCardBox2 = cardDao.getCardsByBox(2).blockingObserve()
        Assert.assertEquals(2, fetchedCardBox1?.size)
        Assert.assertEquals(2, fetchedCardBox2?.size)
        Assert.assertTrue(DaoTestUtils.cardsEquals(card1, fetchedCardBox1?.get(0)!!))
        Assert.assertTrue(DaoTestUtils.cardsEquals(card2, fetchedCardBox1[1]))
        Assert.assertTrue(DaoTestUtils.cardsEquals(card3, fetchedCardBox2?.get(0)!!))
        Assert.assertTrue(DaoTestUtils.cardsEquals(card4, fetchedCardBox2[1]))
    }

    @Test
    fun insertCardTest() {
        cardDao.insert(getDefaultCard())
        val fetchedCards = cardDao.getAll().blockingObserve()
        Assert.assertEquals(fetchedCards?.size, 1)
    }

    @Test
    fun deleteAllTest() {
        cardDao.insert(getDefaultCard())
        cardDao.insert(getDefaultCard())
        val fetchedCardsPre = cardDao.getAll().blockingObserve()
        Assert.assertEquals(fetchedCardsPre?.size, 2)
        cardDao.deleteAll()
        val fetchedCards = cardDao.getAll().blockingObserve()
        Assert.assertEquals(fetchedCards?.size, 0)
    }

    @Test
    fun deleteTest() {
        val card = Card(id = 0)
        cardDao.insert(card)
        val cardToDelete = cardDao.getAll().blockingObserve()?.get(0)!!
        cardDao.deleteCard(cardToDelete)
        val fetchedCards = cardDao.getAll().blockingObserve()
        Assert.assertEquals(fetchedCards?.size, 0)
    }

    @Test
    fun getCardTest() {
        val card = getDefaultCard()
        cardDao.insert(card)
        val fetchedCard = cardDao.getAll().blockingObserve()?.get(0)!!
        Assert.assertTrue(DaoTestUtils.cardsEquals(card, fetchedCard))
    }

    @Test
    fun updateCardTest() {
        val card1 = Card(boxId = 1, type = CardType.STANDARD)
        cardDao.insert(card1)
        val cardToUpdate = cardDao.getAll().blockingObserve()?.get(0)!!
        val card2 = Card(id = cardToUpdate.id, type = CardType.CLOZE_TEXT)
        cardDao.updateCard(card2)
        val updatedBox = cardDao.getAll().blockingObserve()?.get(0)!!
        Assert.assertTrue(DaoTestUtils.cardsEquals(card2, updatedBox))
    }

    private fun getDefaultCard(): Card {
        return Card(
            boxId = 2,
            createDate = Date(1600003000),
            type = CardType.CLOZE_TEXT,
            question = CardQuestion(
                arrayListOf(
                    CardQuestionToken("CONTENT_1", CardQuestionTokenType.TEXT),
                    CardQuestionToken("CONTENT_2", CardQuestionTokenType.IMAGE),
                    CardQuestionToken("CONTENT_3", CardQuestionTokenType.QUESTION),
                    CardQuestionToken("CONTENT_4", CardQuestionTokenType.QUESTION))),
            answer = CardAnswer(arrayListOf("CONTENT_3", "CONTENT_4")))
    }


}