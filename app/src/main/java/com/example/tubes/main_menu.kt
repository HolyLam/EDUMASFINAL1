package com.example.tubes

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class main_menu : AppCompatActivity() {
    private lateinit var logoProfile: ImageView
    private lateinit var historyPage: ImageView
    private lateinit var setting: ImageView
    private lateinit var btnShowPopup: ImageView
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        val cardPengaduan: CardView = findViewById(R.id.laporanPengaduan)
        cardPengaduan.setOnClickListener {
            val pindahPengaduan = Intent(this, main_pengaduan::class.java)
            startActivity(pindahPengaduan)
        }
        val cardData: CardView = findViewById(R.id.dataKeluarga)
        cardData.setOnClickListener {
            val pindahCardKeluarga = Intent(this, form_data_keluarga::class.java)
            startActivity(pindahCardKeluarga)
        }
        val cardLayanan: CardView = findViewById(R.id.layananTerdekat)
        cardLayanan.setOnClickListener{
            val layanan = Intent(this, layananTerdekat::class.java)
            startActivity(layanan)
        }
        val cardBLT: CardView = findViewById(R.id.dana_bantuan)
        cardBLT.setOnClickListener{
            val pindahBLT = Intent(this, dana_bantuan::class.java)
            startActivity(pindahBLT)
        }
//        BERITA
        val cardBeritaPertama: CardView = findViewById(R.id.berita1)
        cardBeritaPertama.setOnClickListener{
            val pindahBeritaPertama = Intent(this, berita_pertama::class.java)
            startActivity(pindahBeritaPertama)
        }
        val cardBeritaKedua: CardView = findViewById(R.id.berita2)
        cardBeritaKedua.setOnClickListener{
            val pindahBeritaKedua = Intent(this, berita_kedua::class.java)
            startActivity(pindahBeritaKedua)
        }
        val cardBeritaKetiga: CardView = findViewById(R.id.berita2)
        cardBeritaKetiga.setOnClickListener{
            val pindahBeritaKetiga = Intent(this, berita_kedua::class.java)
            startActivity(pindahBeritaKetiga)
        }


        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        logoProfile = findViewById(R.id.barUserlogoMenu)
        historyPage = findViewById(R.id.history_aduan)
        btnShowPopup = findViewById(R.id.btnShowPopup)
        setting = findViewById(R.id.edit_profile)


        val tvNamaMain: TextView = findViewById(R.id.tvNamaMain)
        val tvEmailMain: TextView = findViewById(R.id.tvEmailMain)
        val tvAlamatMain: TextView = findViewById(R.id.tvAlamatMain)

        val user = auth.currentUser
        if (user != null) {
            val userId = user.uid
            val userRef = database.reference.child("users").child(userId)

            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val namaDariFirebase = snapshot.child("name").getValue(String::class.java)
                    val emailDariFirebase = snapshot.child("email").getValue(String::class.java)
                    val alamatDariFirebase = snapshot.child("alamat").getValue(String::class.java)

                    // Set teks di TextView sesuai dengan data yang didapat dari Firebase
                    tvNamaMain.text = namaDariFirebase ?: ""
                    tvEmailMain.text = emailDariFirebase ?: ""
                    tvAlamatMain.text = alamatDariFirebase ?: ""
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle kegagalan untuk mendapatkan data
                    Toast.makeText(
                        this@main_menu,
                        "Failed to retrieve data",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
        logoProfile.setOnClickListener {
            val intent = Intent(this, profil::class.java)
            startActivity(intent)
        }
        historyPage.setOnClickListener {
            val pindahHistory = Intent(this, history::class.java)
            startActivity(pindahHistory)
        }
        setting.setOnClickListener{
            val ubah = Intent(this, settings::class.java)
            startActivity(ubah)
        }
        btnShowPopup.setOnClickListener {
            showEmergencyPopup()
        }

    }
    private fun showEmergencyPopup() {
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.activity_emergency_call, null)
        dialogBuilder.setView(dialogView)

        val hospitalTextView = dialogView.findViewById<TextView>(R.id.tvHospital)
        val policeTextView = dialogView.findViewById<TextView>(R.id.tvPolice)
        val fireTextView = dialogView.findViewById<TextView>(R.id.tvFire)

        hospitalTextView.setOnClickListener {
            dialEmergencyNumber("tel:(0274) 4463535") // Ganti dengan nomor telepon rumah sakit yang sebenarnya
        }

        policeTextView.setOnClickListener {
            dialEmergencyNumber("tel:(0274) 884444") // Ganti dengan nomor telepon kepolisian yang sebenarnya
        }

        fireTextView.setOnClickListener {
            dialEmergencyNumber("tel: (0274) 587101") // Ganti dengan nomor telepon pemadam kebakaran yang sebenarnya
        }
        val alertDialog = dialogBuilder.create()
        alertDialog.show()
    }
    private fun dialEmergencyNumber(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse(phoneNumber)
        startActivity(intent)
    }
}
