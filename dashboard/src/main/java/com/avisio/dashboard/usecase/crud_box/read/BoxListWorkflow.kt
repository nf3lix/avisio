package com.avisio.dashboard.usecase.crud_box.read

abstract class BoxListWorkflow {

    abstract fun initWorkflow()
    abstract fun finishWorkflow()
    abstract fun getDisplayedMenuItems(): Map<Int, Boolean>

}