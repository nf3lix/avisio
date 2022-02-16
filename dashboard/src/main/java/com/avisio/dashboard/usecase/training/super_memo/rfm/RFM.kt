package com.avisio.dashboard.usecase.training.super_memo.rfm

import com.avisio.dashboard.usecase.training.super_memo.SuperMemoIntf
import com.avisio.dashboard.usecase.training.super_memo.SuperMemo

class RFM(private val sm: SuperMemoIntf) : RFMIntf {

    override fun rf(repetition: Int, afIndex: Int): Double {
        return sm.forgettingCurves().curves()[repetition][afIndex].uf(100.0 - SuperMemo.REQUESTED_FI)
    }

}