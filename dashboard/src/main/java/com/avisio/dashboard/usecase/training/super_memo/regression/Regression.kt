package com.avisio.dashboard.usecase.training.super_memo.regression

import com.avisio.dashboard.usecase.training.super_memo.model.Model
import com.avisio.dashboard.usecase.training.super_memo.model.PointSequence

sealed class Regression<T: Model>(private val points: PointSequence) {

    abstract fun compute(): T

}