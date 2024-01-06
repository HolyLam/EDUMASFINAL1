package com.example.tubes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class intro : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        val signInButton: Button = findViewById(R.id.SigninbtnIntro)
        val signUpBtn: Button = findViewById(R.id.bSignUp)

        signInButton.setOnClickListener {
            val intent = Intent(this, signIn::class.java)
            startActivity(intent) }

        signUpBtn.setOnClickListener {
            val intent = Intent(this, signUp::class.java)
            startActivity(intent)
        }
    }
}