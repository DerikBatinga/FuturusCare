package com.example.futuruscare.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.futuruscare.data.Converters

@Entity(
    tableName = "tb_plan_employees",
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
data class PlanEmployee(
    @PrimaryKey(autoGenerate = true) val id_employee: Long = 0,
    val user_id: Long,
    val work_area: String,
    val salary: Double
)