package com.egco428.ex20_currentlocation

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_main.*
import java.util.jar.Manifest

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private  var locationManager: LocationManager? = null
    private var listener: LocationListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        listener = object: LocationListener{
            override fun onLocationChanged(location: Location) {
                latitude.setText(String.format("%9f",location!!.latitude))
                longitude.setText(String.format("%9f",location!!.longitude))
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

            }

            override fun onProviderEnabled(provider: String?) {

            }

            override fun onProviderDisabled(provider: String?) {

            }
        }
        requestLocationService()
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        addmarker.setOnClickListener {
            if (latitude.text.isEmpty()||longitude.text.isEmpty()) {
                Toast.makeText(this,"Enter lat,lng values",Toast.LENGTH_SHORT).show()
            } else {
                val latLng = LatLng(latitude.text.toString().toDouble(),longitude.text.toString().toDouble())
                val markerOptions = MarkerOptions().position(latLng)
                mMap.addMarker(markerOptions)
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permission: Array<String>, grantResults: IntArray){
    when(requestCode){
        10-> requestLocationService()
        else ->{}
        }
    }
    fun requestLocationService(){
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                requestPermissions(arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.INTERNET), 10)
            }
            return
        }

        getlocation.setOnClickListener {
            locationManager!!.requestLocationUpdates("gps", 5000,0f, listener)
        }
    }

    override fun onMapReady(googleMap: GoogleMap){
        mMap = googleMap
        val mu = LatLng(13.7974603, 100.3147093)
        mMap.addMarker(MarkerOptions().position(mu).title("Marker in Mahidol"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mu))
    }
}
