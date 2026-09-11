package com.example.futuruscare.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.futuruscare.data.Converters
import com.example.futuruscare.data.Status

@Entity(tableName = "tb_user")
@TypeConverters(Converters::class)
data class User(
    @PrimaryKey(autoGenerate = true) val id_user: Long = 0,
    val first_name: String,
    val second_name: String,
    val cpf: String,
    val gender: String,
    val birthday: String,
    val email: String,
    val password: String,
    val status: Status,
    val current_at: String
)