package ir.composenews.network

sealed interface ApiResponse<out T> {
    data class Success<T>(val data: T) : ApiResponse<T>

    /**
     * There are two subtypes: [ApiResponse.Failure.Error] and [ApiResponse.Failure.Exception].
     */
    sealed interface Failure<T> : ApiResponse<T> {

        /**
         * API communication conventions do not match or applications need to handle errors.
         * e.g., internal server error and etc...
         */
        data class Error(val payload: Any?) : Failure<Nothing> {
            val message = payload.toString()
        }

        /**
         * An unexpected exception occurs while creating requests or processing an response in the client side.
         * e.g., network connection error, timeout and etc...
         */
        data class Exception(val throwable: Throwable) : Failure<Nothing> {
            val message: String? = throwable.message
        }
    }
}
