package com.example.helpme.model

import com.google.gson.annotations.SerializedName

data class Fall (
    @SerializedName("aceleracao")
    var acceleration : Double
)
