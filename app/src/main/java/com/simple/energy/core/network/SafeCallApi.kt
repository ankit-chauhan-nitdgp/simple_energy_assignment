package com.simple.energy.core.network

import retrofit2.HttpException
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException
import com.simple.energy.core.ApiResult

suspend fun <T> safeApiCall(
    apiCall: suspend () -> T
): ApiResult<T> {
    return try {
        ApiResult.Success(apiCall())
    } catch (e: CancellationException) {
        throw e
    } catch (e: HttpException) {
        ApiResult.Error(
            message = "Server error: ${e.code()}",
            cause = e
        )
    } catch (e: IOException) {
        ApiResult.Error(
            message = "Network error. Please check your connection.",
            cause = e
        )
    } catch (e: Exception) {
        ApiResult.Error(
            message = "Something went wrong.",
            cause = e
        )
    }
}