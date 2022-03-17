//package com.avisio.dashboard.usecase.crud_box.common
//
//import android.os.Build
//import android.view.View
//import android.widget.PopupMenu
//import com.avisio.dashboard.R
//import com.avisio.dashboard.usecase.crud_box.create_box.EditBoxFragment
//import java.lang.reflect.Method
//
//class BoxIconSelectionPopup(private val fragment: EditBoxFragment, view: View) {
//
//    private val popup = PopupMenu(fragment.context, view)
//
//    companion object {
//        fun showPopup(fragment: EditBoxFragment, view: View) {
//            val popup = BoxIconSelectionPopup(fragment, view)
//            popup.show()
//        }
//    }
//
//    init {
//        popup.menuInflater.inflate(R.menu.select_box_icon_menu, popup.menu)
//        showIcon()
//        setOnMenuIconClickListener()
//    }
//
//    private fun showIcon() {
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            popup.setForceShowIcon(true)
//            return
//        }
//        forceShowIconLowerVersion(popup)
//    }
//
//    private fun setOnMenuIconClickListener() {
//        popup.setOnMenuItemClickListener { menuItem ->
//            fragment.updateBoxIcon(menuItem.itemId)
//            true
//        }
//    }
//
//    private fun show() {
//        popup.show()
//    }
//
//    private fun forceShowIconLowerVersion(popup: PopupMenu) {
//        try {
//            val fields = popup.javaClass.declaredFields
//            for(field in fields) {
//                if("mPopup" == field.name) {
//                    field.isAccessible = true
//                    val menuPopupHelper = field[popup]
//                    val classPopupHelper = Class.forName(menuPopupHelper.javaClass.name)
//                    val setForceIcons: Method = classPopupHelper.getMethod(
//                        "setForceShowIcon",
//                        Boolean::class.javaPrimitiveType
//                    )
//                    setForceIcons.invoke(menuPopupHelper, true)
//                    break
//                }
//            }
//        } catch(e: Exception) {
//            e.printStackTrace()
//        }
//    }
//
//}