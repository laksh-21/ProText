package better.text.protext.interactors.copiedTexts

import better.text.protext.base.di.IODispatcher
import better.text.protext.interactors.Interactors
import better.text.protext.localdata.database.entities.CopiedText
import better.text.protext.repository.copiedTexts.CopiedTextRepo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetCopiedTextUseCase @Inject constructor(
    @IODispatcher private val coroutineDispatcher: CoroutineDispatcher,
    private val copiedTextRepo: CopiedTextRepo
) : Interactors.SuspendUseCase<GetCopiedTextUseCase.Params, CopiedText>() {
    data class Params(
        val id: Long
    )

    override suspend fun doWork(params: Params): CopiedText = withContext(coroutineDispatcher) {
        copiedTextRepo.getCopiedText(params.id)
    }
}
