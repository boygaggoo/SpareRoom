package com.spareroom.android.model

class DetailList: UpcomingList() {
    var upcomingModel: SpareRoomModel? = null

    override val type: Int
        get() = TYPE_GENERAL
}