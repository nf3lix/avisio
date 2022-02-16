package com.avisio.dashboard.usecase.training.super_memo.model

import java.util.*

class CardQueue {

    private val list: LinkedList<CardItem> = LinkedList<CardItem>()

    private fun findIndexToInsert(cardItem: CardItem): Int {
        if(list.size == 0) {
            return 0
        }
        if(list.size == 1) {
            if(list[0].dueDate().time <= cardItem.dueDate().time) {
                return 1
            }
            return 0
        }
        if(list[0].dueDate.time >= cardItem.dueDate.time) {
            return 0
        }
        for(i in 1 until list.size) {
            val prevCard = list[i - 1]
            val currentCard = list[i]
            if(prevCard.dueDate().time < cardItem.dueDate().time && currentCard.dueDate().time > cardItem.dueDate().time) {
                return i
            }
        }
        return list.size
    }

    fun addCard(cardItem: CardItem) {
        list.add(findIndexToInsert(cardItem), cardItem)
    }

    fun nextCard(isAdvanceable: Boolean = false): CardItem? {
        if(list.size == 0) {
            return null
        }
        if(isAdvanceable || list[0].dueDate().time <= Date(System.currentTimeMillis()).time) {
            return list[0]
        }
        return null
    }

    fun answer(cardItem: CardItem) {
        discard(cardItem)
        list.removeAt(list.indexOf(cardItem))
        val index = findIndexToInsert(cardItem)
        list.add(index, cardItem)
    }

    private fun discard(cardItem: CardItem) {
        val index = list.indexOf(cardItem)
        if(index >= 0) {
            list[index] = cardItem
        }
    }

    fun queueCount(): Int {
        return list.size
    }

}