package com.example.tubes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class profil : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var home: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profil)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        home = findViewById(R.id.homepage)

        val tvNama: TextView = findViewById(R.id.tvNama)
        val tvEmail: TextView = findViewById(R.id.tvEmail)
        val tvTTL: TextView = findViewById(R.id.tvTTL)

        // Mendapatkan data pengguna dari Firebase setelah berhasil login
        val user = auth.currentUser
        if (user != null) {
            val userId = user.uid
            val userRef = database.reference.child("users").child(userId)

            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val namaDariFirebase = snapshot.child("name").getValue(String::class.java)
                    val emailDariFirebase = snapshot.child("email").getValue(String::class.java)
                    val ttlDariFirebase = snapshot.child("ttl").getValue(String::class.java)

                    // Set teks di TextView sesuai dengan data yang didapat dari Firebase
                    tvNama.text = namaDariFirebase ?: ""
                    tvEmail.text = emailDariFirebase ?: ""
                    tvTTL.text = ttlDariFirebase ?: ""
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle kegagalan untuk mendapatkan data
                    Toast.makeText(
                        this@profil,
                        "Failed to retrieve data",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
        home.setOnClickListener{
            val ubah = Intent(this, main_menu::class.java)
            startActivity(ubah)
        }
        val intent = intent
        val namaKepala = intent.getStringExtra("namaKepala")

        val greetingMessage = "Halo, keluarga bapak $namaKepala"

        // Find the TextView by its ID
        val textViewGreeting: TextView = findViewById(R.id.textViewGreeting)

        // Set the greeting message to the TextView
        textViewGreeting.text = greetingMessage
    }
}

