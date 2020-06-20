package com.example.helpme.Utils

import android.telephony.SmsManager
import android.util.Log
import com.example.helpme.model.Dependent

class SendMessageUtils {
    fun sendMessageDependent(message: String?, dependents:MutableList<Dependent>) {

        val smsManager = SmsManager.getDefault()
        val parts = smsManager.divideMessage(message)
        val iterator = dependents.listIterator()
        for (item in iterator) {
            try {
                smsManager.sendMultipartTextMessage(
                    item.phone,
                    null,
                    parts,
                    null,
                    null
                )
            } catch (e: Exception) {
                Log.e("Message error", e.toString())
            }

        }
    }
}