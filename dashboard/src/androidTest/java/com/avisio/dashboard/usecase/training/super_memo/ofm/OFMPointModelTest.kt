package com.avisio.dashboard.usecase.training.super_memo.ofm

import com.avisio.dashboard.usecase.training.super_memo.model.PowerLawModel
import com.avisio.dashboard.usecase.training.super_memo.ofm.OFMModel
import org.junit.Assert
import org.junit.Test

class OFMPointModelTest {

    @Test
    fun getY() {
        val model = PowerLawModel(3.0, 2.0)
        val pointModel = OFMModel.PointModel(model)
        Assert.assertEquals(pointModel.getY(1.0), 16.0, 0.0)
        Assert.assertEquals(pointModel.getY(2.0), 54.0, 0.0)
        Assert.assertEquals(pointModel.getY(3.0), 128.0, 0.0)
    }

    @Test
    fun getX() {
        val model = PowerLawModel(3.0, 2.0)
        val pointModel = OFMModel.PointModel(model)
        Assert.assertEquals(pointModel.getX(16.0), 1.0, 0.0)
        Assert.assertEquals(pointModel.getX(54.0), 2.0, 0.0)
        Assert.assertEquals(pointModel.getX(128.0), 3.0, 1E-6)
    }

}