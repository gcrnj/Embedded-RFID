package com.giotech.embeddedrfidlogin

import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleSheetApi {

    @GET("/macros/echo")
    suspend fun getSheet(
        @Query("user_content_key") useContentKey: String,
        @Query("lib") lib: String,
    ): List<RfidEntry>
}
