package com.example.workshophub.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.workshophub.models.Workshop

@Dao
interface WorkshopDao {

    @Insert
    suspend fun insert(workshop: Workshop)

    @Delete
    suspend fun delete(workshop: Workshop)

    @Update
    suspend fun update(workshop: Workshop)

    @Query("SELECT * FROM workshop_table WHERE applied = :key ORDER BY wid DESC")
    fun getAllWorkshop(key: Boolean = false): LiveData<List<Workshop>>

    @Query("SELECT * FROM workshop_table WHERE applied = :key ORDER BY wid DESC")
    fun getAllAppliedWorkshop(key: Boolean = true): LiveData<List<Workshop>>

    @Query("DELETE FROM workshop_table")
    suspend fun deleteAll()

    @Query("UPDATE workshop_table SET applied = 0")
    suspend fun clearAll()

}