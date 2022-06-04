package better.text.protext.repository.bookmarks

import better.text.protext.remotedata.bookmarks.WebsiteDao
import javax.inject.Inject

class WebsiteInfoRepoImpl @Inject constructor(
    private val websiteDao: WebsiteDao
) : WebsiteInfoRepo {
    override suspend fun getWebsiteTitle(url: String): String {
        return websiteDao.getWebsiteTitle(url)
    }
}
