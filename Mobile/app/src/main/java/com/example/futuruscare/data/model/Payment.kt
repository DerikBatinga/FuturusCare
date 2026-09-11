package com.example.futuruscare.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.futuruscare.data.Converters
import com.example.futuruscare.data.PaymentForm
import com.example.futuruscare.data.PaymentStatus

@Entity(
    tableName = "tb_payments",
    foreignKeys = [
        ForeignKey(
            entity = Petsitting::class,
            parentColumns = ["id_petsitting"],
            childColumns = ["petsitting_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
@TypeConverters(Converters::class)
data class Payment(
    @PrimaryKey(autoGenerate = true) val id_payment: Long = 0,
    val petsitting_id: Long,
    val payment_form: PaymentForm,
    val status_payments: PaymentStatus
)