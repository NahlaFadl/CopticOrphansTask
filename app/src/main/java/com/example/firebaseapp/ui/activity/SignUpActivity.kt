package com.example.firebaseapp.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.firebaseapp.R
import com.example.firebaseapp.base.BaseActivity
import com.example.firebaseapp.databinding.ActivitySignUpBinding
import com.example.firebaseapp.ui.MainViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlin.reflect.KClass

class SignUpActivity : BaseActivity<ActivitySignUpBinding, MainViewModel>() {

    private var mAuth: FirebaseAuth? = null

    override fun resourceId(): Int = R.layout.activity_sign_up

    override fun viewModelClass(): KClass<MainViewModel> = MainViewModel::class

    override fun setUI(savedInstanceState: Bundle?) {
        window.statusBarColor = ContextCompat.getColor(this,R.color.black)
        mAuth = FirebaseAuth.getInstance()
    }

    override fun clicks() {
        dataBinding.btnRegister.setOnClickListener {
            signUp(dataBinding.etEmail.text.toString(), dataBinding.etPassword.text.toString())
        }
    }

    override fun callApis() {
    }

    override fun observer() {
    }

    private fun signUp(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "complete data", Toast.LENGTH_SHORT).show()
            return
        }
        mAuth?.createUserWithEmailAndPassword(email, password)
            ?.addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(this, "done", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this,MainActivity::class.java))
                    finishAffinity()
                }
                else Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
            }
    }
}