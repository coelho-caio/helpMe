package com.example.helpme.model

import android.os.Parcel
import android.os.Parcelable
import org.json.JSONObject
import java.io.Serializable


class Dependent()  : Parcelable {
//    lateinit var id : String
    var name : String? =null
    var email : String? = null
    var phone : String? = null
    var userId : String? = null

    constructor(parcel: Parcel) : this() {
        name = parcel.readString()
        email = parcel.readString()
        phone = parcel.readString()
        userId = parcel.readString()
    }

    constructor(name: String, email: String, phone: String, userId: String) : this(){
        this.name = name
        this.email = email
        this.phone = phone
        this.userId = userId
    }
    constructor(jsonObject: JSONObject) : this(){
        this.name = jsonObject.getString("name")
        this.email = jsonObject.getString("email")
        this.phone = jsonObject.getString("phone")
        this.userId = jsonObject.getString("userId")
    }

//    constructor(id:String, name: String, email: String, phone: String, userId: String) : this(){
//        this.id = id
//        this.name = name
//        this.email = email
//        this.phone = phone
//        this.userId = userId
//    }
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(email)
        parcel.writeString(phone)
        parcel.writeString(userId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Dependent> {
        override fun createFromParcel(parcel: Parcel): Dependent {
            return Dependent(parcel)
        }

        override fun newArray(size: Int): Array<Dependent?> {
            return arrayOfNulls(size)
        }
    }


}