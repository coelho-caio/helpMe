package com.example.helpme.ui.activity

import android.content.Context
import android.content.Intent
import android.os.*
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.helpme.R
import kotlinx.android.synthetic.main.activity_alert.*


class AlertActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alert)
        vibrate()
        startTimer()
    }

    private fun startTimer() {
        object : CountDownTimer(10000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = millisUntilFinished / 1000 % 60
                tv_alert_timer.setText(seconds.toString())
            }

            override fun onFinish() {
                Log.w("timer","timer is done")
            }
        }.start()
    }

    private fun setlisteners(vibrator: Vibrator) {
        bt_alert_yes.setOnClickListener {
            disableAlert()
        }
    }

    private fun disableAlert(): View.OnClickListener? {
        //vibrator.cancel()
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
        return null
    }

    private fun vibrate() {
        val vibrator:Vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(10000,VibrationEffect.DEFAULT_AMPLITUDE))
        }else{
            vibrator.vibrate(10000)
        }
        setlisteners(vibrator)
    }

}
