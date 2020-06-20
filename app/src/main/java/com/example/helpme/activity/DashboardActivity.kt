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
import com.example.helpme.viewmodel.DashboardViewModel
import com.example.helpme.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*


class DashboardActivity : AppCompatActivity(), SensorEventListener, OnItemClickListener {

    lateinit var viewModel : DashboardViewModel
    lateinit var viewModelFactory: ViewModelFactory
    val business: DashboardBusiness = DashboardBusiness()

    private val MY_PERMISSIONS: Int = 21
    private var arrayAcelerate=arrayListOf<String>()

    private var dependents: ArrayList<Dependent> = ArrayList()

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
        setlistener()
        sessionUser()
        checkPermissions()
        receiveIntents()
    }

    private fun setlistener() {
        bt_dashboard_call_api.setOnClickListener {
/*
            sensorManager.unregisterListener(this)
*/

           Log.w("aceleracao", arrayAcelerate.toString())
        }
        testeVibra.setOnClickListener {

            arrayAcelerate.clear()
        }
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
        val documents =viewModel.getDependents(userId)
        documents.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (document in task.result!!) {
                    val dependent = document.toObject(Dependent::class.java)
                    dependents.add(dependent)
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
        if (event != null) {
            val acelerate = viewModel.calculerteAcelerate(
                event.values[0].toDouble(),
                event.values[1].toDouble(),
                event.values[2].toDouble())

            if (acelerate<2.0){

                Log.w("acelerecacao", acelerate.toString() )

                sensorManager.unregisterListener(this)

                /*val intent = Intent(this, AlertActivity::class.java)
                intent.putExtra("dependente",dependents)
                startActivity(intent)*/
            }

            arrayAcelerate.add(acelerate.toString())
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
        sensorManager.unregisterListener(this)

    }
}

