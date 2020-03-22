package com.example.helpme.model

import android.os.Parcel
import android.os.Parcelable

class FallData() :Parcelable {
    var x:Int=0
    var y:Int=0
    var z:Int=0
    constructor(parcel: Parcel) : this() {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FallData> {
        override fun createFromParcel(parcel: Parcel): FallData {
            return FallData(parcel)
        }

        override fun newArray(size: Int): Array<FallData?> {
            return arrayOfNulls(size)
        }
    }

}