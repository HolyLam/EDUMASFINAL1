package com.example.tubes

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class dana_bantuan : AppCompatActivity() {
    private lateinit var TInama: TextInputEditText
    private lateinit var TInik: TextInputEditText
    private lateinit var TIalamat: TextInputEditText
    private lateinit var dbRef: DatabaseReference
    private var selectedJob: String = ""
    private lateinit var btnSend: Button
    private val CAMERA_PERMISSION_CODE = 100
    private lateinit var imageButton: ImageButton
    private val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            val selectedImageUri: Uri? = result.data?.data
        }
    }
    private val takePicture = registerForActivityResult(ActivityResultContracts.TakePicture()) { success: Boolean ->
        if (success) {
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dana_bantuan)


        TInama = findViewById(R.id.textInputNama)
        TInik= findViewById(R.id.textInputNIK)
        TIalamat = findViewById(R.id.textInputAlamat)
        btnSend = findViewById(R.id.btnSend)



        // Mendapatkan referensi ke Spinner
        val spinnerJob: Spinner = findViewById(R.id.spinnerJob)

        // Daftar pekerjaan yang akan ditampilkan di Spinner
        val jobList = listOf("Petani", "Buruh Harian Lepas", "Pedagang", "Pengangguran", "Lain-lain")

        // Membuat adapter untuk Spinner
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, jobList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Mengatur adapter ke Spinner
        spinnerJob.adapter = adapter

        // Menangani item yang dipilih dari Spinner
        spinnerJob.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedJob = jobList[position]
                saveSelectedJobToDatabase(selectedJob)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Tidak melakukan apa-apa ketika tidak ada yang dipilih
            }
        })

        imageButton = findViewById<ImageButton>(R.id.imageButton)
        imageButton.setOnClickListener {
            checkPermission()
        }
        // Move the setOnClickListener outside the saveSelectedJobToDatabase function
        btnSend.setOnClickListener {
            saveDataDanaBantuan()
        }
    }
        private fun checkPermission() {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED
            ) {
                // Request Camera permission
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.CAMERA),
                    CAMERA_PERMISSION_CODE
                )
            } else {
                // Permission already granted
                showOptionsDialog()
            }
        }
        private fun showOptionsDialog() {
            val items = arrayOf("Pick Image from Gallery", "Take Picture")
            val builder = androidx.appcompat.app.AlertDialog.Builder(this)
            builder.setTitle("Select Option")
            builder.setItems(items) { _, which ->
                when (which) {
                    0 -> openImagePicker()
                    1 -> takePicture()
                }
            }
            builder.show()
        }

        private fun openImagePicker() {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            getContent.launch(intent)

        }

        private fun takePicture() {
            val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(
                Date()
            )
            val photoFile: File = File.createTempFile("JPEG_${timeStamp}_", ".jpg")
            val photoUri: Uri = Uri.fromFile(photoFile)
            takePicture.launch(photoUri)
        }

        override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
        ) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            if (requestCode == CAMERA_PERMISSION_CODE) {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showOptionsDialog()
                }
            }
        }
        override fun onBackPressed() {
            // Add your back button functionality here
            // For example, you can navigate back to the previous activity or perform some other action.
            super.onBackPressed()
        }
        override fun onSupportNavigateUp(): Boolean {
            onBackPressed()
            return true
        }
    fun saveSelectedJobToDatabase(selectedJob: String) {
        // Di sini Anda dapat menyimpan pekerjaan yang dipilih ke dalam database
        // Anda dapat menggunakan nilai "selectedJob" dan menyimpannya bersamaan dengan data lainnya.
        this.selectedJob = selectedJob
        dbRef = FirebaseDatabase.getInstance().getReference("Data Penerima Dana Bantuan")
    }
    fun saveDataDanaBantuan() {
        // Use the class-level selectedJob variable
        val nama = TInama.text.toString()
        val nik = TInik.text.toString()
        val alamat = TIalamat.text.toString()

        if (nama.isEmpty()) {
            TInama.error = "Tulis nama anda"
        }
        if (nik.isEmpty()) {
            TInik.error = "Tolong masukkan alamat dengan benar"
        }
        if (alamat.isEmpty()) {
            TIalamat.error = "Masukkan nomor telepon dengan benar"
        }
        val Id = dbRef.push().key!!
        val bantuan = bantuan(Id, nama, nik, alamat, selectedJob)
        dbRef.child(Id).setValue(bantuan)
            .addOnCompleteListener {
                Toast.makeText(this, "Sukses memasukkan data", Toast.LENGTH_LONG).show()
                TInama.text?.clear()
                TInik.text?.clear()
                TIalamat.text?.clear()
            }.addOnFailureListener { err ->
                Toast.makeText(this, "gagal ${err.message}", Toast.LENGTH_LONG).show()
            }
    }
}