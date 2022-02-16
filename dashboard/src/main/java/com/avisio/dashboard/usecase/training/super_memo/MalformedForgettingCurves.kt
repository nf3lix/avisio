package com.avisio.dashboard.usecase.training.super_memo

import java.lang.IndexOutOfBoundsException

class MalformedForgettingCurves : IndexOutOfBoundsException() {

    override val message: String
        get() = "There must be a forgetting curve for each combination of considered AF and repetition"

}