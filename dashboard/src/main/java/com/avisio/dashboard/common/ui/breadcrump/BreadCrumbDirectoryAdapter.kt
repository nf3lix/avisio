package com.avisio.dashboard.common.ui.breadcrump

abstract class BreadCrumbDirectoryAdapter<T: Any> {

    private lateinit var breadCrumb: BreadCrumb

    abstract fun convert(value: T): BreadCrumbDirectoryElement

    internal fun setBreadCrumb(breadCrumb: BreadCrumb) {
        this.breadCrumb = breadCrumb
    }

    fun setElements(elements: List<T>) {
        breadCrumb.clearElements()
        for(element in elements) {
            breadCrumb.addElement(convert(element))
        }
    }

}