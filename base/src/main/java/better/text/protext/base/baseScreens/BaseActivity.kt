package better.text.protext.base.baseScreens

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<B : ViewBinding> : AppCompatActivity() {
    protected lateinit var binding: B
    protected abstract fun getViewBinding(): B
    protected abstract fun onCreate(savedInstanceState: Bundle?, binding: B)

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = getViewBinding()
        setContentView(binding.root)
        onCreate(savedInstanceState, binding)
    }
}
