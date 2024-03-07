package com.example.workshophub

import android.app.Application
import com.example.workshophub.db.AppDatabase
import com.example.workshophub.db.AppRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class WorkshopApplication : Application() {
    val applicationScope = CoroutineScope(SupervisorJob())

    private val database by lazy { AppDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { AppRepository(database.workshopDao) }
}