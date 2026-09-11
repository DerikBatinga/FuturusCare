package com.example.futuruscare.data

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromStatus(status: Status): String = status.name

    @TypeConverter
    fun toStatus(value: String): Status = Status.valueOf(value)

    @TypeConverter
    fun fromPetGender(gender: PetGender): String = gender.name

    @TypeConverter
    fun toPetGender(value: String): PetGender = PetGender.valueOf(value)

    @TypeConverter
    fun fromPetsittingStatus(status: PetsittingStatus): String = status.name

    @TypeConverter
    fun toPetsittingStatus(value: String): PetsittingStatus = PetsittingStatus.valueOf(value)

    @TypeConverter
    fun fromPaymentForm(form: PaymentForm): String = form.name

    @TypeConverter
    fun toPaymentForm(value: String): PaymentForm = PaymentForm.valueOf(value)

    @TypeConverter
    fun fromPaymentStatus(status: PaymentStatus): String = status.name

    @TypeConverter
    fun toPaymentStatus(value: String): PaymentStatus = PaymentStatus.valueOf(value)
}