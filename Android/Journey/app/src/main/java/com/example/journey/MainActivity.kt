package com.example.journey

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import kotlinx.android.synthetic.main.profile_fragment.*


class MainActivity : AppCompatActivity(), PermissionRequest.Listener {
    private var profilepage = false
    private val request by lazy {
        permissionsBuilder(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION).build()
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
}