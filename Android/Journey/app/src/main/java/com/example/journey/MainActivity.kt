package com.example.journey

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.fondesa.kpermissions.PermissionStatus
import com.fondesa.kpermissions.allGranted
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.fondesa.kpermissions.request.PermissionRequest
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MainActivity : AppCompatActivity(), PermissionRequest.Listener, OnMapReadyCallback  {
    private var profilepage = false
    private val request by lazy {
        permissionsBuilder(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        ).build()
    }

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        request.addListener(this)
        request.send()


        setContentView(R.layout.activity_main)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)



        /*
        button.setOnClickListener {

            if (!profilepage){
                val fragment = ProfileFragment.newInstance("","")
                supportFragmentManager.beginTransaction()
                    .replace(R.id.map, fragment)
                    .commitNow()

            }
            else {
                val fragment = MapsFragment()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.main, fragment)
                    .commitNow()
            }

            profilepage = !profilepage
        }
         */
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
            Toast.makeText(this, "You have to accept to enjoy all app's services!", Toast.LENGTH_LONG).show();
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
            }
        }

        /*
        if (mMap != null) {
            mMap.setOnMyLocationChangeListener { arg0 ->
                mMap.addMarker(
                    MarkerOptions().position(
                        LatLng(
                            arg0.latitude,
                            arg0.longitude
                        )
                    ).title("It's Me!")
                )
            }
            */

        /*
        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
         */
    }
}