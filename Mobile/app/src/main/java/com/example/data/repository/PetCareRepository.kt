package com.example.data.repository

import com.example.data.local.BookingDao
import com.example.data.local.CertificateDao
import com.example.data.local.ChatDao
import com.example.data.local.NotificationDao
import com.example.data.local.PetDao
import com.example.data.local.ProfileDao
import com.example.data.local.SessionDao
import com.example.data.local.UserAccountDao
import com.example.data.model.AppNotificationEntity
import com.example.data.model.AppSessionEntity
import com.example.data.model.BookingEntity
import com.example.data.model.BookingStatus
import com.example.data.model.Caregiver
import com.example.data.model.CaregiverCertificateEntity
import com.example.data.model.CaregiverMockData
import com.example.data.model.ChatMessageEntity
import com.example.data.model.NotificationType
import com.example.data.model.PetEntity
import com.example.data.model.ServiceCategory
import com.example.data.model.TutorProfileEntity
import com.example.data.model.UserAccountEntity
import com.example.ui.theme.CaregiverToneMint
import com.example.ui.theme.CaregiverTonePeach
import com.example.ui.theme.CaregiverToneRose
import com.example.ui.theme.CaregiverToneSky
import com.example.ui.theme.CaregiverToneViolet
import com.example.util.PasswordHasher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PetCareRepository(
    private val userAccountDao: UserAccountDao,
    private val sessionDao: SessionDao,
    private val petDao: PetDao,
    private val bookingDao: BookingDao,
    private val chatDao: ChatDao,
    private val profileDao: ProfileDao,
    private val certificateDao: CertificateDao,
    private val notificationDao: NotificationDao
) {
    val activeUserId: Flow<Int?> = sessionDao.observeSession().map { it?.loggedInUserId }

    val caregivers: List<Caregiver> = CaregiverMockData.caregivers
    val services: List<ServiceCategory> = CaregiverMockData.services

    fun observeAllCaregivers(): Flow<List<Caregiver>> =
        profileDao.observeAvailableCaregivers().map { localProfiles ->
            val tones = listOf(
                CaregiverToneViolet,
                CaregiverToneSky,
                CaregiverToneMint,
                CaregiverToneRose,
                CaregiverTonePeach
            )
            val localMapped = localProfiles.mapIndexed { idx, profile ->
                val specs = profile.specialtiesList.ifEmpty { listOf("Dog Walking") }
                val primarySpec = specs.firstOrNull() ?: "Dog Walking"
                val defaultUnit = services.firstOrNull { it.title == primarySpec }?.priceUnit ?: "/h"
                val initials = profile.name
                    .trim()
                    .split(" ")
                    .filter { it.isNotBlank() }
                    .take(2)
                    .joinToString("") { it.take(1).uppercase() }
                    .ifBlank { "FC" }

                Caregiver(
                    id = "local_user_${profile.userId}",
                    name = profile.name,
                    roleTitle = profile.headline.ifBlank { primarySpec },
                    neighborhood = profile.neighborhood.ifBlank { profile.city.ifBlank { "Região local" } },
                    distanceLabel = "Local",
                    rating = 5.0,
                    reviewCount = maxOf(1, profile.certificateCount * 3),
                    pricePerSession = profile.pricePerSession.takeIf { it > 0 } ?: 50.0,
                    priceUnit = profile.priceUnit.ifBlank { defaultUnit },
                    specialties = specs,
                    bio = buildString {
                        append(
                            profile.bio.ifBlank {
                                "Cuidador com ${profile.experienceYears} anos de experiência em $primarySpec."
                            }
                        )
                        if (profile.highlightsList.isNotEmpty()) {
                            append(" Diferenciais: ")
                            append(profile.highlightsList.joinToString(", "))
                            append(".")
                        }
                    },
                    verified = profile.isVerified,
                    experienceYears = profile.experienceYears,
                    completedServices = maxOf(1, profile.experienceYears * 12),
                    responseTime = "Responde rápido",
                    initials = initials,
                    avatarTone = tones[idx % tones.size]
                )
            }
            localMapped + CaregiverMockData.caregivers
        }

    fun observePets(userId: Int): Flow<List<PetEntity>> = petDao.observePets(userId)
    fun observeBookings(userId: Int): Flow<List<BookingEntity>> = bookingDao.observeBookings(userId)
    fun observeBookingsForCaregiver(caregiverUserId: Int): Flow<List<BookingEntity>> =
        bookingDao.observeBookingsForCaregiver(caregiverUserId)

    fun observeAllMessages(userId: Int): Flow<List<ChatMessageEntity>> = chatDao.observeAllMessages(userId)
    fun observeMessagesForCaregiverAccount(caregiverUserId: Int): Flow<List<ChatMessageEntity>> =
        chatDao.observeMessagesForCaregiverAccount(caregiverUserId)

    fun observeThreadMessagesForCaregiverAccount(
        caregiverUserId: Int,
        clientUserId: Int,
        caregiverId: String
    ): Flow<List<ChatMessageEntity>> =
        chatDao.observeThreadMessagesForCaregiverAccount(caregiverUserId, clientUserId, caregiverId)

    fun observeProfile(userId: Int): Flow<TutorProfileEntity?> = profileDao.observeProfile(userId)
    fun observeCertificates(userId: Int): Flow<List<CaregiverCertificateEntity>> =
        certificateDao.observeCertificates(userId)

    fun observeNotifications(userId: Int): Flow<List<AppNotificationEntity>> =
        notificationDao.observeNotifications(userId)

    fun observeMessagesForCaregiver(userId: Int, caregiverId: String): Flow<List<ChatMessageEntity>> =
        chatDao.observeMessagesForCaregiver(userId, caregiverId)

    /**
     * Registra uma nova conta no banco Room local.
     * Por padrão, a conta inicia com ZERO pets e ZERO agendamentos.
     */
    suspend fun registerAccount(
        name: String,
        email: String,
        password: String,
        phone: String,
        address: String,
        city: String,
        neighborhood: String,
        bio: String,
        avatarPath: String?,
        isCaregiver: Boolean = false,
        specialty: String = "",
        experienceYears: String = ""
    ): Result<Int> {
        val cleanEmail = email.trim().lowercase(Locale.ROOT)
        val cleanName = name.trim()
        if (cleanName.isBlank()) {
            return Result.failure(IllegalArgumentException("Informe seu nome completo."))
        }
        if (cleanEmail.isBlank() || !cleanEmail.contains("@")) {
            return Result.failure(IllegalArgumentException("Informe um e-mail válido."))
        }
        if (password.length < 8) {
            return Result.failure(IllegalArgumentException("A senha precisa ter pelo menos 8 caracteres."))
        }

        val existing = userAccountDao.getAccountByEmail(cleanEmail)
        if (existing != null) {
            return Result.failure(IllegalStateException("Este e-mail já está cadastrado neste aparelho."))
        }

        val salt = PasswordHasher.generateSalt()
        val hash = PasswordHasher.hashPassword(password, salt)

        val userId = userAccountDao.insertAccount(
            UserAccountEntity(
                email = cleanEmail,
                passwordHash = hash,
                passwordSalt = salt,
                isCaregiver = isCaregiver
            )
        ).toInt()

        val cleanSpec = if (isCaregiver) specialty.trim().ifBlank { "Dog Walking" } else ""
        val defaultService = services.firstOrNull { it.title == cleanSpec }
        val initialProfile = TutorProfileEntity(
            userId = userId,
            name = cleanName,
            email = cleanEmail,
            phone = phone.trim(),
            address = address.trim(),
            city = city.trim(),
            neighborhood = neighborhood.trim(),
            bio = bio.trim(),
            isCaregiver = isCaregiver,
            specialty = cleanSpec,
            experienceYears = if (isCaregiver) experienceYears.trim().toIntOrNull()?.coerceAtLeast(0) ?: 0 else 0,
            headline = if (isCaregiver) "$cleanSpec com carinho e segurança" else "",
            specialtiesCsv = cleanSpec,
            pricePerSession = defaultService?.basePrice ?: 55.0,
            priceUnit = defaultService?.priceUnit ?: "/h",
            highlightsCsv = if (isCaregiver) "Relatório com fotos" else "",
            isAvailableForClients = true,
            certificateCount = 0,
            avatarPath = avatarPath,
            bannerPath = null,
            notificationsEnabled = true,
            verifiedNotified = false
        )
        profileDao.upsertProfile(initialProfile)

        val welcomeTitle = if (isCaregiver) {
            "Bem-vindo(a) ao painel de Cuidador, ${cleanName.substringBefore(" ")}!"
        } else {
            "Bem-vindo ao FuturusCare, ${cleanName.substringBefore(" ")}!"
        }
        val welcomeBody = if (isCaregiver) {
            "Capriche no seu perfil profissional, adicione foto e anexe ao menos 1 certificado para liberar o selo Cuidador Verificado."
        } else if (initialProfile.isVerified) {
            "Seu perfil já está verificado. Cadastre seu primeiro pet para começar a agendar."
        } else {
            "Cadastre seu primeiro pet e complete seu perfil com foto para liberar o selo Verificado."
        }

        notificationDao.insertNotification(
            AppNotificationEntity(
                userId = userId,
                title = welcomeTitle,
                body = welcomeBody,
                type = NotificationType.WELCOME,
                relatedRoute = if (isCaregiver) "profile" else "pets",
                timeLabel = currentTimeLabel()
            )
        )

        sessionDao.upsertSession(AppSessionEntity(id = 1, loggedInUserId = userId))
        return Result.success(userId)
    }

    suspend fun loginAccount(
        email: String,
        password: String,
        expectedCaregiver: Boolean? = null
    ): Result<Int> {
        val cleanEmail = email.trim().lowercase(Locale.ROOT)
        if (cleanEmail.isBlank() || password.isBlank()) {
            return Result.failure(IllegalArgumentException("Preencha o e-mail e a senha."))
        }
        val account = userAccountDao.getAccountByEmail(cleanEmail)
            ?: return Result.failure(IllegalArgumentException("Confira o e-mail e a senha da conta local."))

        if (!PasswordHasher.verifyPassword(password, account.passwordSalt, account.passwordHash)) {
            return Result.failure(IllegalArgumentException("Confira o e-mail e a senha da conta local."))
        }

        if (expectedCaregiver != null && account.isCaregiver != expectedCaregiver) {
            return Result.failure(IllegalArgumentException("Selecione o tipo correto de conta: Cliente ou PetSitter."))
        }

        if (profileDao.getProfileOnce(account.id) == null) {
            profileDao.upsertProfile(
                TutorProfileEntity(
                    userId = account.id,
                    name = cleanEmail.substringBefore("@").replaceFirstChar { it.uppercase() },
                    email = cleanEmail,
                    isCaregiver = account.isCaregiver,
                    specialty = if (account.isCaregiver) "Dog Walking" else "",
                    specialtiesCsv = if (account.isCaregiver) "Dog Walking" else ""
                )
            )
        }

        sessionDao.upsertSession(AppSessionEntity(id = 1, loggedInUserId = account.id))
        return Result.success(account.id)
    }

    suspend fun logout() {
        sessionDao.upsertSession(AppSessionEntity(id = 1, loggedInUserId = null))
    }

    suspend fun addPet(pet: PetEntity): Long = petDao.insertPet(pet)
    suspend fun updatePet(pet: PetEntity) = petDao.updatePet(pet)
    suspend fun deletePet(petId: Int) = petDao.deletePetById(petId)

    suspend fun createBooking(booking: BookingEntity): Long = bookingDao.insertBooking(booking)
    suspend fun getBookingById(bookingId: Int): BookingEntity? = bookingDao.getBookingById(bookingId)
    suspend fun updateBookingStatus(bookingId: Int, status: BookingStatus) =
        bookingDao.updateBookingStatus(bookingId, status)

    suspend fun cancelBookingWithReason(
        bookingId: Int,
        reason: String,
        cancelledByCaregiver: Boolean
    ) {
        bookingDao.cancelBookingWithReason(
            bookingId = bookingId,
            status = BookingStatus.RECUSADO,
            reason = reason.trim(),
            cancelledByCaregiver = cancelledByCaregiver
        )
    }

    suspend fun deleteBooking(bookingId: Int) = bookingDao.deleteBookingById(bookingId)

    suspend fun sendMessage(message: ChatMessageEntity) = chatDao.insertMessage(message)

    suspend fun addCertificate(certificate: CaregiverCertificateEntity): Int {
        certificateDao.insertCertificate(certificate)
        return certificateDao.getCertificateCount(certificate.userId)
    }

    suspend fun deleteCertificate(certificateId: Int, userId: Int): Int {
        certificateDao.deleteCertificateById(certificateId)
        return certificateDao.getCertificateCount(userId)
    }

    suspend fun updateProfile(profile: TutorProfileEntity) {
        profileDao.upsertProfile(profile)
        val cleanEmail = profile.email.trim().lowercase(Locale.ROOT)
        if (cleanEmail.isNotBlank()) {
            val existingOwner = userAccountDao.getAccountByEmail(cleanEmail)
            if (existingOwner == null || existingOwner.id == profile.userId) {
                userAccountDao.updateAccountEmail(profile.userId, cleanEmail)
            }
        }
    }

    suspend fun addNotification(
        userId: Int,
        title: String,
        body: String,
        type: NotificationType,
        relatedRoute: String? = null
    ): Long = notificationDao.insertNotification(
        AppNotificationEntity(
            userId = userId,
            title = title,
            body = body,
            type = type,
            relatedRoute = relatedRoute,
            timeLabel = currentTimeLabel()
        )
    )

    suspend fun markNotificationAsRead(notificationId: Int) =
        notificationDao.markAsRead(notificationId)

    suspend fun markAllNotificationsAsRead(userId: Int) =
        notificationDao.markAllAsRead(userId)

    suspend fun clearReadNotifications(userId: Int) =
        notificationDao.clearReadNotifications(userId)

    private fun currentTimeLabel(): String =
        SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
}
