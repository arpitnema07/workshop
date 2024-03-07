package com.example.workshophub.network

import com.example.workshophub.models.AuthResponse
import retrofit2.Response
import retrofit2.http.*

interface ApiInterface {

    /**@param params: email and pass are the params used to login
     * @return LoggedInUser Class wrapped inside of http Response Object
     * @author Arpit Nema */
    @FormUrlEncoded
    @POST("login")
    suspend fun login(@FieldMap params: Map<String,String>): Response<AuthResponse>

    /**@param params: email and pass are the params used to login
     * @return LoggedInUser Class wrapped inside of http Response Object
     * @author Arpit Nema */
    @FormUrlEncoded
    @POST("signup")
    suspend fun register(@FieldMap params: Map<String,String>): Response<AuthResponse>


}