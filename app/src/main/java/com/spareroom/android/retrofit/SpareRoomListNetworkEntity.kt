package com.spareroom.android.retrofit

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Class that model that data that come from API calls.
 * Exclusively model for Retrofit API calls.
 */


data class SpareRoomNetworkEntity(
    @SerializedName("image_url")
    @Expose
    var image_url: String,

    @SerializedName("location")
    @Expose
    var location: String,

    @SerializedName("phone_number")
    @Expose
    var phone_number: String,

    @SerializedName("venue")
    @Expose
    var venue: String,

    @SerializedName("start_time")
    @Expose
    var start_time: String,

    @SerializedName("end_time")
    @Expose
    var end_time: String,

    @SerializedName("cost")
    @Expose
    var cost: String
)
