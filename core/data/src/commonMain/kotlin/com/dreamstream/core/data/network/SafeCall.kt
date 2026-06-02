package com.dreamstream.core.data.network

import com.dreamstream.core.domain.util.DataError
import com.dreamstream.core.domain.util.Result
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.serialization.SerializationException

/**
 * Executes [execute], catches connectivity and serialization exceptions, and
 * translates the [HttpResponse] status into a typed [Result].
 *
 * Usage:
 * ```kotlin
 * val result: Result<MyDto, DataError.Network> = safeCall {
 *     httpClient.get("$BASE_URL/endpoint")
 * }
 * ```
 */
suspend inline fun <reified T> safeCall(
    execute: () -> HttpResponse,
): Result<T, DataError.Network> {
    val response = try {
        execute()
    } catch (e: UnresolvedAddressException) {
        return Result.Error(DataError.Network.NO_INTERNET)
    } catch (e: Exception) {
        return Result.Error(DataError.Network.UNKNOWN)
    }
    return responseToResult(response)
}

/** Translates an [HttpResponse] status code into a typed [Result]. */
suspend inline fun <reified T> responseToResult(
    response: HttpResponse,
): Result<T, DataError.Network> {
    return when (response.status.value) {
        in 200..299 -> {
            try {
                Result.Success(response.body<T>())
            } catch (e: SerializationException) {
                Result.Error(DataError.Network.SERIALIZATION)
            }
        }
        400 -> Result.Error(DataError.Network.BAD_REQUEST)
        401, 403 -> Result.Error(DataError.Network.UNAUTHORIZED)
        404 -> Result.Error(DataError.Network.NOT_FOUND)
        408 -> Result.Error(DataError.Network.REQUEST_TIMEOUT)
        409 -> Result.Error(DataError.Network.CONFLICT)
        413 -> Result.Error(DataError.Network.PAYLOAD_TOO_LARGE)
        429 -> Result.Error(DataError.Network.TOO_MANY_REQUESTS)
        503 -> Result.Error(DataError.Network.SERVICE_UNAVAILABLE)
        in 500..599 -> Result.Error(DataError.Network.SERVER_ERROR)
        else -> Result.Error(DataError.Network.UNKNOWN)
    }
}
