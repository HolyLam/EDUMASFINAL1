package com.example.tubes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.cardview.widget.CardView

class main_pengaduan : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_pengaduan)

        val card1: CardView = findViewById(R.id.lapMedis)
        card1.setOnClickListener {
            // Navigate to the formPengaduan activity with a specific report type
            navigateToFormPengaduan("Laporan Medis")
        }

        val card2: CardView = findViewById(R.id.lapKebakaran)
        card2.setOnClickListener {
            // Navigate to the formPengaduan activity with a specific report type
            navigateToFormPengaduan("Laporan Kebakaran")
        }

        val card3: CardView = findViewById(R.id.lapInfrastruktur)
        card3.setOnClickListener {
            // Navigate to the formPengaduan activity with a specific report type
            navigateToFormPengaduan("Laporan Infrastruktur")
        }
    }
    private fun navigateToFormPengaduan(reportType: String) {
        val intent = Intent(this, formPengaduan::class.java)
        intent.putExtra("REPORT_TYPE", reportType)
        startActivity(intent)
    }
}