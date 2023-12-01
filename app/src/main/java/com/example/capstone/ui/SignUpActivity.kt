package com.example.capstone.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.capstone.databinding.ActivitySignUpBinding
import com.example.capstone.utils.EmailValidator
import com.example.capstone.utils.Helper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var activitySignUpBinding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activitySignUpBinding = ActivitySignUpBinding.inflate(layoutInflater)

        supportActionBar?.hide()

        auth = Firebase.auth

        activitySignUpBinding.btnSignup.setOnClickListener {

            val email = activitySignUpBinding.etUserEmail.text.toString()
            val password = activitySignUpBinding.etPassword.text.toString()
            val confirmPassword = activitySignUpBinding.etConfirmPassword.text.toString()

            if(!Helper.nullCheck(email)){
                activitySignUpBinding.etUserEmail.error = "Please Enter Credentials!"

            }else if (!Helper.nullCheck(password)){
                activitySignUpBinding.etPassword.error = "Please Enter Credentials!"
            }else if (!Helper.nullCheck(confirmPassword)){
                activitySignUpBinding.etConfirmPassword.error = "Please Enter Credentials!"
            }else if(password != confirmPassword){
                activitySignUpBinding.etConfirmPassword.error = "Confirm password doesn't Match!"
            }else if(!EmailValidator.isValidEmail(email)){
                activitySignUpBinding.etUserEmail.error = "Please Enter Valid Email!"
            } else{
                //Not Empty
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(this,
                                "User Added Successfully!",
                                Toast.LENGTH_SHORT,
                            ).show()

                            //Clear Data
                            activitySignUpBinding.etUserEmail.text?.clear()
                            activitySignUpBinding.etPassword.text?.clear()
                            activitySignUpBinding.etConfirmPassword.text?.clear()

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(this,
                                "Authentication failed.",
                                Toast.LENGTH_SHORT,
                            ).show()

                            //Clear Data
                            activitySignUpBinding.etUserEmail.text?.clear()
                            activitySignUpBinding.etPassword.text?.clear()
                            activitySignUpBinding.etConfirmPassword.text?.clear()

                        }
                    }
            }

        }

        activitySignUpBinding.btnLoginHere.setOnClickListener {
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
        }
        setContentView(activitySignUpBinding.root)
    }
}