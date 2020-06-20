package com.example.helpme.model

import android.os.Parcel
import android.os.Parcelable

class DependentFromFirebase() : Parcelable {
    var key : String? =null
    var dependent:Dependent? = null

    constructor(parcel: Parcel) : this() {
        key = parcel.readString()
        dependent = parcel.readParcelable(Dependent::class.java.classLoader)
    }

    constructor(key:String?,dependent: Dependent ?):this(){
        this.key=key
        this.dependent = dependent
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(key)
        parcel.writeParcelable(dependent, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DependentFromFirebase> {
        override fun createFromParcel(parcel: Parcel): DependentFromFirebase {
            return DependentFromFirebase(parcel)
        }

        override fun newArray(size: Int): Array<DependentFromFirebase?> {
            return arrayOfNulls(size)
        }
    }
}