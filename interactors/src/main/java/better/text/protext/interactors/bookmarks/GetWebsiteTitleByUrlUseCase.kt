package better.text.protext.interactors.bookmarks

import better.text.protext.base.di.IODispatcher
import better.text.protext.interactors.Interactors
import better.text.protext.repository.bookmarks.WebsiteInfoRepo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetWebsiteTitleByUrlUseCase @Inject constructor(
    @IODispatcher private val coroutineDispatcher: CoroutineDispatcher,
    private val websiteInfoRepo: WebsiteInfoRepo,
) : Interactors.SuspendUseCase<GetWebsiteTitleByUrlUseCase.Params, String>() {
    data class Params(
        val url: String,
    )

    override suspend fun doWork(params: Params): String = withContext(coroutineDispatcher) {
        websiteInfoRepo.getWebsiteTitle(params.url)
    }
}
