package better.text.protext.interactors.copiedTexts

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import better.text.protext.interactors.Interactors
import better.text.protext.localdata.database.entities.CopiedText
import better.text.protext.repository.copiedTexts.CopiedTextRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllCopiedTextsUseCase @Inject constructor(
    private val copiedTextRepo: CopiedTextRepo
) : Interactors.PagingObserveUseCase<GetAllCopiedTextsUseCase.Params, CopiedText>() {
    data class Params(
        override val pagingConfig: PagingConfig
    ) : Interactors.PagingObserveUseCase.Params

    override suspend fun createObservable(params: Params): Flow<PagingData<CopiedText>> {
        return Pager(config = params.pagingConfig) {
            copiedTextRepo.getCopiedTexts()
        }.flow
    }
}
