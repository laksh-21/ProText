package better.text.protext.base.results

import java.lang.Exception

sealed class InvokeStatus<out T> {
    data class Success<out T>(val data: T) : InvokeStatus<T>()
    data class Failure<out T>(val exception: Exception) : InvokeStatus<T>()
    class Loading<out T> : InvokeStatus<T>()
}
