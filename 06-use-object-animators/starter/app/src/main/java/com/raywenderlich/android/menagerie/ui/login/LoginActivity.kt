package com.raywenderlich.android.menagerie.ui.login

import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.BounceInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.updateLayoutParams
import com.raywenderlich.android.menagerie.databinding.ActivityLoginBinding
import com.raywenderlich.android.menagerie.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity(), LoginView {

  private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
  private val loginViewModel by viewModels<LoginViewModel>()

  companion object {
    fun getIntent(context: Context) = Intent(context, LoginActivity::class.java).apply {
      flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    loginViewModel.setView(this)
    loginViewModel.start()

    setContentView(binding.root)
    setupUi()
  }

  private fun setupUi() {
    binding.loginButton.setOnClickListener {
      binding.progressBar.visibility = View.VISIBLE
      loginViewModel.logIn()
      animateLogin()
    }
  }

  private fun animateLogin() {
    binding.progressBar.alpha = 0f
    binding.progressBar.visibility = View.VISIBLE

    val alphaAnimator = ValueAnimator.ofFloat(0f, 1f)
    alphaAnimator.duration = 2000

    val buttonWidth = binding.loginButton.width
    val buttonHeight = binding.loginButton.height

    alphaAnimator.addUpdateListener {
      val animationValue = it.animatedValue as Float

      binding.progressBar.alpha = animationValue
      binding.loginButton.alpha = 1f - animationValue * 1.5f

      binding.loginButton.updateLayoutParams {
        this.height = (buttonHeight * (1f - animationValue)).toInt()
        this.width = (buttonWidth * (1f - animationValue)).toInt()
      }
    }

    // DecelerateInterpolator()
    // AccelerateDecelerateInterpolator()
    alphaAnimator.interpolator = BounceInterpolator()

    alphaAnimator.start()
  }

  override fun onLoggedIn() { // todo button animation, transition, progress
    binding.progressBar.visibility = View.GONE
    startActivity(MainActivity.getIntent(this))
  }
}