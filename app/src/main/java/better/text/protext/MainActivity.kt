package better.text.protext

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import better.text.protext.base.baseScreens.BaseActivity
import better.text.protext.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    override fun getViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?, binding: ActivityMainBinding) {
        setupBottomNav()
    }

    override fun onResume() {
        super.onResume()
        navController = binding.navHostFragment.findNavController()
    }

    private fun setupBottomNav() {
        binding.bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.bookmarks_nav_item -> {
                    navController.navigate(better.text.protext.ui.bookmarks.R.id.ui_bookmarks_nav_graph)
                    true
                }
                R.id.copied_texts_nav_item -> {
                    navController.navigate(better.text.protext.ui_copiedtexts.R.id.copied_text_nav_graph)
                    true
                }
                R.id.settings_nav_item -> {
                    navController.navigate(better.text.protext.ui_settings.R.id.settings_nav_graph)
                    true
                }
                else -> false
            }
        }
    }
}
