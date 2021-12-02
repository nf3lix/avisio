package com.avisio.dashboard.usecase.create_card

import android.widget.EditText
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import com.avisio.dashboard.usecase.crud_card.common.input_flex_box.AnswerFlexBox
import com.avisio.dashboard.usecase.crud_card.common.input_flex_box.QuestionFlexBox
import org.hamcrest.core.AllOf
import org.hamcrest.core.Is

class EditCardTestUtils {

    companion object {

        fun typeInQuestionEditText(question: String) {
            typeInEditText(QuestionFlexBox::class.java.name, question)
        }

        fun typeInAnswerEditText(answer: String) {
            typeInEditText(AnswerFlexBox::class.java.name, answer)
        }

        private fun typeInEditText(className: String, input: String) {
            Espresso.onView(
                AllOf.allOf(
                    ViewMatchers.withParent(
                        ViewMatchers.withParent(
                            ViewMatchers.withParent(
                                ViewMatchers.withParent(
                                    ViewMatchers.withParent(ViewMatchers.withClassName(Is.`is`(className)))
                                )
                            )
                        )
                    ),
                    ViewMatchers.withClassName(Is.`is`(EditText::class.java.name))
                )
            )
                .perform(ViewActions.typeText(input))
        }
    }



}