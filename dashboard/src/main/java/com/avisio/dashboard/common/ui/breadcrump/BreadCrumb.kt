package com.avisio.dashboard.common.ui.breadcrump

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.*
import com.avisio.dashboard.R

class BreadCrumb(context: Context, attrs: AttributeSet) : HorizontalScrollView(context, attrs) {

    private var elements: ArrayList<BreadCrumbDirectoryElement> = arrayListOf()
    private val views: HashMap<BreadCrumbDirectoryElement, TextView> = HashMap()

    private var holder: LinearLayout

    init {
        inflate(context, R.layout.bread_crumb_view, this)
        holder = findViewById(R.id.holder)
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.BreadCrumb)
        attributes.recycle()
    }

    internal fun clearElements() {
        holder.removeAllViews()
        elements.clear()
    }

    internal fun addElement(element: BreadCrumbDirectoryElement) {
        elements.add(element)
        addElementTextView(element)
        addBreadCrumbSeparator()
        scrollToEnd()
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
        holder.addView(textView)
        views[element] = textView
    }

    private fun addBreadCrumbSeparator() {
        val separator = ImageView(context)
        separator.setImageResource(R.drawable.ic_breadcrumb_separator)
        holder.addView(separator)
    }

    private fun scrollToEnd() {
        viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                scrollBarSize = 4
                scrollTo(width, 0)
            }
        })
    }

}