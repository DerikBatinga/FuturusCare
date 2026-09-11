package com.example.futuruscare.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.futuruscare.data.Converters

@Entity(
    tableName = "tb_user_address",
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
data class UserAddress(
    @PrimaryKey val user_id: Long,
    val street_name: String,
    val house_number: String,
    val neighborhood: String,
    val cep: String,
    val complement: String?
)