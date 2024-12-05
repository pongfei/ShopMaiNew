package com.egci428.shopmai

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.egci428.shopmai.databinding.ActivityMapsBinding
import com.google.android.gms.maps.model.BitmapDescriptorFactory

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var dataReference: FirebaseFirestore

    private var countMarker: Int = 0
    private var currentMarker: Marker? = null
    private var currentPolyline: Polyline? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dataReference = FirebaseFirestore.getInstance()

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val locationBtn = findViewById<Button>(R.id.locationBtn)
        locationBtn.setOnClickListener {
            if (currentMarker != null) {
                val latLon = currentMarker!!.position
                sendLocation(latLon)
            } else {
                Toast.makeText(this, "No location selected", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //send delivery location to database (firestore)
    private fun sendLocation(latLon: LatLng) {
        val db = dataReference.collection("location")
        db.add(mapOf(
                "latitude" to latLon.latitude.toString(),
                "longitude" to latLon.longitude.toString()
            )

        ).addOnSuccessListener {
            Toast.makeText(this, "Location saved successfully", Toast.LENGTH_SHORT).show()
            finish()
        }.addOnFailureListener {
            Toast.makeText(this, "Failed to save location", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {

        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.delivery)
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, false)

        val bitmapShop = BitmapFactory.decodeResource(resources, R.drawable.shop)
        val scaledBitmapShop = Bitmap.createScaledBitmap(bitmapShop, 100, 100, false)

        mMap = googleMap

        val shopMai = LatLng(13.7982, 100.3172)
        mMap.addMarker(MarkerOptions()
            .position(shopMai).title("Shop Mai Bakery")
            .icon(BitmapDescriptorFactory.fromBitmap(scaledBitmapShop)))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(shopMai, 15f))


        //to remove any previous marker and polyline
        mMap.setOnMapClickListener { latlon ->
            currentMarker?.remove()
            currentPolyline?.remove()

            //move the camera to the selected location
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlon, 15f))
            currentMarker = mMap.addMarker(
                MarkerOptions()
                    .position(latlon)
                    .title("Delivery Location: lat: ${latlon.latitude}, lon: ${latlon.longitude}")
                    .icon(BitmapDescriptorFactory.fromBitmap(scaledBitmap))
            )
            //to draw a line from shopmai to the selected location
            currentPolyline = mMap.addPolyline(
                PolylineOptions()
                    .add(shopMai, latlon)
                    .width(5f)
                    .color(ContextCompat.getColor(this, R.color.black))
            )
        }
    }
}