package com.example.tubes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class editData : AppCompatActivity() {
    private lateinit var dbRef: DatabaseReference
    private lateinit var editTextNama: TextInputEditText
    private lateinit var editTextAlamat: TextInputEditText
    private lateinit var editTextTelepon: TextInputEditText
    private lateinit var editTextKejadian: TextInputEditText
    private lateinit var btnSimpan: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_data)

        // Inisialisasi UI
        editTextNama = findViewById(R.id.textInputNama)
        editTextAlamat = findViewById(R.id.textInputLocation)
        editTextTelepon = findViewById(R.id.textInputTelepon)
        editTextKejadian = findViewById(R.id.textInput)
        btnSimpan = findViewById(R.id.btnKirim)

        // Mendapatkan ID data yang akan diedit dari Intent
        val penggunaId = intent.getStringExtra("PENGGUNA_ID")

        // Mengambil data dari Firebase
        dbRef = FirebaseDatabase.getInstance().getReference("Data pengaduan").child(penggunaId!!)
        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val pengguna = snapshot.getValue(pengguna::class.java)
                if (pengguna != null) {
                    // Menampilkan data pada EditText
                    editTextNama.setText(pengguna.namaPelapor)
                    editTextAlamat.setText(pengguna.Alamat)
                    editTextTelepon.setText(pengguna.noTelepon)
                    editTextKejadian.setText(pengguna.kejadian)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })

        // Menyimpan perubahan saat tombol "Simpan Perubahan" ditekan
        btnSimpan.setOnClickListener {
            val newNamaPelapor = editTextNama.text.toString()
            val newAlamat = editTextAlamat.text.toString()
            val newNoTelepon = editTextTelepon.text.toString()
            val newKejadian = editTextKejadian.text.toString()

            // Simpan perubahan ke Firebase
            dbRef.child("namaPelapor").setValue(newNamaPelapor)
            dbRef.child("alamat").setValue(newAlamat)
            dbRef.child("noTelepon").setValue(newNoTelepon)
            dbRef.child("kejadian").setValue(newKejadian)
                .addOnCompleteListener {
                    Toast.makeText(this, "Perubahan berhasil disimpan", Toast.LENGTH_LONG).show()
                    finish() // Menutup activity setelah penyimpanan berhasil
                }
                .addOnFailureListener { err ->
                    Toast.makeText(this, "Gagal menyimpan perubahan: ${err.message}", Toast.LENGTH_LONG).show()
                }
        }
    }
}