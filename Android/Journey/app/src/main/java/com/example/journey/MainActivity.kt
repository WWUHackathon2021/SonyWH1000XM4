package com.example.journey

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.journey.UI.MapsFragment
import com.example.journey.UI.ProfileFragment
import com.fondesa.kpermissions.PermissionStatus
import com.fondesa.kpermissions.allGranted
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.fondesa.kpermissions.request.PermissionRequest
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.PointOfInterest
import com.google.android.gms.maps.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.profile_fragment.*


class MainActivity : AppCompatActivity(), PermissionRequest.Listener, OnMapReadyCallback, GoogleMap.OnPoiClickListener  {
    private var profilepage = false
    private val request by lazy {
        permissionsBuilder(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        ).build()
    }

    private lateinit var mMap: GoogleMap
    private lateinit var mainFragment: MapsFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        request.addListener(this)
        request.send()

        setContentView(R.layout.activity_main)
        // add fragment on top of container (first initialization)
        val mapFragment = MapsFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer, mapFragment)
            .commit()

        profileButton.setOnClickListener{
            if (!profilepage) {
                val profileFragment = ProfileFragment()
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, profileFragment)
                    .addToBackStack(null)
                    .commit()
            }
            else {
                supportFragmentManager.popBackStack()
            }
            profilepage  = !profilepage
        }
    }

    fun showGrantedToast() {
        Toast.makeText(this, "Permission accepted", Toast.LENGTH_LONG).show()
    }

    override fun onPermissionsResult(result: List<PermissionStatus>) {
        when {
            result.allGranted() -> showGrantedToast()
        }
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        //mMap.setMyLocationEnabled(true);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            Toast.makeText(this, "SETLOCATIONTRUE", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(
                this,
                "You have to accept to enjoy all app's services!",
                Toast.LENGTH_LONG
            ).show();
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
            }
        }

        if (mMap != null) {
            mMap.setOnMyLocationChangeListener { arg0 ->
                val currLocation = LatLng(arg0.latitude, arg0.longitude)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currLocation, 15.toFloat()))

            }
        }

        googleMap.setOnPoiClickListener(this)


    }

    override fun onPoiClick(poi: PointOfInterest) {
        Toast.makeText(this, """Clicked: ${poi.name}
            Place ID:${poi.placeId}
            Latitude:${poi.latLng.latitude} Longitude:${poi.latLng.longitude}""",
            Toast.LENGTH_SHORT
        ).show()
    }
}