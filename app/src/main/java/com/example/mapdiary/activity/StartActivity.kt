package com.example.mapdiary.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.mapdiary.R
import com.example.mapdiary.databinding.ActivityMainLogInBinding

class StartActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainLogInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        startLoading()
        binding.btnLogIn.setOnClickListener {
            val intent = Intent(this@StartActivity, LoginActivity::class.java)
            startActivity(intent)
        }
        binding.btnSignUp.setOnClickListener {
            val intent = Intent(this@StartActivity, SignUpActivity::class.java)
            startActivity(intent)
        }
        binding.googleLoginButton.setOnClickListener {
            val intent = Intent(this@StartActivity, GoogleLogin::class.java)
            startActivity(intent)
        }
    }

    fun startLoading() {
        Glide.with(this).load(R.raw.loading2).into(binding.imageView)
    }
}