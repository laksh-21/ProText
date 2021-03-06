package better.text.protext.ui.bookmarks.utils

import androidx.recyclerview.selection.ItemKeyProvider
import better.text.protext.ui.bookmarks.adapters.BookmarksAdapter

class BookmarkIdKeyProvider(
    private val adapter: BookmarksAdapter
) : ItemKeyProvider<Long>(SCOPE_CACHED) {
    override fun getKey(position: Int): Long {
        return adapter.snapshot().items[position].id
    }

    override fun getPosition(key: Long): Int {
        return adapter.snapshot().items.indexOfFirst { it.id == key }
    }
}
