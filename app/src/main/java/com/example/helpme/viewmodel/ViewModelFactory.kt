package com.example.helpme.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.helpme.business.BaseBusiness
import com.example.helpme.business.DashboardBusiness

class ViewModelFactory(private val business: BaseBusiness): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DashboardViewModel::class.java)){
            return DashboardViewModel(business as DashboardBusiness) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}