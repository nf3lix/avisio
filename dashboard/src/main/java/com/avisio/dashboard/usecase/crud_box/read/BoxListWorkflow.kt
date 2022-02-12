package com.avisio.dashboard.usecase.crud_box.read

abstract class BoxListWorkflow {

    abstract fun getDisplayedMenuItems(): Map<Int, Boolean>

}