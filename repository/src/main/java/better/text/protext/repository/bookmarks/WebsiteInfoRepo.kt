package better.text.protext.repository.bookmarks

interface WebsiteInfoRepo {
    suspend fun getWebsiteTitle(url: String): String
}
