package com.avisio.dashboard.usecase.training

import androidx.core.os.bundleOf
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.intent.Intents
import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.model.box.AvisioBox
import com.avisio.dashboard.common.data.model.box.ParcelableAvisioBox
import com.avisio.dashboard.common.data.transfer.IntentKeys
import com.avisio.dashboard.usecase.training.activity.LearnBoxFragment
import org.junit.After
import org.junit.Before

class LearnBoxFragmentTest {

    private lateinit var scenario: FragmentScenario<LearnBoxFragment>

    @Before
    fun initScenario() {
        Intents.init()
        val fragmentArgs = bundleOf(
            IntentKeys.BOX_OBJECT to ParcelableAvisioBox.createFromEntity(AvisioBox()))
        scenario = launchFragmentInContainer(fragmentArgs = fragmentArgs, themeResId = R.style.Theme_Avisio)
    }

    @After
    fun releaseIntents() {
        Intents.release()
    }

}