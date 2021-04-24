package com.spareroom.android.intent

sealed class MainStateEvent {

    object GetSpareRoomEvents: MainStateEvent()

    object None: MainStateEvent()

}