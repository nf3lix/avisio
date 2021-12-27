package com.avisio.dashboard.usecase.training.super_memo.model

import com.avisio.dashboard.usecase.training.super_memo.SuperMemo
import java.util.*

class CardItem(private val sm: SuperMemo, private var dueDate: Date = Date(System.currentTimeMillis())) {

    fun dueDate(): Date {
        return dueDate
    }

}