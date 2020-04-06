package com.example.helpme.activity

import android.Manifest.permission.*
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
import android.telephony.SmsManager
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.helpme.R
import com.example.helpme.adapter.OnItemClickListener
import com.example.helpme.adapter.RecyclerAdapter
import com.example.helpme.business.DashboardBusiness
import com.example.helpme.model.Dependent
import com.example.helpme.repository.DependentRepository
import com.example.helpme.viewmodel.DashboardViewModel
import com.example.helpme.viewmodel.ViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*


class DashboardActivity : AppCompatActivity(), SensorEventListener, OnItemClickListener {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var adapter: RecyclerAdapter

    lateinit var viewModel : DashboardViewModel
    lateinit var viewModelFactory: ViewModelFactory
    val business: DashboardBusiness =
        DashboardBusiness()

    private val MY_PERMISSIONS: Int = 21

    private var arrayeixoX = arrayListOf<String>()
    private var arrayeixoY = arrayListOf<String>()
    private var arrayeixoZ =arrayListOf<String>()
    private var arrayAcelerate=arrayListOf<String>()

    private var record = false

    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener

    private var flag: Int = 0

    private var latitude: Double? = 0.0
    private var longitude: Double? = 0.0

    private var dependents: MutableList<Dependent> = mutableListOf()
    private val repository: DependentRepository = DependentRepository()

    private lateinit var sensorManager: SensorManager
    lateinit var acelerometer: Sensor
    private val permissions = arrayOf(
        SEND_SMS,
        ACCESS_FINE_LOCATION,
        ACCESS_NETWORK_STATE

    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModelFactory = ViewModelFactory(business)
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(DashboardViewModel::class.java)
        sessionUser()
        setSensor()
        setListener()
    }

    private fun setListener() {
        testeVibra.setOnClickListener {
            record =true
            /*val intent = Intent(this, AlertActivity::class.java)
            startActivity(intent)*/
        }
        bt_dashboard_call_api.setOnClickListener {
          checkPermissions()
        }

    }

    private fun setSensor() {
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        acelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager.registerListener(this, acelerometer, SensorManager.SENSOR_DELAY_NORMAL)

    }

    private fun sessionUser() {
        val user = viewModel.checkUser()

        if (user == null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            getDependents(user.uid)
            configureButtonAdd()
        }
    }

    private fun getDependents(userId: String) {
        val documents = repository.getAll(userId)
        documents.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (document in task.result!!) {
                    val dependent = document.toObject(Dependent::class.java)
                    dependents.add(dependent)
                }
                //configureList(dependents)
            }
        }.addOnFailureListener { e ->
            Log.w("Buscar Dependentes", "Erro ao buscar dependentes: ", e)
        }.addOnSuccessListener {
            configureList(dependents)
        }
    }

    private fun configureButtonAdd() {
        botao_novo_usuario.setOnClickListener {
            val intent = Intent(this, FormActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onItemClicked(dependent: Dependent) {
        viewModel.editDependent(dependent)
    }

    private fun configureList(dependents: MutableList<Dependent>) {
            if (!dependents.isEmpty()) {
                lista_usuario_recyclerView.layoutManager =
                    LinearLayoutManager(this)
                lista_usuario_recyclerView.adapter = RecyclerAdapter(dependents, this)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null && record) {
            val acelerate = viewModel.calculerteAcelerate(
                event.values[0].toDouble(),
                event.values[1].toDouble(),
                event.values[2].toDouble())

            arrayAcelerate.add(acelerate.toString())
            arrayeixoX.add(event.values[0].toDouble().toString())
            arrayeixoY.add(event.values[1].toDouble().toString())
            arrayeixoZ.add(event.values[2].toDouble().toString())
        }
    }
    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(
                this,
                permissions.toString()
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    ACCESS_FINE_LOCATION) || ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    SEND_SMS) ||ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    ACCESS_NETWORK_STATE)
            ) {
            } else {
                ActivityCompat.requestPermissions(this, permissions, MY_PERMISSIONS)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    )
    {
        when (requestCode) {
            MY_PERMISSIONS -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    getLocation()
                    Log.d("latitude", "$latitude")
                } else {
                    Log.w("SMS Error", "Tivemos um problema a enviar a msg")
                }
                return
            }
            else -> {
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
                        //sendMessageDependent()
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
                checkSelfPermission(permissions.toString()) != PackageManager.PERMISSION_GRANTED
            } else {
                TODO("VERSION.SDK_INT < M")
            }
        )
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                0,
                0f,
                locationListener
            )
    }

    private fun sendMessageDependent() {
        val smsManager = SmsManager.getDefault()
        val iterator = dependents.listIterator()
        for (item in iterator) {
            smsManager.sendTextMessage(
                item.phone,
                null,
                "testando o app $latitude $longitude ",
                null,
                null
            )
        }
        /*smsManager.sendTextMessage(
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
        )*/
    }
}

