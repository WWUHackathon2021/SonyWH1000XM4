package com.example.journey

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.example.journey.UI.MapsFragment
import com.example.journey.UI.ProfileFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_main.*

import com.fondesa.kpermissions.PermissionStatus
import com.fondesa.kpermissions.allGranted
import com.fondesa.kpermissions.anyPermanentlyDenied
import com.fondesa.kpermissions.anyShouldShowRationale
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.fondesa.kpermissions.request.PermissionRequest
import com.google.android.gms.maps.*


class MainActivity : AppCompatActivity(), PermissionRequest.Listener, OnMapReadyCallback  {
    private var profilepage = false
    private val request by lazy {
        permissionsBuilder(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION).build()
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

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }
}