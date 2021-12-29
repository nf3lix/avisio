package com.avisio.dashboard.usecase.training.super_memo

import com.avisio.dashboard.usecase.training.super_memo.model.CardQueue
import com.avisio.dashboard.usecase.training.super_memo.model.ForgettingCurves
import com.avisio.dashboard.usecase.training.super_memo.model.ForgettingIndexGraph
import com.avisio.dashboard.usecase.training.super_memo.rfm.RFM
import com.avisio.dashboard.usecase.training.super_memo.ofm.OFM
import com.avisio.dashboard.usecase.training.super_memo.rfm.RFMIntf

interface SuperMemoIntf {

    fun queue(): CardQueue
    fun rfm(): RFMIntf
    fun ofm(): OFM
    fun forgettingIndexGraph(): ForgettingIndexGraph
    fun forgettingCurves(): ForgettingCurves

}