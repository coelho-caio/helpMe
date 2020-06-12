package com.example.helpme

import android.app.IntentService
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.ResultReceiver
import android.text.TextUtils
import java.util.*
import kotlin.collections.ArrayList

class FetchAddressIntentService : IntentService("name"){

    lateinit var resultReceiver: ResultReceiver

    override fun onHandleIntent(intent: Intent?) {
        if (intent!=null){
            var errorMessage = ""
            resultReceiver = intent.getParcelableExtra(Constants.RECEIVER)
            val location:Location? = intent.getParcelableExtra(Constants.LOCATION_DATA_EXTRAS)
            if (location==null){
                return
            }
            var geoCoder = Geocoder(this, Locale.getDefault())
            var addresses : List<Address>? = null
            try {
                addresses = geoCoder.getFromLocation(location.latitude, location.longitude, 1)
            }catch (e:Exception){
                errorMessage = e.message.toString()
            }
            if (addresses == null || addresses.isEmpty()){
                deliverResultToReceiver(Constants.FAILURE_RESULT, errorMessage)
            }else{
                var address = addresses.get(0)
                var addressFragments : ArrayList<String> = java.util.ArrayList()
                for (i in 0..address.maxAddressLineIndex){
                    addressFragments.add(address.getAddressLine(i))
                }
                deliverResultToReceiver(
                    Constants.RESULT_SUCESS,
                    TextUtils.join(
                        Objects.requireNonNull(System.getProperty("line.separator")),
                        addressFragments
                    )
                )
            }
        }
    }

    fun deliverResultToReceiver( resultCode: Int, addressMessage: String){
        var bundle = Bundle()
        bundle.putString(Constants.RESULT_DATA_KEY, addressMessage)
        resultReceiver.send(resultCode,bundle)

    }
}
