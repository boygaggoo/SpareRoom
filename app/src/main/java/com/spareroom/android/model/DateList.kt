package com.spareroom.android.model

class DateList: UpcomingList() {
    var date: String? = null

    override val type: Int
        get() = TYPE_DATE

}