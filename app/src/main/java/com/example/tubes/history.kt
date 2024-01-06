package com.example.tubes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class history : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: penggunaAdapter
    private lateinit var dataPengaduanList: MutableList<pengguna>
    private lateinit var dbRef: DatabaseReference
    private lateinit var btnBackhistory: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        btnBackhistory = findViewById(R.id.backHistory)
        btnBackhistory.setOnClickListener(){
            val pindah = Intent(this, main_menu::class.java)
            startActivity(pindah)
        }

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        dataPengaduanList = mutableListOf()

        dbRef = FirebaseDatabase.getInstance().getReference("Data pengaduan")

        // Menggunakan ValueEventListener untuk mengambil data dari Firebase
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                dataPengaduanList.clear()
                for (postSnapshot in snapshot.children) {
                    val pengaduan = postSnapshot.getValue(pengguna::class.java)
                    if (pengaduan != null) {
                        dataPengaduanList.add(pengaduan)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })

        adapter = penggunaAdapter(dataPengaduanList)
        adapter.setOnItemClickListener(object : penggunaAdapter.OnItemClickListener {
            override fun onDeleteClick(position: Int) {
                showDeleteConfirmationDialog(position)
            }
        })
        recyclerView.adapter = adapter
    }
    private fun showDeleteConfirmationDialog(position: Int) {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Konfirmasi Penghapusan")
        alertDialogBuilder.setMessage("Anda yakin ingin menghapus data ini?")
        alertDialogBuilder.setPositiveButton("Ya") { _, _ ->
            // Hapus data dari Firebase
            val deletedItem = dataPengaduanList[position]
            val dbRef = FirebaseDatabase.getInstance().getReference("Data pengaduan")

            // Mengonversi Id ke String jika tipe data Id bukan String
            val itemId = deletedItem.Id.toString()

            dbRef.child(itemId).removeValue()
                .addOnCompleteListener {
                    Toast.makeText(this, "Data berhasil dihapus", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { error ->
                    Toast.makeText(
                        this,
                        "Gagal menghapus data: ${error.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
        alertDialogBuilder.setNegativeButton("Tidak") { dialog, _ ->
            dialog.dismiss()
        }

        alertDialogBuilder.create().show()
    }
}