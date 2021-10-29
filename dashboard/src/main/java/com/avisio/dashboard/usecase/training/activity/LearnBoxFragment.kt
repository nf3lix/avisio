package com.avisio.dashboard.usecase.training.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.model.box.AvisioBox
import com.avisio.dashboard.usecase.training.DefaultTrainingStrategy
import com.avisio.dashboard.usecase.training.TrainingStrategy

class LearnBoxFragment : Fragment() {

    private lateinit var trainingStrategy: TrainingStrategy
    private lateinit var box: AvisioBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        box = AvisioBox()
        trainingStrategy = DefaultTrainingStrategy(box)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_learn_box, container, false)
    }

}