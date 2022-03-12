package com.avisio.dashboard.usecase.training.activity.question

import android.content.Context
import android.util.AttributeSet
import com.avisio.dashboard.usecase.crud_card.common.input_flex_box.CardInputFlexBox
import com.avisio.dashboard.usecase.crud_card.common.save_constraints.SaveCardConstraint

class AnswerLearnFlexBox(context: Context, attributeSet: AttributeSet) : CardInputFlexBox(context, attributeSet, SaveCardConstraint.TargetInput.ANSWER_INPUT) {

    override fun initToolbar() {}

    override fun resetEditText() {}

}