package com.example.helpme.model

class Dependent() {
    lateinit var id : String
    lateinit var name : String
    lateinit var email : String
    lateinit var phone : String
    lateinit var userId : String
    constructor(name: String, email: String, phone: String, userId: String) : this(){
        this.name = name
        this.email = email
        this.phone = phone
        this.userId = userId
    }


}