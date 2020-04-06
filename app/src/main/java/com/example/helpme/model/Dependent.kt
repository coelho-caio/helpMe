package com.example.helpme.model

import org.json.JSONObject
import java.io.Serializable


class Dependent()  {
//    lateinit var id : String
    var name : String? =null
    var email : String? = null
    var phone : String? = null
    var userId : String? = null

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


}