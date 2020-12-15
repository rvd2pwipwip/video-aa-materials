package com.raywenderlich.android.menagerie.ui.login

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.BounceInterpolator
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
      loginViewModel.logIn()
      animateLogin()
    }
  }

  private fun animateLogin() {
    binding.progressBar.alpha = 0f
    binding.progressBar.visibility = View.VISIBLE

    //OBJECT ANIMATORS
    val progressBarAnimator =
      ObjectAnimator.ofFloat(binding.progressBar, "alpha", 0f, 1f)
    progressBarAnimator.duration = 2000
    progressBarAnimator.interpolator = AccelerateDecelerateInterpolator()

    //VALUE ANIMATORS
    val loginButtonAnimator = ValueAnimator.ofFloat(0f, 1f)
    loginButtonAnimator.duration = 2000
//    loginButtonAnimator.interpolator = DecelerateInterpolator()
//    loginButtonAnimator.interpolator = AccelerateDecelerateInterpolator()
    loginButtonAnimator.interpolator = BounceInterpolator()

    val buttonWidth = binding.loginButton.width
    val buttonHeight = binding.loginButton.height

    loginButtonAnimator.addUpdateListener {
      val animatedValue = it.animatedValue as Float
      
      binding.loginButton.alpha = 1 - animatedValue * 1.5f

      binding.loginButton.updateLayoutParams {
        this.height = (buttonHeight * (1f - animatedValue)).toInt()
        this.width = (buttonWidth * (1f - animatedValue)).toInt()
      }
    }

    loginButtonAnimator.start()
    progressBarAnimator.start()
  }

  override fun onLoggedIn() { // todo button animation, transition, progress
    binding.progressBar.visibility = View.GONE
    startActivity(MainActivity.getIntent(this))
  }
}