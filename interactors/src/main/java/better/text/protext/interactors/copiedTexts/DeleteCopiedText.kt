package better.text.protext.interactors.copiedTexts

import better.text.protext.interactors.Interactors
import better.text.protext.localdata.database.entities.CopiedText
import better.text.protext.repository.copiedTexts.CopiedTextRepo
import javax.inject.Inject

class DeleteCopiedText @Inject constructor(
    private val copiedTextRepo: CopiedTextRepo
) : Interactors.SuspendUseCase<DeleteCopiedText.Params, Unit>() {
    data class Params(
        val copiedText: CopiedText
    )

    override suspend fun doWork(params: Params) {
        copiedTextRepo.deleteCopiedText(params.copiedText)
    }
}
