package better.text.protext

import android.os.Bundle
import better.text.protext.base.baseScreens.BaseActivity
import better.text.protext.base.databinding.AccessibilityListScreenBinding

class MainActivity : BaseActivity<AccessibilityListScreenBinding>() {
    override fun getViewBinding(): AccessibilityListScreenBinding {
        return AccessibilityListScreenBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?, binding: AccessibilityListScreenBinding) {
        binding.apply {
            includedMenuList.apply {
                add.setOnClickListener {
                    this.menuItemsContainer.transitionToEnd()
                }
                delete.setOnClickListener {
                    this.menuItemsContainer.transitionToStart()
                }
            }
        }
    }
}
