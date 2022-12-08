package com.example.hitenpractical.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.hitenpractical.R
import com.example.hitenpractical.databinding.ActivityMainBinding
import com.example.hitenpractical.db.LocationViewModel
import com.example.hitenpractical.model.LocationEntity
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener


class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMainBinding
    private val apiKey = "AIzaSyBSNyp6GQnnKlrMr7hD2HGiyF365tFlK5U"
    var placesClient: PlacesClient? = null
    var address: String? = null
    var name: String? = null
    var mLocationEntity: LocationEntity? = null
    private var locationViewModel: LocationViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        locationViewModel = ViewModelProvider(this)[LocationViewModel::class.java]

        mLocationEntity = intent.getSerializableExtra("item") as LocationEntity?


        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, apiKey)
        }
        placesClient = Places.createClient(this)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val autocompleteFragment =
            supportFragmentManager.findFragmentById(R.id.place_autocomplete_fragment) as AutocompleteSupportFragment

        autocompleteFragment.setPlaceFields(
            listOf(
                Place.Field.NAME, Place.Field.ID, Place.Field.ADDRESS, Place.Field.LAT_LNG
            )
        )
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onError(p0: Status) {

            }

            override fun onPlaceSelected(place: Place) {
                address = place.address?.toString()

                name = place.name?.toString()
                val latLong = LatLng(place.latLng?.latitude!!, place.latLng?.longitude!!)

                mMap.clear()
                mMap.addMarker(
                    MarkerOptions().position(latLong).title(address)
                )
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLong, 12.0f))

                val locationEntity: LocationEntity = if (mLocationEntity == null) {
                    LocationEntity(
                        0, name.toString(), address.toString(), latLong.longitude, latLong.latitude
                    )
                } else {
                    LocationEntity(
                        mLocationEntity?.id ?: 0,
                        name.toString(),
                        address.toString(),
                        latLong.longitude,
                        latLong.latitude
                    )
                }
                binding.constraint.visibility = View.VISIBLE


                binding.btnSave.setOnClickListener {
                    mLocationEntity?.let {
                        locationViewModel?.update(locationEntity)
                    } ?: kotlin.run {
                        locationViewModel?.insert(locationEntity)
                    }
                    finish()
                }
            }

        })


    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mLocationEntity?.let {

            val latLong = LatLng(it.latitude, it.longitude)

            binding.constraint.visibility = View.VISIBLE
            binding.btnSave.text = "Update"

            mMap.addMarker(MarkerOptions().position(latLong).title(it.address))
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLong, 12.0f))


        }
    }


}