package com.example.mapdiary.activity

import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.mapdiary.BuildConfig
import com.example.mapdiary.R
import com.example.mapdiary.adapter.ViewPagerAdapter
import com.example.mapdiary.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.kakao.util.maps.helper.Utility

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    lateinit var binding: ActivityMainBinding
    lateinit var viewPagerAdapter: ViewPagerAdapter
    var exitFlag = false
    private var isInitialized = false // 초기화 상태를 추적하는 변수

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        BuildConfig.APPLICATION_ID
        //Fragment를 customViewpagerAdapter에 추가
        viewPagerAdapter = ViewPagerAdapter(this)
        //안드로이드 화면 세로 고정하기
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

        // 툴바 초기화 및 설정
        val toolbar = findViewById<Toolbar>(R.id.toolbar) // R.id.toolbar는 레이아웃에서 정의한 툴바의 ID입니다.
        setSupportActionBar(toolbar)

        // 초기화가 아직 수행되지 않았다면 초기화 작업을 실행
        if (!isInitialized) {
            initNavigationComponent()
            isInitialized = true
        }

        Log.d(TAG, "keyhash : ${Utility.getKeyHash(this)}")

    }

    // 네비게이션 설정 초기화 함수
    private fun initNavigationComponent() {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_chatroom, R.id.navigation_userpage,
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)
    }

    fun runDelayed(millis: Long, function: () -> Unit) {
        Handler(Looper.getMainLooper()).postDelayed(function, millis)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        TODO("Not yet implemented")
    }

    override fun onBackPressed() {
        if (exitFlag) {
            finishAffinity()
        } else {
            Toast.makeText(this, R.string.MA_string1, Toast.LENGTH_SHORT).show()
            exitFlag = true
            runDelayed(1500) {
                exitFlag = false
            }
        }
    }
}