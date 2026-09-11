package com.example.futuruscare.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.futuruscare.data.Converters
import com.example.futuruscare.data.PetsittingStatus

@Entity(
    tableName = "tb_petsitting",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id_user"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Pet::class,
            parentColumns = ["id_pet"],
            childColumns = ["pet_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Petsitter::class,
            parentColumns = ["id_petsitter"],
            childColumns = ["petsitter_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
@TypeConverters(Converters::class)
data class Petsitting(
    @PrimaryKey(autoGenerate = true) val id_petsitting: Long = 0,
    val user_id: Long,
    val pet_id: Long,
    val petsitter_id: Long,
    val petsitting_date: String,
    val status: PetsittingStatus
)