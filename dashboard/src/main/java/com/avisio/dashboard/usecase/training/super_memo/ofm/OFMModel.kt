package com.avisio.dashboard.usecase.training.super_memo.ofm

import com.avisio.dashboard.usecase.training.super_memo.model.Model
import com.avisio.dashboard.usecase.training.super_memo.model.PowerLawModel

class OFMModel {

    interface OFM0 {
        fun get(i: Int): Double
    }

    interface OFMN {
        fun get(i: Int): PointModel
    }

    class PointModel(private val model: PowerLawModel): Model() {
        override fun getX(y: Double): Double {
            return model.getX(y) - OFM.INITIAL_REPETITION_VALUE
        }

        override fun getY(x: Double): Double {
            return model.getY(OFM.repFromIndex(x))
        }

    }

}