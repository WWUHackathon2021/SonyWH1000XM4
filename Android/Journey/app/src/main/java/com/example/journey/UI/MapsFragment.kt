package com.example.journey.UI

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.journey.DTO.Landmark
import com.example.journey.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private val location : LatLng = LatLng(47.620422, -122.349358)
class MapsFragment : Fragment() {
    private lateinit var mapView : MapView
    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            val success = googleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    activity, R.raw.style_json
                )
            )
            if (!success) {
                Log.e("Failed", "Style parsing failed.")
            }
        } catch (e: Resources.NotFoundException) {
            Log.e("Error", "Can't find style. Error: ", e)
        }
        var i = 0
        var objectMarker = HashMap<Marker, String>()

        var landmarks = GenerateLandmarks()
        landmarks.forEach {
            var coord = LatLng(it.latitude, it.longitude)
            val m = googleMap.addMarker(
                MarkerOptions()
                    .position(coord)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.landmark_img))
            )
            objectMarker.put(m, "marker$i")
        }

        googleMap.setOnMarkerClickListener(OnMarkerClickListener { marker ->
            // collect
            true
        })

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 18.toFloat()))
        googleMap.addMarker(
            MarkerOptions()
                .position(location)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.toll_24px_img))
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView = view.findViewById<View>(R.id.mapView) as MapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(callback)
    }

    private fun GenerateLandmarks(): List<Landmark> {
        var landmark = Landmark("12345", "Space Needle", location.longitude, location.latitude)
        return listOf(landmark)
    }

    override fun onResume() {
        mapView.onResume()
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MapsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}