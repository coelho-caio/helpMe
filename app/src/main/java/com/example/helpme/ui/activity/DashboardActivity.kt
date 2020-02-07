package com.example.helpme.ui.activity

import android.Manifest.permission.*
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import android.telephony.SmsManager
import android.util.Log
import com.example.helpme.Database.DatabaseHelpMe.DBHelpMe.COLUMN_EMAIL
import com.example.helpme.Database.DatabaseHelpMe.DBHelpMe.COLUMN_NAME
import com.example.helpme.Database.DatabaseHelpMe.DBHelpMe.COLUMN_PHONE
import com.example.helpme.Database.DatabaseHelpMe.DBHelpMe.COLUMN_USER
import com.example.helpme.Database.DependentDatabase
import com.example.helpme.R
import com.example.helpme.adapter.RecyclerAdapter
import com.example.helpme.model.Dependent
import kotlinx.android.synthetic.main.activity_main.*


class DashboardActivity : AppCompatActivity(), SensorEventListener {

    private val MY_PERMISSIONS:Int=21
    private val MY_PERMISSION_REQUEST_COARSE_LOCATION:Int=22
    private val MY_PERMISSION_REQUEST_ACCESS_FINE_LOCATION :Int=23

    private lateinit var locationManager:LocationManager
    private lateinit var locationListener: LocationListener

    private var flag:Int = 0

    private var latitude:Double? = 0.0
    private var longitude:Double?=0.0

    private var dependents: MutableList<Dependent> = mutableListOf()

    val repository: DashboardRepository = DashboardRepository()

    private lateinit var sensorManager: SensorManager
    lateinit var acelerometer: Sensor
    private val permissions = arrayOf(
        SEND_SMS,
        ACCESS_FINE_LOCATION)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        configuraBotaoAdicionar()

        dependents= repository.configuraDataBase(this, dependents)
        configuraLista(dependents)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        acelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        sensorManager.registerListener(this, acelerometer, SensorManager.SENSOR_DELAY_NORMAL)
    }

    private fun configuraBotaoAdicionar() {
        botao_novo_usuario.setOnClickListener {
            val intent = Intent(this, FormActivity::class.java)
            startActivity(intent)
        }
    }

    private fun configuraLista(dependents: MutableList<Dependent>) {
        lista_usuario_recyclerView.layoutManager =
            LinearLayoutManager(this)
        lista_usuario_recyclerView.adapter = RecyclerAdapter(dependents)

    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event!=null)
            if (event.values[1]>2.toFloat())
                Log.d("sensor", "${event.values[1]}")
                sendMessage()
    }

    fun sendMessage(){
        if (ContextCompat.checkSelfPermission(this, permissions.toString())!= PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    permissions.toString()
                )) {

            }else{
                ActivityCompat.requestPermissions(this, permissions,MY_PERMISSIONS)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            MY_PERMISSIONS -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    getLocation()


                  Log.d("latitude", "$latitude")

                } else {
                    Log.w("SMS Error", "Errrrrrou")
                }
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }

    private fun getLocation() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location?) {
                    latitude = location?.latitude
                    longitude = location?.longitude
                    if (!(latitude == 0.0 || longitude == 0.0)) {
                        if (flag < 1) {
                        enviaMensagem()
                        flag++
                    }
                }

            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

            }

            override fun onProviderEnabled(provider: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onProviderDisabled(provider: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }
        if (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                checkSelfPermission(permissions.toString())!= PackageManager.PERMISSION_GRANTED
            } else {
                TODO("VERSION.SDK_INT < M")
            }
        )
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0f,locationListener)
    }

    private fun enviaMensagem() {
        val smsManager = SmsManager.getDefault()
        smsManager.sendTextMessage(
            "11963125917",
            null,
            "testando o app $latitude $longitude ",
            null,
            null
        )
        smsManager.sendTextMessage(
            "11977973346",
            null,
            "Vamos passar nessa bagaça $latitude $longitude",
            null,
            null
        )
    }
}

