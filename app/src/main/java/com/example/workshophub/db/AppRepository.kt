package com.example.workshophub.db

import androidx.lifecycle.LiveData
import com.example.workshophub.models.Workshop

class AppRepository(private val workshopDao: WorkshopDao) {

    fun getAllWorkshop(): LiveData<List<Workshop>> = workshopDao.getAllWorkshop()
    fun getAllAppliedWorkshop(): LiveData<List<Workshop>> = workshopDao.getAllAppliedWorkshop()

    suspend fun update(workshop: Workshop) = workshopDao.update(workshop)

    suspend fun clearAll() = workshopDao.clearAll()
}