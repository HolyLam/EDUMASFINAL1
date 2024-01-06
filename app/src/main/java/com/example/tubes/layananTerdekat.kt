package com.example.tubes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SearchView
import android.widget.Toast
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.ItemizedIconOverlay
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.OverlayItem
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

class layananTerdekat : AppCompatActivity() {
    private lateinit var mapView: MapView
    private lateinit var locationOverlay: MyLocationNewOverlay
    private lateinit var searchView: SearchView
    private lateinit var markers: List<Marker>
    private var selectedMarkerIndex: Int? = null
    private lateinit var itemizedIconOverlay: ItemizedIconOverlay<OverlayItem>
    private fun findMarkerIndexByTitle(title: String): Int {
        return markers.indexOfFirst { it.title == title }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_layanan_terdekat)
        // Konfigurasi osmdroid
        Configuration.getInstance().load(applicationContext, getPreferences(MODE_PRIVATE))

        mapView = findViewById(R.id.mapView)
        searchView = findViewById(R.id.searchView)

        mapView.setTileSource(TileSourceFactory.MAPNIK) // Pilih sumber peta
        mapView.controller.setZoom(12.0)
        mapView.controller.setCenter(GeoPoint(-7.7956, 110.3695)) // Koordinat Jogja

        // Inisialisasi layer lokasi pengguna
        locationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(this), mapView)
        locationOverlay.enableMyLocation()
        mapView.overlays.add(locationOverlay)


        // Inisialisasi marker setelah mapView diinisialisasi
        markers = listOf(
            Marker(mapView).apply {
                position = GeoPoint(-7.686436167970955, 110.4105761018229)
                title = "Fakultas Teknologi Industri Universitas Islam Indonesia"
            },
            Marker(mapView).apply {
                position = GeoPoint(-7.771904965504951, 110.4667372364816)
                title = "Rumah Sakit Panti Rini"
            },
            Marker(mapView).apply {
                position = GeoPoint(-7.777024689204712, 110.37635138436038)
                title = "Rumah Sakit Panti Rapih"
            },
            Marker(mapView).apply {
                position = GeoPoint(-7.757429524470313, 110.40357239415273)
                title = "Rumah Sakit JIH"
            },
            Marker(mapView).apply {
                position = GeoPoint(-7.766084327088446, 110.47179150764573)
                title = "Rumah Sakit Bhayangkara POLDA DIY"
            },
            Marker(mapView).apply {
                position = GeoPoint(-7.767340255383443, 110.37321617447398)
                title = "Rumah Sakit Umum Pusat (RSUP) Dr. Sardjito"
            },
            Marker(mapView).apply {
                position = GeoPoint(-7.783000800727978, 110.39096349723769)
                title = "Siloam Hospitals Yogyakarta"
            },
            Marker(mapView).apply {
                position = GeoPoint(-7.9092581046954225, 110.2959087532644)
                title = "Rumah Sakit UII"
            },
            Marker(mapView).apply {
                position = GeoPoint(-7.775723,110.458862)
                title = "Rumah Sakit Islam PDHI Yogyakarta"
            },
            Marker(mapView).apply {
                position = GeoPoint(-7.797511,110.411506)
                title = "RSPAU Dr. S. Hardjolukito"
            },
            Marker(mapView).apply {
                position = GeoPoint(-7.770110,110.432549)
                title = "Rumah Sakit Hermina Yogya"
            },
            Marker(mapView).apply {
                position = GeoPoint(-7.858536406079082, 110.14803362484035)
                title = "Rumah Sakit Umum Daerah ( RSUD ) Wates"
            },
//            DAMKAR
            Marker(mapView).apply {
                position = GeoPoint(-7.797511,110.411506)
                title = "Pemadam Kebakaran Kab.Sleman"
            },
            Marker(mapView).apply {
                position = GeoPoint(-7.779508773539168, 110.35511859984936)
                title = "Pos Pemadam Kebakaran BPBD"
            },
            Marker(mapView).apply {
                position = GeoPoint( -7.773403868017972, 110.37896119719632)
                title = "Pos Pemadam Kebakaran UGM"
            },
            Marker(mapView).apply {
                position = GeoPoint(-7.766423993260463, 110.30085944927204)
                title = "Kantor Pemadam Kebakaran Godean"
            },
            Marker(mapView).apply {
                position = GeoPoint(-7.82732373872083, 110.41715041277887)
                title = "Pos 2 Pemadam Kebakaran Bantul"
            },
            Marker(mapView).apply {
                position = GeoPoint(-7.853086111091199, 110.16554677554794)
                title = "Pemadam Kebakaran Kabupaten Kulonprogo"
            },
            Marker(mapView).apply {
                position = GeoPoint(-7.814492364584679, 110.25607534995632)
                title = "Pos Pemadam Kebakaran Kabupaten Bantul Pos Sedayu"
            },


        )

        // Tambahkan semua marker ke peta
        for (marker in markers) {
            mapView.overlays.add(marker)
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrBlank()) {
                    val lowerCaseQuery = query.toLowerCase()
                    val matchingMarkerIndex = findMarkerIndexByTitle(lowerCaseQuery)

                    if (matchingMarkerIndex != -1) {
                        val selectedMarker = markers[matchingMarkerIndex]
                        mapView.controller.animateTo(selectedMarker.position, 18.0, 3000L, null)
                        selectedMarker.showInfoWindow()
                        searchView.clearFocus() // Hilangkan fokus dari SearchView
                        selectedMarkerIndex = matchingMarkerIndex
                    }
                }else {
                    Toast.makeText(this@layananTerdekat, "Marker not found", Toast.LENGTH_SHORT).show()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterMarkers(newText)
                return true
            }
        })
        itemizedIconOverlay = ItemizedIconOverlay(this, mutableListOf(), object : ItemizedIconOverlay.OnItemGestureListener<OverlayItem> {
            override fun onItemLongPress(index: Int, item: OverlayItem?): Boolean {
                return false
            }

            override fun onItemSingleTapUp(index: Int, item: OverlayItem?): Boolean {
                val selectedOverlayItem = itemizedIconOverlay.getItem(index)
                val selectedMarkerTitle = selectedOverlayItem.title
                selectedMarkerIndex = markers.indexOfFirst { it.title == selectedMarkerTitle }

                if (selectedMarkerIndex != -1) {
                    val selectedMarker = markers[selectedMarkerIndex!!]
                    mapView.controller.animateTo(selectedMarker.position, 18.0, 3000L, null)
                    selectedMarker.showInfoWindow()
                    searchView.clearFocus()
                }

                return true
            }
        })
        mapView.overlays.add(itemizedIconOverlay)
        mapView.invalidate()
    }


    private fun filterMarkers(query: String?) {
        selectedMarkerIndex = null
        mapView.overlays.clear()

        val overlayItems = mutableListOf<OverlayItem>()

        for ((index, marker) in markers.withIndex()) {
            val markerTitle = marker.title?.toLowerCase() ?: ""

            if (query.isNullOrBlank() || markerTitle.contains(query.toLowerCase())) {
                marker.icon = resources.getDrawable(android.R.drawable.ic_dialog_map)
                overlayItems.add(OverlayItem(marker.title, "", marker.position))
            } else {
                marker.icon = null
            }
        }

        itemizedIconOverlay.removeAllItems()
        itemizedIconOverlay.addItems(overlayItems)
        mapView.overlays.add(itemizedIconOverlay)
        mapView.invalidate()
    }



    override fun onResume() {
        super.onResume()
        mapView.onResume()
        locationOverlay.enableMyLocation()
//        locationOverlay.enableFollowLocation()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
        locationOverlay.disableMyLocation()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDetach()
    }
}