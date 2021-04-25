package com.spareroom.android.model

abstract class UpcomingList {
    abstract val type: Int

    companion object {
        const val TYPE_DATE = 0
        const val TYPE_GENERAL = 1
    }
}