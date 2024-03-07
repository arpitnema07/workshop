package com.example.workshophub.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workshop_table")
data class Workshop(
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "imageId")
    val imageId: String,
    @ColumnInfo(name = "date")
    var date: String,
    @ColumnInfo(name = "applied")
    var applied: Boolean
) {
    @PrimaryKey(autoGenerate = true)
    var wid = 0


    val isEmpty
        get() = title.isEmpty() || imageId.isEmpty()

    fun equals(other: Workshop): Boolean {
        if (this === other) return true

        if (title != other.title) return false
        if (imageId != other.imageId) return false
        return applied == other.applied
    }

    override fun toString(): String {
        return "Workshop(title='$title', imageId='$imageId', date='$date', applied=$applied, wid=$wid, isEmpty=$isEmpty)"
    }


}
