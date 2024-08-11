package com.example.firebaseapp.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.example.firebaseapp.R
import com.example.firebaseapp.databinding.ActivityLoginBinding
import com.example.firebaseapp.base.BaseActivity
import com.example.firebaseapp.ui.MainViewModel
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlin.reflect.KClass

class LoginActivity : BaseActivity<ActivityLoginBinding, MainViewModel>() {

    private var mAuth: FirebaseAuth? = null
    lateinit var callbackManager: CallbackManager
    private val RC_SIGN_IN = 9001

    override fun resourceId(): Int = R.layout.activity_login

    override fun viewModelClass(): KClass<MainViewModel> = MainViewModel::class

    override fun setUI(savedInstanceState: Bundle?) {
        window.statusBarColor = ContextCompat.getColor(this,R.color.black)
        mAuth=FirebaseAuth.getInstance()
        loginWithFacebook()
    }

    override fun clicks() {
        dataBinding.tvSignUp.setOnClickListener {
            val intent=Intent(this,SignUpActivity::class.java)
            startActivity(intent)
        }
        dataBinding.btnLogin.setOnClickListener {
            loginWithEmailPass(
                dataBinding.etEmail.text.toString(),
                dataBinding.etPassword.text.toString()
            )
        }

        dataBinding.btnGoogle.setOnClickListener {
            signIn()
        }
    }

    override fun callApis() {
    }

    override fun observer() {
    }

    private fun loginWithEmailPass(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "complete data", Toast.LENGTH_SHORT).show()
            return
        }
        dataBinding.progressBar.isVisible=true
        mAuth?.signInWithEmailAndPassword(email, password)
            ?.addOnCompleteListener {
                dataBinding.progressBar.isVisible=false
                if (it.isSuccessful) {
                    Toast.makeText(this, "done", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finishAffinity()
                } else Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()

            }
    }

    private fun loginWithFacebook(){
        callbackManager = CallbackManager.Factory.create()
        dataBinding.btFacebook.setReadPermissions("email", "public_profile")
        dataBinding.btFacebook.registerCallback(
            callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    Toast.makeText(this@LoginActivity, "facebook:onSuccess:$loginResult", Toast.LENGTH_SHORT).show()
                    handleFacebookAccessToken(loginResult.accessToken)
                }

                override fun onCancel() {
                    Toast.makeText(this@LoginActivity, "facebook:onCancel", Toast.LENGTH_SHORT).show()

                }

                override fun onError(error: FacebookException) {
                    Toast.makeText(this@LoginActivity, "facebook:onError", Toast.LENGTH_SHORT).show()

                }
            },
        )
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        Toast.makeText(this, "handleFacebookAccessToken:$token", Toast.LENGTH_SHORT).show()

        val credential = FacebookAuthProvider.getCredential(token.token)
        mAuth?.signInWithCredential(credential)
            ?.addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "signInWithCredential:success", Toast.LENGTH_SHORT).show()
                    val user = mAuth?.currentUser
                } else {
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }

    private fun signIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(this, gso)
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Toast.makeText(this, "Google sign in failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth?.signInWithCredential(credential)
            ?.addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = mAuth?.currentUser
                    Toast.makeText(this, "Signed in as ${user?.displayName}", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finishAffinity()
                } else {
                    Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
                }
            }
    }
}