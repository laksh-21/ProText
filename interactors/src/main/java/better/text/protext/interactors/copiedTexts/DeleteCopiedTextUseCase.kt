package better.text.protext.interactors.copiedTexts

import better.text.protext.interactors.Interactors
import better.text.protext.repository.copiedTexts.CopiedTextRepo
import javax.inject.Inject

class DeleteCopiedTextUseCase @Inject constructor(
    private val copiedTextRepo: CopiedTextRepo
) : Interactors.SuspendUseCase<DeleteCopiedTextUseCase.Params, Unit>() {
    data class Params(
        val copiedTexts: List<Long>
    )

    override suspend fun doWork(params: Params) {
        copiedTextRepo.deleteCopiedText(params.copiedTexts)
    }
}
