package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.data.model.AppNotificationEntity
import com.example.data.model.AppSessionEntity
import com.example.data.model.BookingEntity
import com.example.data.model.BookingStatus
import com.example.data.model.CaregiverCertificateEntity
import com.example.data.model.ChatMessageEntity
import com.example.data.model.NotificationType
import com.example.data.model.PetEntity
import com.example.data.model.TutorProfileEntity
import com.example.data.model.UserAccountEntity

class PetCareConverters {
    @TypeConverter
    fun fromBookingStatus(status: BookingStatus): String = status.name

    @TypeConverter
    fun toBookingStatus(value: String): BookingStatus = BookingStatus.fromName(value)

    @TypeConverter
    fun fromNotificationType(type: NotificationType): String = type.name

    @TypeConverter
    fun toNotificationType(value: String): NotificationType = NotificationType.fromName(value)
}

@Database(
    entities = [
        UserAccountEntity::class,
        AppSessionEntity::class,
        PetEntity::class,
        BookingEntity::class,
        ChatMessageEntity::class,
        CaregiverCertificateEntity::class,
        TutorProfileEntity::class,
        AppNotificationEntity::class
    ],
    version = 4,
    exportSchema = false
)
@TypeConverters(PetCareConverters::class)
abstract class PetCareDatabase : RoomDatabase() {
    abstract fun userAccountDao(): UserAccountDao
    abstract fun sessionDao(): SessionDao
    abstract fun petDao(): PetDao
    abstract fun bookingDao(): BookingDao
    abstract fun chatDao(): ChatDao
    abstract fun profileDao(): ProfileDao
    abstract fun certificateDao(): CertificateDao
    abstract fun notificationDao(): NotificationDao

    companion object {
        @Volatile
        private var INSTANCE: PetCareDatabase? = null

        fun getInstance(context: Context): PetCareDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    PetCareDatabase::class.java,
                    "petcare_database.db"
                )
                    .fallbackToDestructiveMigration(dropAllTables = true)
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
