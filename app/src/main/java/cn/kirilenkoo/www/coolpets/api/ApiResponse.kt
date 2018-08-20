package cn.kirilenkoo.www.coolpets.api

sealed class ApiResponse<T> {
    companion object {
        fun <T> create(error: Throwable): ApiErrorResponse<T> {
            return ApiErrorResponse(error.message ?: "unknown error")
        }

        fun <T> create(data: T ): ApiResponse<T> {
            return if (data == null) {
                ApiEmptyResponse()
            } else {
                ApiSuccessResponse(data)
            }
        }
    }
}
class ApiEmptyResponse<T> : ApiResponse<T>()

data class ApiSuccessResponse<T>(val body: T) : ApiResponse<T>()

data class ApiErrorResponse<T>(val errorMessage: String) : ApiResponse<T>()