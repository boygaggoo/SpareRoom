package com.spareroom.android.intent

sealed class MainStateEvent {

    object GetSpareRoomEvents: MainStateEvent()

    object GetSpareRoomLocalDbEvents: MainStateEvent()

    object None: MainStateEvent()

}