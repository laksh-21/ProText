package better.text.protext.interactors

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import better.text.protext.base.results.InvokeStatus
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*

class Interactors {
    abstract class SuspendUseCase<in P, out R> {
        operator fun invoke(params: P): Flow<InvokeStatus<R>> = flow {
            try {
                emit(InvokeStatus.Loading())
                val result = doWork(params)
                emit(InvokeStatus.Success(result))
            } catch (e: Exception) {
                emit(InvokeStatus.Failure(e))
            }
        }
        protected abstract suspend fun doWork(params: P): R
    }

    abstract class PagingObserveUseCase<in P : PagingObserveUseCase.Params, R : Any> {
        private val paramState = MutableSharedFlow<P>(
            replay = 1,
            extraBufferCapacity = 1,
            onBufferOverflow = BufferOverflow.DROP_OLDEST
        )

        @OptIn(ExperimentalCoroutinesApi::class)
        val flow: Flow<PagingData<R>> = paramState
            .distinctUntilChanged()
            .flatMapLatest { createObservable(it) }
            .distinctUntilChanged()

        operator fun invoke(params: P) {
            paramState.tryEmit(params)
        }

        protected abstract suspend fun createObservable(params: P): Flow<PagingData<R>>

        interface Params {
            val pagingConfig: PagingConfig
        }
    }
}
