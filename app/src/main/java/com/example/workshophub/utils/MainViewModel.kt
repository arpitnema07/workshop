package com.example.workshophub.utils

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workshophub.db.AppRepository
import com.example.workshophub.models.ErrorResponse
import com.example.workshophub.models.User
import com.example.workshophub.models.Workshop
import com.example.workshophub.network.ApiInterface
import com.example.workshophub.network.RetrofitClient
import com.example.workshophub.utils.Constant.WORKSHOP
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.Reader

class MainViewModel(private val repository: AppRepository) : ViewModel() {
    val allWorkshop: LiveData<List<Workshop>> = repository.getAllWorkshop()
    val allAppliedWorkshop: LiveData<List<Workshop>> = repository.getAllAppliedWorkshop()


    val _isLoggedIn = MutableLiveData(false)
    val isLoggedIn : LiveData<Boolean> = _isLoggedIn

    val _current = MutableLiveData(WORKSHOP)
    val current: LiveData<String> = _current

    private val _authResult = MutableLiveData<Result<User>>()
    val authResult: LiveData<Result<User>> = _authResult

    private val _loading = MutableLiveData<Boolean>(false)
    val loading: LiveData<Boolean> = _loading

    fun apply(workshop: Workshop){
        viewModelScope.launch (Dispatchers.IO){
            workshop.applied = true
            repository.update(workshop)
        }
    }

    fun withdraw(workshop: Workshop){
        viewModelScope.launch(Dispatchers.IO) {
            workshop.applied = false
            repository.update(workshop)
        }
    }

    fun clearAll (){
        viewModelScope.launch(Dispatchers.IO) {
            repository.clearAll()
        }
    }
    fun login(email: String, password: String) {


        val retrofit = RetrofitClient.instance
        val apiInterface = retrofit.create(ApiInterface::class.java)

        viewModelScope.launch {
            try {
                val params : HashMap<String, String> = HashMap()
                params["email"] = email
                params["pass"] = password
                val response = apiInterface.login(params)

                Log.d("TAG", "login: $response")
                Log.d("TAG", "login: ${response.message()}")
                Log.d("TAG", "login: ${response.raw()}")

                if (response.isSuccessful) {
                    //your code for handling success response
                    val authResponse = response.body()
                    Log.d("TAG", "login request: $params")
                    Log.d("TAG", "login response: ${authResponse.toString()}")
                    if (authResponse != null) {
                        _authResult.value = Result.success(authResponse.response)
                    } else {
                        _authResult.value = Result.failure(Exception("User is null"))
                    }

                } else {
                    val errorResponse = getError(response.errorBody()!!.charStream())
                    _authResult.value = Result.failure(Exception(errorResponse.message))
                }

            } catch (Ex: Exception) {
                Ex.localizedMessage?.let { Log.e("Error", it) }
                _authResult.value = Result.failure(Exception(Ex.localizedMessage?.toString()))
            }
        }
    }


    fun register(username: String,email: String, password: String) {
        // can be launched in a separate asynchronous job

        val retrofit = RetrofitClient.instance
        val apiInterface = retrofit.create(ApiInterface::class.java)

        viewModelScope.launch {
            try {
                val params : HashMap<String, String> = HashMap()
                params["username"] = username
                params["email"] = email
                params["pass"] = password
                val response = apiInterface.register(params)
                if (response.isSuccessful) {
                    //your code for handling success response
                    val authResponse = response.body()
                    Log.d("TAG", "register request: $params")
                    Log.d("TAG", "register response: ${authResponse.toString()}")

                    if (authResponse != null) {
                        _authResult.value = Result.success(authResponse.response)
                    } else {
                        _authResult.value = Result.failure(Exception("Unknown Error!"))
                    }
                } else {
                    val errorResponse = getError(response.errorBody()!!.charStream())
                    _authResult.value = Result.failure(Exception(errorResponse.message))
                    Log.d("TAG", "register: Error - $errorResponse")
                }
            } catch (Ex: Exception) {
                Ex.localizedMessage?.let { Log.e("Error", it) }
                _authResult.value = Result.failure(Exception(Ex.localizedMessage?.toString()))
            }
        }
    }


    private fun getError(charStream: Reader): ErrorResponse {
        val gson = Gson()
        val type = object : TypeToken<ErrorResponse?>() {}.type
        return gson.fromJson(
            charStream, type
        )
    }

    // public loading state setter
    fun isLoading(isLoading : Boolean){
        _loading.value = isLoading
    }

}