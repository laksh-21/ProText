package better.text.protext.base.baseScreens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import better.text.protext.base.globalUI.dialogs.GlobalMenuDialog
import better.text.protext.base.globalUI.utils.GlobalMenuItem

abstract class BaseFragment<B : ViewBinding> : Fragment() {
    protected abstract fun inflater(inflater: LayoutInflater, container: ViewGroup?): B

    private var _binding: B? = null
    protected val binding get() = _binding!!
    protected abstract fun onCreateView(binding: B, savedInstanceState: Bundle?)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflater(inflater, container)
        onCreateView(binding, savedInstanceState)
        return binding.root
    }

    fun showMenu(menuItems: List<GlobalMenuItem>) {
        val dialog = GlobalMenuDialog(menuItems)
        dialog.show(childFragmentManager, "Settings")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
