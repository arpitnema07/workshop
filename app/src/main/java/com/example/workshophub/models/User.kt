package com.example.workshophub.models

data class User (
    val _id: String,
    val email: String,
    val files: List<Any>,
    val imageUrl: String,
    val token: String,
    val username: String
) : java.io.Serializable