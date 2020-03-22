package com.example.helpme.ui.activity

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
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.helpme.Api
import com.example.helpme.R
import com.example.helpme.Utils.NetworkUtils
import com.example.helpme.adapter.OnItemClickListener
import com.example.helpme.adapter.RecyclerAdapter
import com.example.helpme.model.Dependent
import com.example.helpme.model.Fall
import com.example.helpme.model.FallData
import com.example.helpme.ui.repository.DependentRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Response


class DashboardActivity : AppCompatActivity(), SensorEventListener, OnItemClickListener {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var adapter: RecyclerAdapter

    private val MY_PERMISSIONS: Int = 21
    private val MY_PERMISSION_REQUEST_COARSE_LOCATION: Int = 22
    private val MY_PERMISSION_REQUEST_ACCESS_FINE_LOCATION: Int = 23

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
        sessionUser()
        setSensor()
        setListener()
    }

    private fun setListener() {
        testeVibra.setOnClickListener {
            val intent = Intent(this, AlertActivity::class.java)
            startActivity(intent)
        }
        bt_dashboard_call_api.setOnClickListener {
            getData()
        }
    }

    private fun getData() {
        val fallData = FallData()

        val retrofitClient = NetworkUtils
            .getRetrofitInstance("https://imaxinformatica.com.br/projetos/helpme/public/api/")

        val api = retrofitClient.create(Api::class.java)
        val callback = api.sendData(fallData)

        callback.enqueue(object :  retrofit2.Callback<Fall> {
            override fun onFailure(call: Call<Fall>, t: Throwable) {
                Toast.makeText(this@DashboardActivity,"Falha na conexão", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<Fall>, response: Response<Fall>) {
                Toast.makeText(this@DashboardActivity,"Conexão com sucesso", Toast.LENGTH_LONG).show()
                Log.w("TCClindo",response.body().toString())
            }

        })
    }

    private fun setSensor() {
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        acelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager.registerListener(this, acelerometer, SensorManager.SENSOR_DELAY_NORMAL)

    }

    private fun sessionUser() {
        mAuth = FirebaseAuth.getInstance()
        var user = mAuth.currentUser

        if (user == null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            configureDependents(user.uid)
            configureButtonAdd()
        }
    }

    private fun configureDependents(userId: String) {
        val documents = repository.getAll(userId)
        documents.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (document in task.result!!) {
                    val dependent = document.toObject(Dependent::class.java)
                    dependents.add(dependent)
                }
                configureList(dependents)
            }
        }.addOnFailureListener { e ->
            Log.w("Buscar Dependentes", "Erro ao buscar dependentes: ", e)
        }
    }

    private fun configureButtonAdd() {
        botao_novo_usuario.setOnClickListener {
            val intent = Intent(this, FormActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onItemClicked(dependent: Dependent) {
        val dependentDoc = repository.getDependent("blkjv6rFTBblHBBh6WQ3JF19onj1")
        dependentDoc.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d("exist", "DocumentSnapshot data: ${document.data}")
                } else {
                    Log.d("noexist", "No such document")
                }
            }
//        val intent = Intent(this, FormActivity::class.java)
//        intent.putExtra("nameDependent", dependent.name)
//        intent.putExtra("phoneDependent", dependent.phone)
//        intent.putExtra("emailDependent", dependent.email)
//        startActivity(intent)
    }

    private fun configureList(dependents: MutableList<Dependent>) {
        lista_usuario_recyclerView.layoutManager =
            LinearLayoutManager(this)
        lista_usuario_recyclerView.adapter = RecyclerAdapter(dependents, this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null) {
            var acelerate = calculateAcelerate(
                event.values[0].toDouble(),
                event.values[1].toDouble(),
                event.values[2].toDouble()
            )
            Log.d("aceleracao", acelerate.toString())
            Log.d("eixo x", event.values[0].toDouble().toString())
            Log.d("eixo y", event.values[1].toDouble().toString())
            Log.d("eixo z", event.values[2].toDouble().toString())
            sendMessage()
        }
    }

    private fun calculateAcelerate(x: Double, y: Double, z: Double) : Double {
        val eixox=Math.pow(x,2.0)
        val eixoy=Math.pow(y,2.0)
        val eixoz=Math.pow(z,2.0)

        return Math.sqrt(eixox+eixoy+eixoz)
    }

    private fun sendMessage() {
        if (ContextCompat.checkSelfPermission(
                this,
                permissions.toString()
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    permissions.toString()
                )
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
    ) {
        when (requestCode) {
            MY_PERMISSIONS -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    getLocation()


                    Log.d("latitude", "$latitude")

                } else {
                    Log.w("SMS Error", "Tivemos um problema a enviar a msg")
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
                        sendMessageDependent()
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

