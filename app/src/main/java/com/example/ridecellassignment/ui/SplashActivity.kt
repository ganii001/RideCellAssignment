package com.example.ridecellassignment.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import com.example.ridecellassignment.ConstantClass
import com.example.ridecellassignment.MyPreferenses
import com.example.ridecellassignment.R
import com.example.ridecellassignment.Utils
import com.example.ridecellassignment.databinding.ActivityDashboardBinding
import com.example.ridecellassignment.databinding.ActivitySplashBinding
import com.example.ridecellassignment.ui.auth.LoginActivity
import com.example.ridecellassignment.ui.auth.LoginViewModel
import com.example.ridecellassignment.ui.dashboard.DashboardActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            if (MyPreferenses(this).getString(ConstantClass.EMAIl, "")
                    ?.isNotEmpty()!! && MyPreferenses(
                    this
                ).getString(ConstantClass.PASSWORD, "")?.isNotEmpty()!!
            ) {
                signInUser()
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }, 1000)

        onSignIn()
    }

    private fun signInUser() {
        if (Utils.isDataConnected(this)) {
            viewModel.doSignIn(
                emailId = MyPreferenses(this).getString(ConstantClass.EMAIl, ""),
                pass = MyPreferenses(this).getString(ConstantClass.PASSWORD, "")
            )
        } else {
            Utils.showSNACK_BAR_NO_INTERNET(this, localClassName)
        }
    }

    private fun onSignIn() {
        viewModel.isSuccessful.observe(this, androidx.lifecycle.Observer { isSuccess ->
            if (isSuccess) {
                startActivity(Intent(this, DashboardActivity::class.java))
                finish()
            }
        })
    }
}