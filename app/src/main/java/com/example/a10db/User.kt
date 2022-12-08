package com.example.a10db

import android.text.Editable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey (autoGenerate = true) val uid: Int = 0,
    @ColumnInfo (name = "name") val name: String?,
    @ColumnInfo (name = "gender") val gender: Boolean?,
    @ColumnInfo (name = "age") val age: Int
)