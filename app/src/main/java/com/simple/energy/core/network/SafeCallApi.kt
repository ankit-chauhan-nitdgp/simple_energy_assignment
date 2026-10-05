package com.simple.energy.core.network

import retrofit2.HttpException
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException
import com.simple.energy.core.ApiResult

suspend fun <T> safeApiCall(
    apiCall: suspend () -> T
): ApiResult<T> {
    return try {
        // successful call
        ApiResult.Success(apiCall())
    } catch (e: CancellationException) {
        // coroutine cancels the call
        throw e
    } catch (e: HttpException) {
        // server throws error
        ApiResult.Error(
            message = "Server error: ${e.code()}",
            cause = e
        )
    } catch (e: IOException) {
        // network error
        ApiResult.Error(
            message = "Network error. Please check your connection.",
            cause = e
        )
    } catch (e: Exception) {
        // unexpected error
        ApiResult.Error(
            message = "Something went wrong.",
            cause = e
        )
    }
}