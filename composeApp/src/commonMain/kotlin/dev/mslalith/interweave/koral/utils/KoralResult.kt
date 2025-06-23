package dev.mslalith.interweave.koral.utils

import kotlin.coroutines.cancellation.CancellationException

sealed interface KoralResult<out T> {
    data class Success<T>(val data: T) : KoralResult<T>
    data class ServerError(val code: Int, val message: String?) : KoralResult<Nothing>
    data class TechnicalError(val cause: Throwable) : KoralResult<Nothing>
}

val KoralResult<*>.isSuccess: Boolean
    get() = this is KoralResult.Success

inline fun <T> koralResult(
    block: () -> T
): KoralResult<T> = try {
    KoralResult.Success(block())
} catch (e: CancellationException) {
    throw e
} catch (e: Exception) {
    KoralResult.TechnicalError(e)
}

inline fun <T> KoralResult<T>.onSuccess(
    block: (T) -> Unit
): KoralResult<T> {
    if (this is KoralResult.Success) block(data)
    return this
}

inline fun <T> KoralResult<T>.onFailure(
    block: () -> Unit
): KoralResult<T> {
    if (this !is KoralResult.Success) block()
    return this
}

inline fun <T, R> KoralResult<T>.mapSuccess(
    block: (T) -> R
): KoralResult<R> = when (this) {
    is KoralResult.Success<T> -> KoralResult.Success(data = block(data))
    is KoralResult.ServerError -> this
    is KoralResult.TechnicalError -> this
}
