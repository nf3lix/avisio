package com.avisio.dashboard.usecase.training.super_memo.regression

import com.avisio.dashboard.usecase.training.super_memo.model.Model

sealed class Regression<T: Model> {

    abstract fun compute(): T

}