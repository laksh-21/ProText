package better.text.protext

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.os.bundleOf
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import better.text.protext.base.baseScreens.BaseActivity
import better.text.protext.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    override fun getViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var navController: NavController
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?, binding: ActivityMainBinding) {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.findNavController()
        setupBottomNav()
        checkSplashScreen()
    }

    private fun checkSplashScreen() {
        lifecycleScope.launch {
            viewModel.isOnBoardingDoneFlow.flowWithLifecycle(lifecycle).collectLatest {
                if (!it) {
                    startOnBoarding()
                }
            }
        }
    }

    private fun startOnBoarding() {
        val options = NavOptions.Builder()
            .setPopUpTo(better.text.protext.ui.bookmarks.R.id.ui_bookmarks_nav_graph, true)
            .build()
        navController.navigate(
            better.text.protext.ui_onboarding.R.id.onboarding_nav_graph,
            bundleOf(),
            options
        )
    }

    private fun setupBottomNav() {
        binding.bottomNavigationView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            val showNavBar = destination.parent?.id != better.text.protext.ui_onboarding.R.id.onboarding_nav_graph
            binding.bottomNavigationView.visibility = if (showNavBar) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }
}
