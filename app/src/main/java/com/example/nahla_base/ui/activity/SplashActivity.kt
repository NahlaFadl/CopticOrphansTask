package com.example.nahla_base.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.content.ContextCompat
import com.example.firebaseapp.R
import com.example.firebaseapp.base.BaseActivity
import com.example.firebaseapp.databinding.ActivitySplashBinding
import com.example.firebaseapp.ui.MainViewModel
import com.example.firebaseapp.ui.activity.LoginActivity
import com.example.firebaseapp.ui.activity.MainActivity
import com.google.firebase.auth.FirebaseAuth
import kotlin.reflect.KClass

class SplashActivity : BaseActivity<ActivitySplashBinding, MainViewModel>() {

    private lateinit var auth: FirebaseAuth

    override fun resourceId(): Int = R.layout.activity_splash

    override fun viewModelClass(): KClass<MainViewModel> = MainViewModel::class

    override fun setUI(savedInstanceState: Bundle?) {
        window.statusBarColor = ContextCompat.getColor(this,R.color.black)
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        Handler(Looper.getMainLooper()).postDelayed({
            if (currentUser != null) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finishAffinity()
            }else{
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finishAffinity()
            }
        }, 1500)
    }

    override fun clicks() {
    }

    override fun callApis() {
    }

    override fun observer() {
    }

}