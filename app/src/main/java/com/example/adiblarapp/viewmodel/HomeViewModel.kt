package com.example.adiblarapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel:ViewModel() {

    // for notify items
    var boolean = MutableLiveData<Boolean>(false)

    fun refreshData(){
        boolean.value = !boolean.value!!
    }

}