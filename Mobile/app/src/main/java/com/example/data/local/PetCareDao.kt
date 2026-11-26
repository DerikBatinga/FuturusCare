package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.AppNotificationEntity
import com.example.data.model.AppSessionEntity
import com.example.data.model.BookingEntity
import com.example.data.model.BookingStatus
import com.example.data.model.CaregiverCertificateEntity
import com.example.data.model.ChatMessageEntity
import com.example.data.model.PetEntity
import com.example.data.model.TutorProfileEntity
import com.example.data.model.UserAccountEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserAccountDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertAccount(account: UserAccountEntity): Long

    @Query("SELECT * FROM user_accounts WHERE LOWER(email) = LOWER(:email) LIMIT 1")
    suspend fun getAccountByEmail(email: String): UserAccountEntity?

    @Query("SELECT * FROM user_accounts WHERE id = :userId LIMIT 1")
    suspend fun getAccountById(userId: Int): UserAccountEntity?

    @Query("UPDATE user_accounts SET email = :email WHERE id = :userId")
    suspend fun updateAccountEmail(userId: Int, email: String)
}

@Dao
interface SessionDao {
    @Query("SELECT * FROM app_session WHERE id = 1")
    fun observeSession(): Flow<AppSessionEntity?>

    @Query("SELECT * FROM app_session WHERE id = 1")
    suspend fun getSessionOnce(): AppSessionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertSession(session: AppSessionEntity)
}

@Dao
interface PetDao {
    @Query("SELECT * FROM pets WHERE userId = :userId ORDER BY createdAt DESC")
    fun observePets(userId: Int): Flow<List<PetEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPet(pet: PetEntity): Long

    @Update
    suspend fun updatePet(pet: PetEntity)

    @Query("DELETE FROM pets WHERE id = :petId")
    suspend fun deletePetById(petId: Int)
}

@Dao
interface BookingDao {
    @Query("SELECT * FROM bookings WHERE userId = :userId ORDER BY dateIso ASC, timeLabel ASC")
    fun observeBookings(userId: Int): Flow<List<BookingEntity>>

    @Query("SELECT * FROM bookings WHERE caregiverUserId = :caregiverUserId ORDER BY dateIso ASC, timeLabel ASC")
    fun observeBookingsForCaregiver(caregiverUserId: Int): Flow<List<BookingEntity>>

    @Query("SELECT * FROM bookings WHERE id = :bookingId LIMIT 1")
    suspend fun getBookingById(bookingId: Int): BookingEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooking(booking: BookingEntity): Long

    @Query("UPDATE bookings SET status = :status WHERE id = :bookingId")
    suspend fun updateBookingStatus(bookingId: Int, status: BookingStatus)

    @Query(
        "UPDATE bookings SET status = :status, cancellationReason = :reason, cancelledByCaregiver = :cancelledByCaregiver WHERE id = :bookingId"
    )
    suspend fun cancelBookingWithReason(
        bookingId: Int,
        status: BookingStatus,
        reason: String,
        cancelledByCaregiver: Boolean
    )

    @Query("DELETE FROM bookings WHERE id = :bookingId")
    suspend fun deleteBookingById(bookingId: Int)
}

@Dao
interface ChatDao {
    @Query("SELECT * FROM chat_messages WHERE userId = :userId ORDER BY createdAt ASC")
    fun observeAllMessages(userId: Int): Flow<List<ChatMessageEntity>>

    @Query("SELECT * FROM chat_messages WHERE userId = :userId AND caregiverId = :caregiverId ORDER BY createdAt ASC")
    fun observeMessagesForCaregiver(userId: Int, caregiverId: String): Flow<List<ChatMessageEntity>>

    @Query("SELECT * FROM chat_messages WHERE caregiverUserId = :caregiverUserId ORDER BY createdAt ASC")
    fun observeMessagesForCaregiverAccount(caregiverUserId: Int): Flow<List<ChatMessageEntity>>

    @Query(
        "SELECT * FROM chat_messages WHERE caregiverUserId = :caregiverUserId AND userId = :clientUserId AND caregiverId = :caregiverId ORDER BY createdAt ASC"
    )
    fun observeThreadMessagesForCaregiverAccount(
        caregiverUserId: Int,
        clientUserId: Int,
        caregiverId: String
    ): Flow<List<ChatMessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: ChatMessageEntity)
}

@Dao
interface ProfileDao {
    @Query("SELECT * FROM tutor_profile WHERE userId = :userId")
    fun observeProfile(userId: Int): Flow<TutorProfileEntity?>

    @Query("SELECT * FROM tutor_profile WHERE userId = :userId")
    suspend fun getProfileOnce(userId: Int): TutorProfileEntity?

    @Query("SELECT * FROM tutor_profile WHERE isCaregiver = 1 AND isAvailableForClients = 1")
    fun observeAvailableCaregivers(): Flow<List<TutorProfileEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertProfile(profile: TutorProfileEntity)
}

@Dao
interface CertificateDao {
    @Query("SELECT * FROM caregiver_certificates WHERE userId = :userId ORDER BY createdAt DESC")
    fun observeCertificates(userId: Int): Flow<List<CaregiverCertificateEntity>>

    @Query("SELECT COUNT(*) FROM caregiver_certificates WHERE userId = :userId")
    suspend fun getCertificateCount(userId: Int): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCertificate(certificate: CaregiverCertificateEntity): Long

    @Query("DELETE FROM caregiver_certificates WHERE id = :certificateId")
    suspend fun deleteCertificateById(certificateId: Int)
}

@Dao
interface NotificationDao {
    @Query("SELECT * FROM app_notifications WHERE userId = :userId ORDER BY createdAt DESC")
    fun observeNotifications(userId: Int): Flow<List<AppNotificationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: AppNotificationEntity): Long

    @Query("UPDATE app_notifications SET isRead = 1 WHERE id = :notificationId")
    suspend fun markAsRead(notificationId: Int)

    @Query("UPDATE app_notifications SET isRead = 1 WHERE userId = :userId")
    suspend fun markAllAsRead(userId: Int)

    @Query("DELETE FROM app_notifications WHERE userId = :userId AND isRead = 1")
    suspend fun clearReadNotifications(userId: Int)
}
