package com.example.tubes

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.auth.FirebaseAuth

class signUp : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        val signUpName: TextInputEditText = findViewById(R.id.signUpName)
        val signUpEmail: TextInputEditText = findViewById(R.id.signUpEmail)
        val signUpTTL: TextInputEditText = findViewById(R.id.signUpTTL)
        val signUpAlamat: TextInputEditText = findViewById(R.id.signUpAlamat)
        val signUpPassword: TextInputEditText = findViewById(R.id.signUpPassword)
        val signUpCpassword: TextInputEditText = findViewById(R.id.signUpCpassword)
        val signUpButton: Button = findViewById(R.id.btnSignUp)


        signUpButton.setOnClickListener {
            val name = signUpName.text.toString()
            val email = signUpEmail.text.toString()
            val ttl = signUpTTL.text.toString()
            val alamat = signUpAlamat.text.toString()
            val password = signUpPassword.text.toString()
            val cPassword = signUpCpassword.text.toString()


            if (name.isEmpty() || email.isEmpty() || ttl.isEmpty() ||alamat.isEmpty() || password.isEmpty() || cPassword.isEmpty()) {
                if (name.isEmpty()) {
                    signUpName.error = "Masukkan Nama"
                }
                if (email.isEmpty()) {
                    signUpEmail.error = "Masukkan Email"
                }
                if (ttl.isEmpty()) {
                    signUpTTL.error = "Masukkan Tanggal Lahir"
                }
                if (alamat.isEmpty()) {
                    signUpAlamat.error = "Masukkan Alamat"
                }
                if (password.isEmpty()) {
                    signUpPassword.error = "Masukkan Password"
                }
                if (cPassword.isEmpty()) {
                    signUpCpassword.error = "Masukkan Ulang Password Anda"
                }
                Toast.makeText(this, "Enter Valid Details", Toast.LENGTH_SHORT).show()

            } else if (!email.matches(emailPattern.toRegex())) {
                signUpEmail.error = "Masukkan Alamat Email Dengan Benar"
                Toast.makeText(this, "Masukkan Alamat Email Dengan Benar", Toast.LENGTH_SHORT).show()
            } else if (ttl.length < 7) {
                signUpTTL.error = "Masukkan Tanggal Lahir Yang Benar"
                Toast.makeText(this, "Masukkan Tanggal Lahir Yang Benar", Toast.LENGTH_SHORT).show()
            } else if (password.length < 4) {
                signUpPassword.error = "Masukkan Minimal 4 Kata"
                Toast.makeText(this, "Masukkan Minimal 4 Kata", Toast.LENGTH_SHORT)
                    .show()
            } else if (alamat.length < 4) {
                signUpAlamat.error = "Masukkan Minimal 4 Kata"
                Toast.makeText(this, "Masukkan Minimal 4 Kata", Toast.LENGTH_SHORT)
                    .show()
            }else {
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val databaseRef =
                            database.reference.child("users").child(auth.currentUser!!.uid)
                        val users = Users(auth.currentUser!!.uid, name, email, ttl, alamat)

                        databaseRef.setValue(users).addOnCompleteListener { innerTask ->
                            if (innerTask.isSuccessful) {
                                auth.signOut() // Menambahkan sign-out sebelum intent ke halaman sign-in
                                val intent = Intent(this, signIn::class.java)
                                startActivity(intent)
                            } else {
                                Toast.makeText(
                                    this,
                                    "Terjadi kesalahan, silahkan coba lagi",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    } else {
                        Toast.makeText(this, "Something went Wrong, Try Again!", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }
}
