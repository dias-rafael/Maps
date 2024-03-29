package br.com.rafaeldias.maps

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.widget.Toast
import br.com.rafaeldias.maps.utils.PermissaoUtils

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    val permissoesLocalizacao = listOf(Manifest.permission.ACCESS_FINE_LOCATION)

    private lateinit var locationManager: LocationManager

    private lateinit var locationListener: LocationListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        PermissaoUtils.validaPermissao(permissoesLocalizacao.toTypedArray(),this,1)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    private fun initLocationListener() {
        locationListener = object : LocationListener{
            override fun onLocationChanged(location: Location?) {
                val minhaPosicao = LatLng(location?.latitude!!,location?.longitude)
                addMarcador(minhaPosicao,"Mão to no Maps")
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(minhaPosicao,16f))
            }

            override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {

            }

            override fun onProviderEnabled(p0: String?) {

            }

            override fun onProviderDisabled(p0: String?) {

            }
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        for(resposta in grantResults) {
            if (resposta == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(applicationContext, "Sem permissão, sem acesso", Toast.LENGTH_LONG).show()
            } else {
                requestLocationUpdates()
            }
        }
    }

    private fun requestLocationUpdates() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            var locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0.1f, locationListener)
        }
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    private fun addMarcador(latLng: LatLng, titulo: String) {
        mMap.addMarker(MarkerOptions().position(latLng).title(titulo))
    }

    private fun addMarcadorFIAP(latitude: Double, longitude: Double, titulo: String) {
        val geocoder = Geocoder(applicationContext, Locale.getDefault())
        val endereco = geocoder.getFromLocation(latitude,longitude,1)
        val latLng = LatLng(latitude,longitude)
        //addMarcador(it, endereco[0].subLocality) //rodar em modo debug para ver todas as opções
        mMap.addMarker(MarkerOptions().position(latLng).title(titulo).snippet(endereco[0].getAddressLine(0)))
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        initLocationListener()
        requestLocationUpdates()

        // Add a marker in Sydney and move the camera
        val fiappaulistaLat = -23.563805
        val fiappaulistaLon = -46.652485
        val fiappaulistaTit = "Fiap Paulista"

        val fiapaclimacaoLat = -23.573140
        val fiapaclimacaoLon = -46.623823
        val fiapaclimacaoTit = "Fiap Aclimação"

        val fiapvilaolimpiaLat = -23.595079
        val fiapvilaolimpiaLon = -46.685290
        val fiapvilaolimpiaTit = "Fiap Vila Olímpia"

        val fiappaulista = LatLng(fiappaulistaLat,fiappaulistaLon)

        //addMarcadorFIAP(fiappaulistaLat,fiappaulistaLon,fiappaulistaTit)
        //addMarcadorFIAP(fiapaclimacaoLat,fiapaclimacaoLon,fiapaclimacaoTit)
        //addMarcadorFIAP(fiapvilaolimpiaLat,fiapvilaolimpiaLon,fiapvilaolimpiaTit)

        mMap.setOnMapClickListener {
            val geocoder = Geocoder(applicationContext, Locale.getDefault())
            val endereco = geocoder.getFromLocation(it.latitude,it.longitude,1)
            //addMarcador(it, endereco[0].subLocality) //rodar em modo debug para ver todas as opções
            addMarcador(it, endereco[0].getAddressLine(0))
        }

        //mMap.addMarker(MarkerOptions().position(fiappaulista).title("FIAP Paulista").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)))
        //mMap.addMarker(MarkerOptions().position(fiapaclimacao).title("FIAP Aclimação").icon(BitmapDescriptorFactory.fromResource(R.drawable.marcador)))
        //mMap.addMarker(MarkerOptions().position(fiapvilaolimpia).title("FIAP Vila Olímpia").snippet("Clique aqui para remover o ponto"))

        val circulo = CircleOptions()
        circulo.center(fiappaulista)
        circulo.radius(400.0)
        circulo.fillColor(Color.argb(128,0,51,102))
        circulo.strokeWidth(10f)
        circulo.strokeColor(Color.argb(128,0,51,102))

        mMap.addCircle(circulo)


        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(fiappaulista,10f))
    }
}
