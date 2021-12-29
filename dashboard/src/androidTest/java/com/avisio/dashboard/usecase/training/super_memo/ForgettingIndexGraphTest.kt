package com.avisio.dashboard.usecase.training.super_memo

import com.avisio.dashboard.usecase.training.super_memo.model.ForgettingIndexGraph
import org.junit.Assert
import org.junit.Test

class ForgettingIndexGraphTest {

    @Test
    fun initialFI() {
        val graph = ForgettingIndexGraph(SuperMemo())
        Assert.assertEquals(graph.forgettingIndex(1.0), 61.31471927654583, 0.0)
        Assert.assertEquals(graph.forgettingIndex(2.0), 38.68528072345415, 0.0)
        Assert.assertEquals(graph.forgettingIndex(3.0), 22.62943855309168, 0.0)
        Assert.assertEquals(graph.forgettingIndex(4.0), 10.175559829607286, 0.0)
        Assert.assertEquals(graph.forgettingIndex(5.0), 0.0, 0.0)
    }

}