package com.example.futuruscare.data

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.example.futuruscare.data.model.Payment
import com.example.futuruscare.data.model.Pet
import com.example.futuruscare.data.model.Petsitter
import com.example.futuruscare.data.model.Petsitting
import com.example.futuruscare.data.model.PlanEmployee
import com.example.futuruscare.data.model.Procedure
import com.example.futuruscare.data.model.User
import com.example.futuruscare.data.model.UserAddress

@Database(
    entities = [
        User::class,
        Pet::class,
        UserAddress::class,
        Petsitter::class,
        PlanEmployee::class,
        Petsitting::class,
        Payment::class,
        Procedure::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun petDao(): PetDao
    abstract fun userAddressDao(): UserAddressDao
    abstract fun petsitterDao(): PetsitterDao
    abstract fun planEmployeeDao(): PlanEmployeeDao
    abstract fun petsittingDao(): PetsittingDao
    abstract fun paymentDao(): PaymentDao
    abstract fun procedureDao(): ProcedureDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "FuturusCare.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}