package com.sumera.argallery.data.store.persistence.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "picture")
data class PersistedPicture(
        @PrimaryKey val id: String,
        val title: String,
        val author: String,
        val description: String,
        val imageUrl: String,
        val price: Int
)