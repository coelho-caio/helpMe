package com.example.helpme.ui.activity

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.example.helpme.Database.DatabaseHelpMe.DBHelpMe.COLUMN_EMAIL
import com.example.helpme.Database.DatabaseHelpMe.DBHelpMe.COLUMN_NAME
import com.example.helpme.Database.DatabaseHelpMe.DBHelpMe.COLUMN_PHONE
import com.example.helpme.Database.DatabaseHelpMe.DBHelpMe.COLUMN_USER
import com.example.helpme.Database.DependentDatabase
import com.example.helpme.R
import com.example.helpme.adapter.ListDependentAdapter
import com.example.helpme.model.Dependent
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), SensorEventListener {

    private val dependents: MutableList<Dependent> = mutableListOf()

    private lateinit var sensorManager: SensorManager
    lateinit var acelerometer: Sensor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        configuraBotaoAdicionar()

        val dbDependent = DependentDatabase(this)
        val cursor = dbDependent.getAllDependent()
        cursor!!.moveToFirst()
        while (cursor.moveToNext()) {

            val name = (cursor.getString(cursor.getColumnIndex(COLUMN_NAME)))
            val email = (cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL)))
            val phone = (cursor.getString(cursor.getColumnIndex(COLUMN_PHONE)))
            val userId = (cursor.getInt(cursor.getColumnIndex(COLUMN_USER)))
            dependents.add(Dependent(name, email, phone, userId))
        }
        cursor.close()
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
//        lista_usuario_recyclerView.layoutManager = LinearLayoutManager(this)
//        lista_usuario_recyclerView.adapter = RecyclerAdapter(usuarios)
        lista_usuarios_listview.adapter = ListDependentAdapter(dependents, this)

    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event!=null)
        Log.d("SensorChange", "${event.values[1]}")
    }

}
