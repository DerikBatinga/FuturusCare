package com.example.futuruscare.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.futuruscare.data.Converters
import com.example.futuruscare.data.PetGender

@Entity(
    tableName = "tb_pet",
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
data class Pet(
    @PrimaryKey(autoGenerate = true) val id_pet: Long = 0,
    val user_id: Long,
    val first_name: String,
    val second_name: String,
    val gender: PetGender,
    val birthday: String,
    val current_at: String
)