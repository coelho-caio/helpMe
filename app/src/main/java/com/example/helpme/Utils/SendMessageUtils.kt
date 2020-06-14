package com.example.helpme.Utils

import android.telephony.SmsManager
import com.example.helpme.model.Dependent

class SendMessageUtils {
    fun sendMessageDependent(message: String?, dependents:MutableList<Dependent>) {
        val smsManager = SmsManager.getDefault()
        val iterator = dependents.listIterator()
        for (item in iterator) {
            smsManager.sendTextMessage(
                item.phone,
                null,
                "o local da queda é : ${message} ",
                null,
                null
            )
        }
        smsManager.sendTextMessage(
            "11963125917",
            null,
            "o local da queda é : ${message} ",
            null,
            null
        )
    }
}