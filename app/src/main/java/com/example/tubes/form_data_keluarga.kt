package com.example.tubes

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class form_data_keluarga : AppCompatActivity() {
    private lateinit var btn: Button
    private lateinit var database: DatabaseReference
    private var progressDialog: Dialog?=null
    private lateinit var AnomorKeluarga: TextInputLayout
    private lateinit var bNamaKepala: TextInputLayout
    private lateinit var cNIK: TextInputLayout
    private lateinit var Djumlah: TextInputLayout
    private lateinit var eStatus: TextInputLayout
    private lateinit var Falamat: TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_data_keluarga)
        btn=findViewById(R.id.btnKirim)
        AnomorKeluarga=findViewById(R.id.Anomorkeluarga)
        bNamaKepala=findViewById(R.id.bNamakepala)
        cNIK=findViewById(R.id.cNIK)
        Djumlah=findViewById(R.id.Djumlah)
        eStatus=findViewById(R.id.eStatus)
        Falamat=findViewById(R.id.Falamat)

        btn.setOnClickListener(View.OnClickListener {

            val satu=AnomorKeluarga.editText?.text.toString()
            val dua=bNamaKepala.editText?.text.toString()
            val tiga=cNIK.editText?.text.toString()
            val empat=Djumlah.editText?.text.toString()
            val lima=eStatus.editText?.text.toString()
            val enam=Falamat.editText?.text.toString()

            showprogressbar()

            addValue(satu,dua,tiga,empat,lima,enam)

        })

    }private fun showprogressbar(){
        progressDialog=Dialog(this)
        progressDialog?.setContentView(R.layout.progressbar)
        progressDialog?.setCancelable(false)
        progressDialog?.show()
    }

    private fun addValue(satu: String, dua: String, tiga: String, empat: String, lima: String, enam: String) {
        database = FirebaseDatabase.getInstance().getReference("Users")
        val dataKeluarga = User(satu, dua, tiga, empat, lima, enam)
        database.child(satu).setValue(dataKeluarga).addOnSuccessListener {

            AnomorKeluarga.editText?.text?.clear()
            bNamaKepala.editText?.text?.clear()
            cNIK.editText?.text?.clear()
            Djumlah.editText?.text?.clear()
            eStatus.editText?.text?.clear()
            Falamat.editText?.text?.clear()

            progressDialog?.dismiss()
            Toast.makeText(this, "Berhasil menambahkan data keluarga", Toast.LENGTH_LONG).show()
            val intent = Intent(this, profil::class.java)
            intent.putExtra("namaKepala", dua)
            startActivity(intent)
        }.addOnFailureListener{
            Toast.makeText(this,"Error", Toast.LENGTH_LONG).show()
            progressDialog?.dismiss()
        }
    }

}