package better.text.protext.ui.bookmarks.adapters

import android.graphics.Color
import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import better.text.protext.localdata.database.entities.BookmarkFolder
import better.text.protext.ui.bookmarks.databinding.BookmarkFolderGridItemBinding

class ColorSelectorViewHolder(
    private val binding: BookmarkFolderGridItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: BookmarkFolder) {
        binding.apply {
            tvFolderName.text = item.folderName
            folderCard.apply {
                setCardBackgroundColor(Color.parseColor(item.folderColor))
                updateLayoutParams {
                    height = ViewGroup.LayoutParams.MATCH_PARENT
                    width = ViewGroup.LayoutParams.MATCH_PARENT
                }
                isFocusable = false
                isClickable = false
            }
        }
    }
}
