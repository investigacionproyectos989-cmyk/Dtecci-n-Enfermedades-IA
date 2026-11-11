package com.innonorbelis.tomatoia.ui.splash

import android.content.Intent
import androidx.activity.viewModels
import com.innonorbelis.tomatoia.core.util.aniBottom
import com.innonorbelis.tomatoia.core.util.aniBottomtop
import com.innonorbelis.tomatoia.databinding.ActivitySplashBinding
import com.innonorbelis.tomatoia.ui.activity.MainActivity
import com.innonorbelis.tomatoia.ui.activity.SplashBaseActivity
import com.innonorbelis.tomatoia.ui.viewmodel.SplashBaseViewModel
import com.innonorbelis.tomatoia.ui.viewmodel.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : SplashBaseActivity() {

    private lateinit var binding: ActivitySplashBinding
    private  val viewModel: SplashViewModel by viewModels()
    override fun getBinding() {
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun setView() {
        // importar los metodos

        binding.lnBarnerTop.aniBottom()
        binding.lnBarnerBottom.aniBottomtop()

        //llamo las funciones loadHome cultivoViewMOdel
        viewModel.loadHome()
    }

    override fun observerViewModel() {
        viewModel.successSplash.observe(this) { success ->
            if (success) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
    //por si hay algun error
    override fun getViewModel(): SplashBaseViewModel=viewModel
}