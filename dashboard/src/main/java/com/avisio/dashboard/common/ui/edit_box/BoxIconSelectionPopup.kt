package com.avisio.dashboard.common.ui.edit_box

import android.os.Build
import android.view.View
import android.widget.PopupMenu
import com.avisio.dashboard.R
import java.lang.reflect.Method

class BoxIconSelectionPopup {

    companion object {
        fun createPopup(fragment: EditBoxFragment, view: View): PopupMenu {
            val popup = PopupMenu(fragment.context, view)
            popup.menuInflater.inflate(R.menu.select_box_icon_menu, popup.menu)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                popup.setForceShowIcon(true)
            } else {
                forceShowIconLowerVersion(popup)
            }
            return popup
        }

        private fun forceShowIconLowerVersion(popup: PopupMenu) {
            try {
                val fields = popup.javaClass.declaredFields
                for(field in fields) {
                    if("mPopup" == field.name) {
                        field.isAccessible = true
                        val menuPopupHelper = field[popup]
                        val classPopupHelper = Class.forName(menuPopupHelper.javaClass.name)
                        val setForceIcons: Method = classPopupHelper.getMethod(
                            "setForceShowIcon",
                            Boolean::class.javaPrimitiveType
                        )
                        setForceIcons.invoke(menuPopupHelper, true)
                        break
                    }
                }
            } catch(e: Exception) {
                e.printStackTrace()
            }
        }

    }



}