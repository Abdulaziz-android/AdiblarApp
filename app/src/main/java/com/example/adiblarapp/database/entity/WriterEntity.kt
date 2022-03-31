package com.example.adiblarapp.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WriterEntity(
    @PrimaryKey
    val name:String
)
