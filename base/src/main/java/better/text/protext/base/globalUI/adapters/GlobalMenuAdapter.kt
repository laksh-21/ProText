package better.text.protext.base.globalUI.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import better.text.protext.base.databinding.GlobalMenuItemBinding
import better.text.protext.base.globalUI.utils.GlobalMenuItem

class GlobalMenuAdapter(
    private val menuItems: List<GlobalMenuItem>,
    private val onItemClick: (GlobalMenuItem) -> Unit
) : RecyclerView.Adapter<GlobalMenuViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GlobalMenuViewHolder {
        return GlobalMenuViewHolder(GlobalMenuItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: GlobalMenuViewHolder, position: Int) {
        holder.bind(menuItems[position], onItemClick)
    }

    override fun getItemCount(): Int {
        return menuItems.size
    }
}
