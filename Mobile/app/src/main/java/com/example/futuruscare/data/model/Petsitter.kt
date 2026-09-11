package com.example.futuruscare.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.futuruscare.data.Converters

@Entity(
    tableName = "tb_petsitter",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id_user"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
@TypeConverters(Converters::class)
data class Petsitter(
    @PrimaryKey(autoGenerate = true) val id_petsitter: Long = 0,
    val user_id: Long,
    val specialization: String,
    val phone: String,
    val rating: Double,
    val experience_years: Int
)