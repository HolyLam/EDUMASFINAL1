package com.example.tubes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class signIn : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    override fun onStart() {
        super.onStart()
        if (auth.currentUser != null) {
            startActivity(Intent(this, main_menu::class.java))
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        auth = FirebaseAuth.getInstance()

        //config email pw signin
        val signInEmail: EditText = findViewById(R.id.isigninEmail)
        val signInPassword: EditText = findViewById(R.id.signInPassword)
        val signInBtn: Button = findViewById(R.id.signInBtn)
        val signUpBtn: Button = findViewById(R.id.tSignUp)


//Sign in email pw

        signUpBtn.setOnClickListener {
            val intent = Intent(this,signUp::class.java)
            startActivity(intent)
            ///to SignUp

        }

        signInBtn.setOnClickListener {
            val email = signInEmail.text.toString()
            val password = signInPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                if (email.isEmpty()) {
                    signInEmail.error = "Enter Your Email address"
                }
                if (password.isEmpty()) {
                    signInPassword.error = "Enter Your Password"
                }
                Toast.makeText(this, "Enter Valid Details", Toast.LENGTH_SHORT).show()
            } else if (!email.matches(emailPattern.toRegex())) {
                signInEmail.error = "Enter Valid email address"
                Toast.makeText(this, "Enter Valid email address", Toast.LENGTH_SHORT).show()
            } else {
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val intent = Intent(this, main_menu::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "Something went wrong, Try Again!", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }
}
