package com.example.futuruscare.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.futuruscare.data.Converters

@Entity(tableName = "tb_procedure")
@TypeConverters(Converters::class)
data class Procedure(
    @PrimaryKey(autoGenerate = true) val id_procedure: Long = 0,
    val diagnostic: String,
    val description: String,
    val procedure_value: Double
)