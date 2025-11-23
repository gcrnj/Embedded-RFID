package com.giotech.embeddedrfidlogin

import android.util.Log
import retrofit2.HttpException
import java.io.IOException

class SheetRepository {

    companion object {
        private const val TAG = "SheetRepository"
    }


    suspend fun getEntries(): List<RfidEntry> {
        Log.d(TAG, "getEntries: Getting")
        return try {
            val list = RetrofitProvider.api.getSheet(
                useContentKey = BuildConfig.SHEETS_CONTENT_KEY,
                lib = BuildConfig.SHEETS_LIB,
            )
            Log.d(TAG, "getEntries: $list")
            // sort by date + time descending
            list.sortedByDescending { it.timestamp() }
        } catch (e: IOException) {
            Log.e(TAG, "Network error ${e.message}", e)
            emptyList()
        } catch (e: HttpException) {
            Log.e(TAG, "HTTP error: ${e.code()}", e)
            emptyList()
        } catch (e: Exception) {
            Log.e(TAG, "Unknown error", e)
            emptyList()
        }
    }
}
