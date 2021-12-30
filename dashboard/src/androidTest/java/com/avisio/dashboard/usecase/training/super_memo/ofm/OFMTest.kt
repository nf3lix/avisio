package com.avisio.dashboard.usecase.training.super_memo.ofm

import com.avisio.dashboard.usecase.training.super_memo.SuperMemoIntf
import com.avisio.dashboard.usecase.training.super_memo.rfm.RFMIntf
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class OFMTest {


    @Mock
    private lateinit var initialSM: SuperMemoIntf

    @Mock
    private lateinit var advancedSM: SuperMemoIntf

    private lateinit var rfm1: RFMIntf
    private lateinit var rfm2: RFMIntf

    @Before
    fun setUp() {
        rfm1 = object : RFMIntf {
            override fun rf(repetition: Int, afIndex: Int): Double {
                return 1.0429290121703236
            }
        }
        rfm2 = object : RFMIntf {
            override fun rf(repetition: Int, afIndex: Int): Double {
                return 1.1548701505862036
            }
        }
        `when`(initialSM.rfm()).thenReturn(rfm1)
        `when`(advancedSM.rfm()).thenReturn(rfm2)
    }

    @Test
    fun ofEqualToRFactorInFirstRepetition() {
        val ofm = OFM(initialSM)
        val ofm2 = OFM(advancedSM)
        Assert.assertEquals(ofm.of(0, 0), 1.0429290121703236, 1E-6)
        Assert.assertEquals(ofm.of(0, 1), 1.0429290121703236, 1E-6)
        Assert.assertEquals(ofm2.of(0, 0), 1.1548701505862036, 1E-6)
        Assert.assertEquals(ofm2.of(0, 1), 1.1548701505862036, 1E-6)
    }

}