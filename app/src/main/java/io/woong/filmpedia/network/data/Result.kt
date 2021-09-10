package io.woong.filmpedia.network.data

import okhttp3.ResponseBody

/**
 * Wrapper class for the retrofit callbacks.
 */
sealed class Result<out D> {

    /**
     * Wrapper class for successful retrofit callbacks.
     */
    data class Success<D>(val data: D?) : Result<D>()

    /**
     * Wrapper class for failed retrofit callbacks.
     */
    data class Failure(val code: Int, val errorBody: ResponseBody?) : Result<Nothing>()

    /**
     * Singleton object for network error retrofit callbacks.
     */
    object NetworkError : Result<Nothing>()

    fun onSuccess(action: (data: D) -> Unit): Result<D> {
        if (this is Success) {
            this.data?.let {
                action(it)
            }
        }
        return this
    }

    fun onFailure(action: (code: Int, errorBody: ResponseBody?) -> Unit): Result<D> {
        if (this is Failure) {
            action(this.code, this.errorBody)
        }
        return this
    }

    fun onNetworkError(action: () -> Unit): Result<D> {
        if (this is NetworkError) {
            action()
        }
        return this
    }
}
