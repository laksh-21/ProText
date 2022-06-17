package better.text.protext.ui_copiedtexts.utils

import androidx.recyclerview.selection.ItemKeyProvider
import better.text.protext.ui_copiedtexts.adapters.CopiedTextAdapter

class CopiedTextItemKeyProvider(
    private val adapter: CopiedTextAdapter
) : ItemKeyProvider<Long>(SCOPE_CACHED) {
    override fun getKey(position: Int): Long {
        return adapter.snapshot().items[position].id
    }

    override fun getPosition(key: Long): Int {
        return adapter.snapshot().items.indexOfFirst { it.id == key }
    }
}
