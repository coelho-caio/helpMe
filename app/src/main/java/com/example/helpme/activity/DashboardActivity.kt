package com.example.helpme.activity

import android.Manifest.permission.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
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
import com.example.helpme.model.DependentFromFirebase
import com.example.helpme.viewmodel.DashboardViewModel
import com.example.helpme.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*


class DashboardActivity : AppCompatActivity(), SensorEventListener, OnItemClickListener {

    lateinit var viewModel : DashboardViewModel
    lateinit var viewModelFactory: ViewModelFactory
    val business: DashboardBusiness = DashboardBusiness()

    private val MY_PERMISSIONS: Int = 21
    private var arrayAcelerate=arrayListOf<Double>()

    private var dependents: ArrayList<DependentFromFirebase> = ArrayList()

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
        checkPermissions()
        receiveIntents()
        configureButtonAdd()
    }

    private fun receiveIntents() {
        val bundle = intent.extras
        if (bundle!=null){
            val boolean = bundle.getBoolean("UserPassOut")
            if (boolean){
                //
            }
        }
    }
     fun setSensor() {
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        acelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
         Log.w("sensor"," sensor ligado")
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

        }
    }

    private fun getDependents(userId: String) {
        dependents.clear()
        val documents = viewModel.getDependents(userId)
        documents.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (document in task.result!!) {
                    val dependentFromFirebase = DependentFromFirebase(
                        document.id,
                        document.toObject(Dependent::class.java)
                    )
                    dependents.add(dependentFromFirebase)
                    setSensor()


                }
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

    override fun onItemClicked(id: String?) {
        viewModel.deleteDependent(id)?.addOnSuccessListener { sessionUser() }

    }

    private fun configureList(dependents: MutableList<DependentFromFirebase>) {
                lista_usuario_recyclerView.layoutManager =
                    LinearLayoutManager(this)
                lista_usuario_recyclerView.adapter = RecyclerAdapter(dependents, this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null ) {
            val acelerate = viewModel.calculerteAcelerate(
                event.values[0].toDouble(),
                event.values[1].toDouble(),
                event.values[2].toDouble()
            )

            arrayAcelerate.add(acelerate)

            if (arrayAcelerate.size>299){

                for (i in 0 until arrayAcelerate.size) {
                    if (arrayAcelerate.get(0)<0.5){
                        Log.w("primeira validacao ", arrayAcelerate.toString() )
                        for (j in 0 until arrayAcelerate.size)
                        if (arrayAcelerate.get(j)>2.0){
                            Log.w("deu certo ", "VALIDACOES OK")
                            sensorManager.unregisterListener(this)

                            val intent = Intent(this, AlertActivity::class.java)
                            intent.putExtra("dependente", dependents)
                            startActivity(intent)
                        }
                    }

                }

                arrayAcelerate.removeAt(0)
            }

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
                } else {
                    Log.w("SMS Error", "Tivemos um problema a enviar a msg")
                }
                return
            }
            else -> {
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.w("sensor ","sensor desligado")
        sensorManager.unregisterListener(this)

    }
}

