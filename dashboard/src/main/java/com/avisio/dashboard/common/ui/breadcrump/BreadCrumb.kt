package com.avisio.dashboard.common.ui.breadcrump

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.avisio.dashboard.R

class BreadCrumb(context: Context, attrs: AttributeSet) : HorizontalScrollView(context, attrs) {

    private var elements: ArrayList<BreadCrumbDirectoryElement> = arrayListOf()
    private val views: HashMap<BreadCrumbDirectoryElement, TextView> = HashMap()

    private var holder: LinearLayout
    private var scrollbarSize = 0

    private var onElementClicked: (Int) -> Unit = { }

    init {
        inflate(context, R.layout.bread_crumb_view, this)
        holder = findViewById(R.id.breadcrumb_holder)
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.BreadCrumb)
        attributes.getInt(R.styleable.BreadCrumb_breadCrumbScrollbarSize, 0)
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
        textView.setOnClickListener {
            val index = (textView.parent as ViewGroup).indexOfChild(textView) / 2
            onElementClicked(index)
        }
        holder.addView(textView)
        views[element] = textView
    }

    fun setOnBreadCrumbElementClickListener(onClick: (itemIndex: Int) -> Unit) {
        onElementClicked = onClick
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
                scrollBarSize = scrollbarSize
                scrollTo(width, 0)
            }
        })
    }

}