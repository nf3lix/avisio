package com.avisio.dashboard.usecase.training.super_memo

import com.avisio.dashboard.usecase.training.super_memo.ofm.OFM
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class OFMTest {


    @Mock
    private lateinit var initialSM: SuperMemo

    @Mock
    private lateinit var advancedSM: SuperMemo

    @Before
    fun setUp() {
        `when`(initialSM.rf(any(Int::class.java), any(Int::class.java))).thenReturn(1.0429290121703236)
        `when`(advancedSM.rf(any(Int::class.java), any(Int::class.java))).thenReturn(1.1548701505862036)
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