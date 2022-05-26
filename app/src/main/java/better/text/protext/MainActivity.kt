package better.text.protext

import android.os.Bundle
import better.text.protext.base.baseScreens.BaseActivity
import better.text.protext.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    override fun getViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?, binding: ActivityMainBinding) {
    }
}
