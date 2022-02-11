package com.avisio.dashboard.common.ui.breadcrump

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.avisio.dashboard.R
import kotlinx.android.synthetic.main.box_detail_view.view.*

class BreadCrumb(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    private var elements: ArrayList<BreadCrumbDirectoryElement> = arrayListOf()

    private val views: HashMap<BreadCrumbDirectoryElement, TextView> = HashMap()

    init {
        inflate(context, R.layout.bread_crumb_view, this)
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.BreadCrumb)
        attributes.recycle()
    }

    internal fun clearElements() {
        removeAllViews()
        elements.clear()
    }

    internal fun addElement(element: BreadCrumbDirectoryElement) {
        elements.add(element)
        addElementTextView(element)
        addBreadCrumbSeparator()
    }

    internal fun addElement(index: Int, element: BreadCrumbDirectoryElement) {
        elements.add(index, element)
    }

    internal fun setElements(elements: ArrayList<BreadCrumbDirectoryElement>) {
        this.elements = elements
    }

    private fun addElementTextView(element: BreadCrumbDirectoryElement) {
        val textView = TextView(context)
        textView.text = element.displayName
        val params = MarginLayoutParams(LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        params.setMargins(16, 0 , 16, 0)
        textView.layoutParams = params
        addView(textView)
        views[element] = textView
    }

    private fun addBreadCrumbSeparator() {
        val separator = ImageView(context)
        separator.setImageResource(R.drawable.ic_breadcrumb_separator)
        addView(separator)
    }

}