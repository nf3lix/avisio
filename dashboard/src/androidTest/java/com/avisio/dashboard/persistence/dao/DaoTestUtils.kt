package com.avisio.dashboard.persistence.dao

import com.avisio.dashboard.common.data.model.box.AvisioBox
import com.avisio.dashboard.common.data.model.card.Card

class DaoTestUtils {

    companion object {

        fun boxesEquals(box1: AvisioBox, box2: AvisioBox): Boolean {
            return box1.createDate.time == box2.createDate.time
                    && box1.name == box2.name
                    && box1.icon.iconId == box2.icon.iconId
        }

        fun cardsEquals(card1: Card, card2: Card): Boolean {
            return card1.boxId == card2.boxId
                    && card1.question == card2.question
                    && card1.answer == card2.answer
                    && card1.createDate.time == card2.createDate.time
                    && card1.type == card2.type
        }

    }

}
