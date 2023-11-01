package com.example.mapdiary.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.mapdiary.R
import com.example.mapdiary.databinding.ActivityLoadingBinding
import com.example.mapdiary.dataclass.LoginViewModel
import com.google.firebase.auth.FirebaseAuth

class CheckLoginActivity : AppCompatActivity() {
    // Firebase
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val viewModel by viewModels<LoginViewModel>()
    lateinit var binding: ActivityLoadingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoadingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setContent {
            Surface(color = Color.White) {
                Text(text = "로그인 확인중", fontSize = 30.sp)
            }
        }
        checkLogin()
    }

    private fun checkLogin() {
        viewModel.tryLogin(this)
        lifecycleScope.launchWhenCreated {
            viewModel.loginResult.collect { isLogin ->
                if (isLogin) {
                    if (auth.currentUser != null) {
                        // 로그인 되어 있을 때 메인 페이지 열림
                        startMainActivity()
                    }
                } else {
                    // 로그인 안되어있을 때 로딩 페이지 열림
                    startLoading()
                }
            }
        }
    }

    private fun startMainActivity() {
        val intent = Intent(this@CheckLoginActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun startLoading() {
        Handler().postDelayed({
            val intent = if (isLoggedIn()) {
                Intent(this@CheckLoginActivity, MainActivity::class.java)
            } else {
                Intent(this@CheckLoginActivity, StartActivity::class.java)
            }
            startActivity(intent)
            finish()
        }, 3540)
        Glide.with(this).load(R.raw.loading).into(binding.ivPicture)
    }

    private fun isLoggedIn(): Boolean {
        val spf = getSharedPreferences("loginKeep", Context.MODE_PRIVATE)
        return spf.getBoolean("isLogin", false)
    }
}
