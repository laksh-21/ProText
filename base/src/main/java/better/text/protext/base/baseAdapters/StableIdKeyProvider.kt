package better.text.protext.base.baseAdapters

import android.view.View
import androidx.recyclerview.selection.ItemKeyProvider
import androidx.recyclerview.widget.RecyclerView

@Suppress("UNCHECKED_CAST")
class StableIdKeyProvider<KT : Any>(
    private val mRecyclerView: RecyclerView,
) : ItemKeyProvider<KT>(SCOPE_CACHED) {

    init {
        mRecyclerView.addOnChildAttachStateChangeListener(
            object : RecyclerView.OnChildAttachStateChangeListener {
                override fun onChildViewAttachedToWindow(view: View) {
                    onAttached(view)
                }

                override fun onChildViewDetachedFromWindow(view: View) {
                    onDetached(view)
                }
            }
        )
    }

    private val mPositionToKey = mutableMapOf<Int, KT>()
    private val mKeyToPosition = mutableMapOf<KT, Int>()

    fun onAttached(view: View) {
        val holder = mRecyclerView.findContainingViewHolder(view) ?: return
        val position = holder.bindingAdapterPosition
        val itemId = (holder as SelectableViewHolder<*, KT>).itemId
        if (position != RecyclerView.NO_POSITION && itemId != null) {
            mPositionToKey[position] = itemId
            mKeyToPosition[itemId] = position
        }
    }

    fun onDetached(view: View) {
        val holder = mRecyclerView.findContainingViewHolder(view) ?: return
        val position = holder.bindingAdapterPosition
        val myItemId = (holder as SelectableViewHolder<*, KT>).itemId
        if (position != RecyclerView.NO_POSITION && myItemId != null) {
            mPositionToKey.remove(position)
            mKeyToPosition.remove(myItemId)
        }
    }
    override fun getKey(position: Int): KT? {
        return mPositionToKey[position]
    }

    override fun getPosition(key: KT): Int {
        return mKeyToPosition[key] ?: RecyclerView.NO_POSITION
    }
}
