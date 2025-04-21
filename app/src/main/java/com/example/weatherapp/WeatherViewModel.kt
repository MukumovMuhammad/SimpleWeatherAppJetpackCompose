package com.example.weatherapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.api.Constant
import com.example.weatherapp.api.NetworkResponse
import com.example.weatherapp.api.RetrofitInstance
import com.example.weatherapp.api.WeatherModel
import kotlinx.coroutines.launch

class WeatherViewModel: ViewModel() {

    private val weatherApi = RetrofitInstance.weatherApi


    private val _weatherResult = MutableLiveData<NetworkResponse<WeatherModel>>()
    val weatherResult : LiveData<NetworkResponse<WeatherModel>> = _weatherResult

    companion object {
        private const val TAG = "WeatherViewModel_TAG"
    }


    fun getData(city: String){
        Log.d(TAG, "getData called with city: $city")
        _weatherResult.value = NetworkResponse.Loading
        viewModelScope.launch{
           val response = weatherApi.getWeather(Constant.apiKey, city)

            try {

                if (response.isSuccessful){
                    Log.i("Response: ", response.body().toString())

                    response.body()?.let {
                        _weatherResult.value = NetworkResponse.Success(it)
                    }
                } else {
                    Log.e("Error: ", response.message().toString())
                    _weatherResult.value = NetworkResponse.Error("Failed to fetch data")
                }
            }
            catch (e: Exception){
                Log.e("Exception: ", e.message.toString())
                _weatherResult.value = NetworkResponse.Error("Exception: ${e.message}")
            }

        }


    }
}