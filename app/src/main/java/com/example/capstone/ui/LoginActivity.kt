package com.example.capstone.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.capstone.R
import com.example.capstone.databinding.ActivityLoginBinding
import com.example.capstone.utils.Constant.Companion.LOGIN_CREDENTIAL
import com.example.capstone.utils.EmailValidator
import com.example.capstone.utils.Helper
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private var mAuth = FirebaseAuth.getInstance()
    private lateinit var auth: FirebaseAuth

    private lateinit var activityLoginBinding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        activityLoginBinding = ActivityLoginBinding.inflate(layoutInflater)

        auth = Firebase.auth

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        activityLoginBinding.googleSignInButton.setOnClickListener {
            val signInIntent = mGoogleSignInClient.signInIntent
            //startActivityForResult(signInIntent, Constant.RC_SIGN_IN)
            startForResult.launch(signInIntent)
        }

        activityLoginBinding.btnLogin.setOnClickListener {

            val email = activityLoginBinding.etUserEmail.text.toString()
            val password = activityLoginBinding.etPassword.text.toString()

            if(!Helper.nullCheck(email)){
                activityLoginBinding.etUserEmail.error = getString(R.string.error_null_error)
                activityLoginBinding.etUserEmail.requestFocus()
            }else if (!Helper.nullCheck(password)){
                activityLoginBinding.etPassword.error = getString(R.string.error_null_error)
                activityLoginBinding.etPassword.requestFocus()
            }else if(!EmailValidator.isValidEmail(email)){
                activityLoginBinding.etUserEmail.error = getString(R.string.error_valid_email)
                activityLoginBinding.etUserEmail.requestFocus()
            } else{
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            val user = auth.currentUser
                            val email = user?.email.toString()
                            val name = user?.displayName.toString()

                            //Storing Email and Name in Shared preference
                            val sharedPreference =
                                getSharedPreferences(LOGIN_CREDENTIAL, Context.MODE_PRIVATE)
                            val editor = sharedPreference.edit()
                            editor.putString("email", email)
                            editor.putString("name", name)
                            editor.apply()

                            //Send data
                            val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                            intent.putExtra("Email", email)
                            intent.putExtra("Name", name)
                            startActivity(intent)
                            //Store User Information and Navigation
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(
                                baseContext,
                                "Authentication failed.",
                                Toast.LENGTH_SHORT,
                            ).show()
                        }
                    }

            }
        }
        activityLoginBinding.btnSignUpHere.setOnClickListener {
            val intent = Intent(applicationContext, SignUpActivity::class.java)
            startActivity(intent)
        }

        setContentView(activityLoginBinding.root)
    }

    //Gor Google Login
    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                val task = GoogleSignIn.getSignedInAccountFromIntent(intent)
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    val account = task.getResult(ApiException::class.java)
                    account.idToken?.let { firebaseAuthWithGoogle(it) }
                } catch (e: ApiException) {
                    // Google Sign In failed, update UI appropriately
                }
            }
        }
    //Deprecated
    /*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
       super.onActivityResult(requestCode, resultCode, data)
       if (requestCode == Constant.RC_SIGN_IN) {
           val task = GoogleSignIn.getSignedInAccountFromIntent(data)
           try {
               // Google Sign In was successful, authenticate with Firebase
               val account = task.getResult(ApiException::class.java)
               firebaseAuthWithGoogle(account.idToken!!)
           } catch (e: ApiException) {
               // Google Sign In failed, update UI appropriately
           }
       }
   }*/

    //Google Login
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, /*accessToken=*/ null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = mAuth.currentUser
                    val email = user?.email.toString()
                    val name = user?.displayName.toString()

                    //Storing Email and Name in Shared preference
                    val sharedPreference =
                        getSharedPreferences(LOGIN_CREDENTIAL, Context.MODE_PRIVATE)
                    val editor = sharedPreference.edit()
                    editor.putString("email", email)
                    editor.putString("name", name)
                    editor.apply()

                    //Send data
                    val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                    intent.putExtra("Email", email)
                    intent.putExtra("Name", name)
                    startActivity(intent)
                    //updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this@LoginActivity, "Authentication Failed.", Toast.LENGTH_SHORT)
                        .show()
                    Log.d("Data", "Failed")
                    //updateUI(null)
                }
            }
    }

}