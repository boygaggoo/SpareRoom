package com.spareroom.android.retrofit

import retrofit2.http.GET
import retrofit2.http.Headers

interface NetworkService {
    @Headers("secret-key: $2b$10$76APFiNwr0YXKLX6FDCGiuks/TPFnSKkJleMY2uz1AR1EqTK9IODC\n")
    @GET("/b/605479c67ffeba41c07de021")
    suspend fun getUpcomingEvents(): List<SpareRoomNetworkEntity>

}