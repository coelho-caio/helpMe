package com.example.helpme.activity

import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.*
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.helpme.Constants
import com.example.helpme.FetchAddressIntentService
import com.example.helpme.R
import com.example.helpme.Utils.SendMessageUtils
import com.example.helpme.model.DependentFromFirebase
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_alert.*


class AlertActivity : AppCompatActivity() {


    private lateinit var locationCallback: LocationCallback
    private  lateinit var dependents: ArrayList<DependentFromFirebase>
    private lateinit var resultReceiver :addressResultReceiver
    var isPressedButton = false
    lateinit var vibrator:Vibrator


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alert)
        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        val extra = intent.extras
        if (extra!=null){
            dependents = extra.getParcelableArrayList("dependente")
        }
        resultReceiver = addressResultReceiver( Handler(), dependents)
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                super.onLocationResult(locationResult)
                LocationServices.getFusedLocationProviderClient(this@AlertActivity)
                    .removeLocationUpdates(this)
                if (locationResult!=null && locationResult.locations.size>0){
                    var latestLocationIndex = locationResult.locations.size -1
                    var latitude =locationResult.locations.get(latestLocationIndex).latitude
                    var longitude = locationResult.locations.get(latestLocationIndex).longitude

                    var location =Location("providerNA")
                    location.setLatitude(latitude)
                    location.setLongitude(longitude)
                    fetchAddressFromLatLong(location)
                }
            }
        }
        vibrate()
        startTimer()
        Log.w("andre","criou alert")
    }

    private fun startTimer() {
        object : CountDownTimer(10000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = millisUntilFinished / 1000 % 60
                tv_alert_timer.setText(seconds.toString())
            }

            override fun onFinish() {
                if (!isPressedButton) {
                    getLocation()
                    vibrator.cancel()
                    tv_alert_timer.setText("mensagem enviada")
                    tv_alert_are_u_okay.visibility = View.GONE
                    bt_alert_yes.visibility = View.GONE
                    bt_alert_back.visibility = View.VISIBLE
                }

            }
        }.start()
    }

    private fun finishThisActivity() {
        vibrator.cancel()
        val intentUserSituation = Intent(this, DashboardActivity::class.java)
        intentUserSituation.putExtra("UserPassOut", true)
        startActivity(intentUserSituation)
        finish()
    }

    private fun setlisteners(vibrator: Vibrator) {
        bt_alert_yes.setOnClickListener {
            isPressedButton = true
            vibrator.cancel()
            finishThisActivity()

        }
        bt_alert_back.setOnClickListener{
            finishThisActivity()
        }
    }

    private fun vibrate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(10000,VibrationEffect.DEFAULT_AMPLITUDE))
        }else{
            vibrator.vibrate(10000)
        }
        setlisteners(vibrator)
    }

    private fun getLocation() {
        val locationRequest = LocationRequest()
        locationRequest.setInterval(10000)
        locationRequest.setFastestInterval(3000)
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)

        LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(locationRequest,locationCallback, Looper.getMainLooper())
    }


    fun fetchAddressFromLatLong(location: Location){
        val intent = Intent(this, FetchAddressIntentService::class.java)
        intent.putExtra(Constants.RECEIVER, resultReceiver)
        intent.putExtra(Constants.LOCATION_DATA_EXTRAS, location)
        startService(intent)
    }

    override fun onBackPressed() {
        isPressedButton = true
        val intentUserSituation = Intent(this, DashboardActivity::class.java)
        intentUserSituation.putExtra("UserPassOut", true)
        intentUserSituation.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intentUserSituation)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.w("andre" ,
            " fechou Alert")
    }

    class addressResultReceiver(handler: Handler, dependent: MutableList<DependentFromFirebase>) : ResultReceiver(handler) {
        val dependents = dependent

        override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
            super.onReceiveResult(resultCode, resultData)
            if (resultCode == Constants.RESULT_SUCESS){

                val address = resultData?.getString(Constants.RESULT_DATA_KEY)
                SendMessageUtils().sendMessageDependent(address, dependents)

            }else {
                Log.w("Favela","deu ruim")
            }
        }
    }

}
