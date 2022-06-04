package better.text.protext.remotedata.bookmarks

import org.jsoup.Jsoup

class WebsiteDao {
    fun getWebsiteTitle(url: String): String {
        val document = Jsoup.connect(url).get()
        return document.title()
    }
}
