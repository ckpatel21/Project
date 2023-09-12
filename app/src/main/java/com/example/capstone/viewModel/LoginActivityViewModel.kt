package com.example.capstone.viewModel

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.capstone.model.User
import com.example.capstone.ui.HomeActivity
import com.example.capstone.ui.LoginActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LoginActivityViewModel : ViewModel() {

    var mAuth = FirebaseAuth.getInstance()

    //var user :Task<AuthResult> = Task<AuthResult>

    /*val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent = result.data
            val task = GoogleSignIn.getSignedInAccountFromIntent(intent)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
            }
        }
    }*/
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

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, /*accessToken=*/ null)
       // user = mAuth.signInWithCredential(credential)
            /*.addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = mAuth.currentUser
                    val email = user?.email.toString()
                    val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                    //intent.putExtra("Email",email)
                    startActivity(intent)
                    //updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this@LoginActivity, "Authentication Failed.", Toast.LENGTH_SHORT).show()
                    Log.d("Data","Failed")
                    //updateUI(null)
                }
            }*/
    }
}