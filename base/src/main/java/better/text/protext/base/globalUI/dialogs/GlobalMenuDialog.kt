package better.text.protext.base.globalUI.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import better.text.protext.base.baseScreens.BaseBottomSheetDialog
import better.text.protext.base.databinding.GlobalMenuDialogBinding
import better.text.protext.base.globalUI.adapters.GlobalMenuAdapter
import better.text.protext.base.globalUI.utils.GlobalMenuItem

class GlobalMenuDialog(
    private val menuItems: List<GlobalMenuItem>
) : BaseBottomSheetDialog<GlobalMenuDialogBinding>() {
    override fun inflater(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): GlobalMenuDialogBinding {
        return GlobalMenuDialogBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(binding: GlobalMenuDialogBinding, savedInstanceState: Bundle?) {
        initMenuList()
    }

    private fun initMenuList() {
        binding.apply {
            rvMenu.adapter = GlobalMenuAdapter(menuItems) {
                dismiss()
            }
            rvMenu.layoutManager = LinearLayoutManager(requireContext())
        }
    }
}
