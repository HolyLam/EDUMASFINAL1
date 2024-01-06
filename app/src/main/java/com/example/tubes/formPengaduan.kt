package com.example.tubes

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class formPengaduan : AppCompatActivity() {
    private val CAMERA_PERMISSION_CODE = 100
    private lateinit var imageButton: ImageButton
    private lateinit var itNamaPelapor: TextInputEditText
    private lateinit var itAlamat: TextInputEditText
    private lateinit var itNoTelepon: TextInputEditText
    private lateinit var itKejadian: TextInputEditText
    private lateinit var tvLaporan: TextView
    private lateinit var btnSaveData: Button
    private lateinit var dbRef: DatabaseReference
    private lateinit var btnLihat: Button

    private val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult->
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
        setContentView(R.layout.activity_form_pengaduan)

        FirebaseApp.initializeApp(this)

        imageButton = findViewById<ImageButton>(R.id.imageButton)
        imageButton.setOnClickListener {
            checkPermission()
        }

        tvLaporan = findViewById(R.id.viewReport)
        tvLaporan.text = "Your default text for TextView"
        val reportType = intent.getStringExtra("REPORT_TYPE")
        val textView: TextView = findViewById(R.id.viewReport)
        textView.text = reportType

//        btnLihat = findViewById(R.id.btnLihat)
//        btnLihat.setOnClickListener {
//            val pindah = Intent(this, history::class.java)
//            startActivity(pindah)
//        }

        itNamaPelapor = findViewById(R.id.textInputNama)
        itAlamat = findViewById(R.id.textInputLocation)
        itNoTelepon = findViewById(R.id.textInputTelepon)
        itKejadian = findViewById(R.id.textInput)
        btnSaveData = findViewById(R.id.btnKirim)

        dbRef = FirebaseDatabase.getInstance().getReference("Data pengaduan")
        btnSaveData.setOnClickListener {
            saveDataPengguna()
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
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
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
    fun saveDataPengguna() {
        //getting values
        val namaPelapor = itNamaPelapor.text.toString()
        val Alamat = itAlamat.text.toString()
        val noTelepon = itNoTelepon.text.toString()
        val kejadian = itKejadian.text.toString()
        val laporan = tvLaporan.text.toString()

        if (namaPelapor.isEmpty()) {
            itNamaPelapor.error = "Tulis nama anda"
        }
        if (Alamat.isEmpty()) {
            itAlamat.error = "Tolong masukkan alamat dengan benar"
        }
        if (noTelepon.isEmpty()) {
            itNoTelepon.error = "Masukkan nomor telepon dengan benar"
        }
        if (kejadian.isEmpty()) {
            itKejadian.error = "Ceritakan kejadian sebenarnya"
        }
        val Id = dbRef.push().key!!

        val pengguna = pengguna(Id, namaPelapor, noTelepon, Alamat, kejadian, laporan)
        dbRef.child(Id).setValue(pengguna)
            .addOnCompleteListener {
                Toast.makeText(this, "Sukses memasukkan data", Toast.LENGTH_LONG).show()
                itNamaPelapor.text?.clear()
                itAlamat.text?.clear()
                itNoTelepon.text?.clear()
                itKejadian.text?.clear()

            }.addOnFailureListener { err ->
                Toast.makeText(this, "gagal ${err.message}", Toast.LENGTH_LONG).show()
            }
    }
}
