package better.text.protext.ui.bookmarks.adapters

import android.net.Uri
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.recyclerview.selection.SelectionTracker
import better.text.protext.base.baseAdapters.SelectableViewHolder
import better.text.protext.base.utils.Validators
import better.text.protext.localdata.database.entities.Bookmark
import better.text.protext.ui.bookmarks.R
import better.text.protext.ui.bookmarks.databinding.BookmarkListItemBinding
import coil.load

class BookmarksViewHolder(
    private val binding: BookmarkListItemBinding,
    private val tracker: SelectionTracker<Long>
) : SelectableViewHolder<Bookmark, Long>(
    binding = binding,
    tracker = tracker
) {
    override val itemId: Long
        get() = item.id

    private val transitionListener = object : MotionLayout.TransitionListener {
        private var changedCheckedFromStart = false
        private var changedCheckedFromEnd = true
        override fun onTransitionStarted(motionLayout: MotionLayout?, startId: Int, endId: Int) {
//            if (startId == R.id.start) {
//                changedCheckedFromStart = false
//                changedCheckedFromEnd = true
//            } else {
//
//            }
        }

        override fun onTransitionChange(
            motionLayout: MotionLayout?,
            startId: Int,
            endId: Int,
            progress: Float
        ) {
            if (progress >= 0.5f && !changedCheckedFromStart) {
                changedCheckedFromStart = true
                changedCheckedFromEnd = false
                binding.ivWebsite.foreground = AppCompatResources
                    .getDrawable(
                        binding.root.context,
                        better.text.protext.base.R.drawable.ic_check
                    )
            } else if (progress < 0.5f && !changedCheckedFromEnd) {
                changedCheckedFromEnd = true
                changedCheckedFromStart = false
                binding.ivWebsite.foreground = null
            }
        }

        override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
        }

        override fun onTransitionTrigger(
            motionLayout: MotionLayout?,
            triggerId: Int,
            positive: Boolean,
            progress: Float
        ) {
        }
    }

    override fun bind(item: Bookmark, onItemClick: (Bookmark) -> Unit) {
        binding.apply {
            tvBookmarkTitle.text = item.bookmarkTitle
            tvLink.text = item.bookmarkUrl
            tvTime.text = item.lastUpdated.stringDate
            itemCard.setOnClickListener {
                onItemClick(item)
            }
            itemCard.isChecked = tracker.isSelected(itemId)
            if (tracker.isSelected(itemId)) {
                if (constraintLayout.currentState == R.id.start) {
                    constraintLayout.transitionToEnd()
                }
            } else {
                if (constraintLayout.currentState == R.id.end) {
                    constraintLayout.transitionToStart()
                }
            }
            getWebsiteLogoUrl().let {
                if (it.isNotEmpty()) {
                    ivWebsite.load(it) {
                        crossfade(true)
                        placeholder(
                            AppCompatResources.getDrawable(
                                binding.root.context,
                                better.text.protext.base.R.color.image_placeholder
                            )
                        )
                    }
                }
            }
            binding.root.setOnClickListener {
                onItemClick(item)
            }
        }
    }

    private fun getWebsiteLogoUrl(): String {
        val httpsString = "https://"
        var url = item.bookmarkUrl
        if (!url.startsWith(httpsString)) {
            url = httpsString + url
        }
        var domain = Uri.parse(url).host ?: return ""
        domain = httpsString + domain
        if (!Validators.validateUrl(domain)) return ""
        return "https://t2.gstatic.com/faviconV2?client=SOCIAL&type=FAVICON&fallback_opts=TYPE,SIZE,URL&url=$domain&size=128"
    }

    fun attachTransitionListener() {
        binding.constraintLayout.addTransitionListener(transitionListener)
    }

    fun detachTransitionListener() {
        binding.constraintLayout.removeTransitionListener(transitionListener)
    }
}
