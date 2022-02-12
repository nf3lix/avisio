package com.avisio.dashboard.common.ui.breadcrump

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.avisio.dashboard.R

class BreadCrumb(context: Context, attrs: AttributeSet) : HorizontalScrollView(context, attrs) {

    private var elements: ArrayList<BreadCrumbDirectoryElement> = arrayListOf()

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
        if(element.iconId != null) {
            addElementIcon(element)
        } else {
            addElementTextView(element)
        }
        addBreadCrumbSeparator()
        scrollToEnd()
    }

    private fun addElementTextView(element: BreadCrumbDirectoryElement) {
        val textView = TextView(context)
        textView.text = element.displayName
        setElementLayoutParams(textView)
        textView.setOnClickListener {
            triggerItemClick(it)
        }
        holder.addView(textView)
    }

    private fun addElementIcon(element: BreadCrumbDirectoryElement) {
        val iconElement = ImageView(context)
        iconElement.setImageResource(element.iconId!!)
        iconElement.tag = element.iconId
        setElementLayoutParams(iconElement)
        iconElement.setOnClickListener {
            triggerItemClick(it)
        }
        holder.addView(iconElement)
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

    private fun setElementLayoutParams(view: View) {
        val params = MarginLayoutParams(LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        params.setMargins(16, 0 , 16, 0)
        view.layoutParams = params
    }

    private fun triggerItemClick(view: View) {
        val index = (view.parent as ViewGroup).indexOfChild(view) / 2
        onElementClicked(index)
    }

}