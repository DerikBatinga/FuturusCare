package com.example.data.model

import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.ui.theme.CaregiverToneAmber
import com.example.ui.theme.CaregiverToneMint
import com.example.ui.theme.CaregiverTonePeach
import com.example.ui.theme.CaregiverToneRose
import com.example.ui.theme.CaregiverToneSky
import com.example.ui.theme.CaregiverToneViolet
import com.example.ui.theme.StatusCompletedBg
import com.example.ui.theme.StatusCompletedText
import com.example.ui.theme.StatusDeclinedBg
import com.example.ui.theme.StatusDeclinedText
import com.example.ui.theme.StatusInProgressBg
import com.example.ui.theme.StatusInProgressText
import com.example.ui.theme.StatusPendingBg
import com.example.ui.theme.StatusPendingText
import com.example.ui.theme.StatusScheduledBg
import com.example.ui.theme.StatusScheduledText

enum class BookingStatus(
    val label: String,
    val containerColor: Color,
    val contentColor: Color
) {
    PENDENTE(
        label = "Pendente",
        containerColor = StatusPendingBg,
        contentColor = StatusPendingText
    ),
    AGENDADO(
        label = "Agendado",
        containerColor = StatusScheduledBg,
        contentColor = StatusScheduledText
    ),
    EM_ANDAMENTO(
        label = "Em andamento",
        containerColor = StatusInProgressBg,
        contentColor = StatusInProgressText
    ),
    CONCLUIDO(
        label = "Concluído",
        containerColor = StatusCompletedBg,
        contentColor = StatusCompletedText
    ),
    RECUSADO(
        label = "Recusado",
        containerColor = StatusDeclinedBg,
        contentColor = StatusDeclinedText
    );

    companion object {
        fun fromName(raw: String): BookingStatus =
            entries.firstOrNull { it.name == raw } ?: PENDENTE
    }
}

enum class NotificationType(
    val categoryLabel: String,
    val badgeColor: Color,
    val badgeTextColor: Color
) {
    BOOKING_CREATED(
        categoryLabel = "Agendamentos",
        badgeColor = StatusScheduledBg,
        badgeTextColor = StatusScheduledText
    ),
    BOOKING_STATUS(
        categoryLabel = "Agendamentos",
        badgeColor = StatusInProgressBg,
        badgeTextColor = StatusInProgressText
    ),
    PET_ADDED(
        categoryLabel = "Pets",
        badgeColor = StatusCompletedBg,
        badgeTextColor = StatusCompletedText
    ),
    PROFILE_VERIFIED(
        categoryLabel = "Verificação",
        badgeColor = StatusCompletedBg,
        badgeTextColor = StatusCompletedText
    ),
    CERTIFICATE_ADDED(
        categoryLabel = "Verificação",
        badgeColor = StatusInProgressBg,
        badgeTextColor = StatusInProgressText
    ),
    CHAT_MESSAGE(
        categoryLabel = "Mensagens",
        badgeColor = StatusInProgressBg,
        badgeTextColor = StatusInProgressText
    ),
    LOCATION_UPDATED(
        categoryLabel = "Localização",
        badgeColor = StatusPendingBg,
        badgeTextColor = StatusPendingText
    ),
    WELCOME(
        categoryLabel = "Conta",
        badgeColor = StatusPendingBg,
        badgeTextColor = StatusPendingText
    );

    companion object {
        fun fromName(raw: String): NotificationType =
            entries.firstOrNull { it.name == raw } ?: WELCOME
    }
}

data class ServiceCategory(
    val id: String,
    val title: String,
    val subtitle: String,
    val basePrice: Double,
    val priceUnit: String
)

data class Caregiver(
    val id: String,
    val name: String,
    val roleTitle: String,
    val neighborhood: String,
    val distanceLabel: String,
    val rating: Double,
    val reviewCount: Int,
    val pricePerSession: Double,
    val priceUnit: String,
    val specialties: List<String>,
    val bio: String,
    val verified: Boolean,
    val experienceYears: Int,
    val completedServices: Int,
    val responseTime: String,
    val initials: String,
    val avatarTone: Color
)

data class CalendarDayOption(
    val dayOfWeek: String,
    val dayNumber: String,
    val dateIso: String,
    val dateLabel: String
)

data class ClientChatThread(
    val threadKey: String,
    val clientUserId: Int,
    val caregiverId: String,
    val clientName: String,
    val clientInitials: String,
    val lastMessageText: String,
    val lastMessageHasPhoto: Boolean,
    val lastMessageTime: String,
    val lastMessageCreatedAt: Long,
    val relatedBookingStatus: BookingStatus? = null,
    val relatedPetName: String? = null
)

@Entity(
    tableName = "user_accounts",
    indices = [Index(value = ["email"], unique = true)]
)
data class UserAccountEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val email: String,
    val passwordHash: String,
    val passwordSalt: String,
    val isCaregiver: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "app_session")
data class AppSessionEntity(
    @PrimaryKey val id: Int = 1,
    val loggedInUserId: Int? = null
)

@Entity(tableName = "pets")
data class PetEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int = 0,
    val name: String,
    val species: String,
    val breed: String,
    val ageYears: String,
    val weightKg: String,
    val specialNotes: String,
    val photoPath: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "bookings")
data class BookingEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int = 0,
    val caregiverId: String,
    val caregiverUserId: Int? = null,
    val caregiverName: String,
    val caregiverInitials: String,
    val clientName: String = "",
    val clientPhone: String = "",
    val petId: Int,
    val petName: String,
    val petSpecies: String = "",
    val serviceName: String,
    val dateIso: String,
    val dateLabel: String,
    val timeLabel: String,
    val status: BookingStatus = BookingStatus.PENDENTE,
    val priceReais: Double = 45.0,
    val notes: String = "",
    val cancellationReason: String = "",
    val cancelledByCaregiver: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int = 0,
    val caregiverId: String,
    val caregiverUserId: Int? = null,
    val clientName: String = "",
    val senderIsUser: Boolean,
    val senderRole: String = "CLIENT",
    val text: String,
    val imagePath: String? = null,
    val timeLabel: String,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "caregiver_certificates")
data class CaregiverCertificateEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val title: String,
    val issuer: String = "",
    val filePath: String,
    val fileName: String,
    val isImage: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "tutor_profile")
data class TutorProfileEntity(
    @PrimaryKey val userId: Int,
    val name: String,
    val email: String,
    val phone: String = "",
    val address: String = "",
    val city: String = "",
    val neighborhood: String = "",
    val bio: String = "",
    val isCaregiver: Boolean = false,
    val specialty: String = "",
    val experienceYears: Int = 0,
    val headline: String = "",
    val specialtiesCsv: String = "",
    val pricePerSession: Double = 55.0,
    val priceUnit: String = "/h",
    val highlightsCsv: String = "",
    val isAvailableForClients: Boolean = true,
    val certificateCount: Int = 0,
    val avatarPath: String? = null,
    val bannerPath: String? = null,
    val notificationsEnabled: Boolean = true,
    val verifiedNotified: Boolean = false
) {
    val specialtiesList: List<String>
        get() = specialtiesCsv
            .split(",")
            .map { it.trim() }
            .filter { it.isNotBlank() }
            .ifEmpty {
                listOf(specialty.trim()).filter { it.isNotBlank() }
            }

    val highlightsList: List<String>
        get() = highlightsCsv
            .split(",")
            .map { it.trim() }
            .filter { it.isNotBlank() }

    val hasProfilePhoto: Boolean
        get() = !avatarPath.isNullOrBlank()

    val hasAllProfileFields: Boolean
        get() = name.isNotBlank() &&
            email.isNotBlank() &&
            phone.isNotBlank() &&
            address.isNotBlank() &&
            city.isNotBlank() &&
            neighborhood.isNotBlank() &&
            bio.isNotBlank()

    val hasCaregiverShowcaseFields: Boolean
        get() = !isCaregiver || (specialtiesList.isNotEmpty() && pricePerSession > 0.0)

    val hasCaregiverCertificate: Boolean
        get() = !isCaregiver || certificateCount > 0

    /**
     * Selo de verificado aparece SOMENTE quando o usuário envia foto de perfil,
     * preenche todas as informações obrigatórias e, no caso de Cuidador,
     * informa especialidade/valor e anexa ao menos 1 certificado.
     */
    val isVerified: Boolean
        get() = hasProfilePhoto &&
            hasAllProfileFields &&
            hasCaregiverShowcaseFields &&
            hasCaregiverCertificate

    val totalVerificationSteps: Int
        get() = if (isCaregiver) 10 else 8

    val missingVerificationItems: List<String>
        get() = buildList {
            if (!hasProfilePhoto) add("Foto de perfil")
            if (name.isBlank()) add("Nome completo")
            if (email.isBlank()) add("E-mail")
            if (phone.isBlank()) add("Telefone")
            if (address.isBlank()) add("Endereço")
            if (city.isBlank()) add("Cidade")
            if (neighborhood.isBlank()) add("Bairro")
            if (bio.isBlank()) add(if (isCaregiver) "Apresentação atrativa" else "Sobre você")
            if (isCaregiver && !hasCaregiverShowcaseFields) add("Especialidade e valor")
            if (isCaregiver && !hasCaregiverCertificate) add("Anexar certificado")
        }

    val completionCount: Int
        get() = (totalVerificationSteps - missingVerificationItems.size).coerceIn(0, totalVerificationSteps)

    val formattedRegion: String
        get() = listOf(neighborhood.trim(), city.trim())
            .filter { it.isNotBlank() }
            .joinToString(", ")
            .ifBlank { "Localização não definida" }
}

@Entity(tableName = "app_notifications")
data class AppNotificationEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int = 0,
    val title: String,
    val body: String,
    val type: NotificationType,
    val isRead: Boolean = false,
    val relatedRoute: String? = null,
    val timeLabel: String,
    val createdAt: Long = System.currentTimeMillis()
)

object CaregiverMockData {

    val services: List<ServiceCategory> = listOf(
        ServiceCategory(
            id = "dog_walking",
            title = "Dog Walking",
            subtitle = "Passeios com carinho",
            basePrice = 45.0,
            priceUnit = "/h"
        ),
        ServiceCategory(
            id = "pet_sitting",
            title = "Pet Sitting",
            subtitle = "Cuidado em casa",
            basePrice = 55.0,
            priceUnit = "/visita"
        ),
        ServiceCategory(
            id = "hospedagem",
            title = "Hospedagem",
            subtitle = "Lar acolhedor 24h",
            basePrice = 85.0,
            priceUnit = "/diária"
        ),
        ServiceCategory(
            id = "banho_tosa",
            title = "Banho & Tosa",
            subtitle = "Higiene sem estresse",
            basePrice = 80.0,
            priceUnit = "/sessão"
        )
    )

    val caregiverHighlightsCatalog: List<String> = listOf(
        "Relatório com fotos",
        "Primeiros socorros pet",
        "Aceita cães idosos",
        "Administra medicação",
        "Quintal seguro",
        "Adaptação gentil"
    )

    val caregivers: List<Caregiver> = listOf(
        Caregiver(
            id = "cg_lucia",
            name = "Lucia Caminos",
            roleTitle = "Dog Walking & Adestramento",
            neighborhood = "Vila Mariana",
            distanceLabel = "1,1 km",
            rating = 5.0,
            reviewCount = 96,
            pricePerSession = 52.0,
            priceUnit = "/h",
            specialties = listOf("Dog Walking", "Pet Sitting"),
            bio = "Especialista em comportamento canino, passeios estruturados e adaptação gentil. Envia relatório com fotos e trajeto em cada saída.",
            verified = true,
            experienceYears = 6,
            completedServices = 340,
            responseTime = "Responde em 3 min",
            initials = "LC",
            avatarTone = CaregiverToneRose
        ),
        Caregiver(
            id = "cg_jason",
            name = "Jason Duval",
            roleTitle = "Hospedagem & Passeios",
            neighborhood = "Itaim Bibi",
            distanceLabel = "1,4 km",
            rating = 4.9,
            reviewCount = 78,
            pricePerSession = 75.0,
            priceUnit = "/diária",
            specialties = listOf("Hospedagem", "Dog Walking"),
            bio = "Casa ampla com área verde segura e rotina ativa para cães de médio e grande porte. Acompanhamento 24 horas e atualizações diárias.",
            verified = true,
            experienceYears = 5,
            completedServices = 264,
            responseTime = "Responde em 5 min",
            initials = "JD",
            avatarTone = CaregiverToneSky
        ),
        Caregiver(
            id = "cg_sabrina",
            name = "Sabrina Carpenter",
            roleTitle = "Pet Sitting Premium",
            neighborhood = "Higienópolis",
            distanceLabel = "1,7 km",
            rating = 5.0,
            reviewCount = 134,
            pricePerSession = 60.0,
            priceUnit = "/visita",
            specialties = listOf("Pet Sitting", "Banho & Tosa"),
            bio = "Cuidado delicado para cães e gatos no conforto de casa, com escovação, brincadeiras enriquecedoras e administração precisa de remédios.",
            verified = true,
            experienceYears = 4,
            completedServices = 315,
            responseTime = "Responde em 2 min",
            initials = "SC",
            avatarTone = CaregiverToneAmber
        ),
        Caregiver(
            id = "cg_benito",
            name = "Benito Antonio",
            roleTitle = "Hospedagem & Cuidados Especiais",
            neighborhood = "Brooklin",
            distanceLabel = "2,3 km",
            rating = 4.9,
            reviewCount = 89,
            pricePerSession = 88.0,
            priceUnit = "/diária",
            specialties = listOf("Hospedagem", "Pet Sitting"),
            bio = "Ambiente tranquilo sem gaiolas, ideal para pets idosos ou que precisam de atenção redobrada, carinho constante e rotina calma.",
            verified = true,
            experienceYears = 7,
            completedServices = 412,
            responseTime = "Responde em 4 min",
            initials = "BA",
            avatarTone = CaregiverToneMint
        ),
        Caregiver(
            id = "cg_derik",
            name = "Derik",
            roleTitle = "Dog Walking",
            neighborhood = "Pinheiros",
            distanceLabel = "1,2 km",
            rating = 4.9,
            reviewCount = 68,
            pricePerSession = 45.0,
            priceUnit = "/h",
            specialties = listOf("Dog Walking", "Pet Sitting"),
            bio = "Cuidador com 5 anos de experiência. Especialidade principal: Dog Walking. Converse para combinar os cuidados e a rotina do seu pet.",
            verified = true,
            experienceYears = 5,
            completedServices = 312,
            responseTime = "Responde em 5 min",
            initials = "D",
            avatarTone = CaregiverToneViolet
        ),
        Caregiver(
            id = "cg_bruno",
            name = "Bruno Oliveira",
            roleTitle = "Dog Walking",
            neighborhood = "Pinheiros",
            distanceLabel = "1,5 km",
            rating = 4.9,
            reviewCount = 84,
            pricePerSession = 45.0,
            priceUnit = "/h",
            specialties = listOf("Dog Walking", "Pet Sitting"),
            bio = "Cuidador com 5 anos de experiência. Especialidade principal: Dog Walking. Passeios educativos com hidratação e fotos.",
            verified = true,
            experienceYears = 5,
            completedServices = 290,
            responseTime = "Responde em 4 min",
            initials = "BO",
            avatarTone = CaregiverToneSky
        ),
        Caregiver(
            id = "cg_ana",
            name = "Ana Paula (Sitter)",
            roleTitle = "Pet Sitting",
            neighborhood = "Vila Madalena",
            distanceLabel = "1,8 km",
            rating = 5.0,
            reviewCount = 112,
            pricePerSession = 55.0,
            priceUnit = "/visita",
            specialties = listOf("Pet Sitting", "Hospedagem"),
            bio = "Cuidadora com 4 anos de experiência. Especialidade principal: Pet Sitting. Cuidado em domicílio com carinho e atenção.",
            verified = true,
            experienceYears = 4,
            completedServices = 240,
            responseTime = "Responde em 3 min",
            initials = "AP",
            avatarTone = CaregiverToneAmber
        ),
        Caregiver(
            id = "cg_clinica",
            name = "Clínica Pet Salles",
            roleTitle = "Hospedagem",
            neighborhood = "Moema",
            distanceLabel = "2,9 km",
            rating = 4.9,
            reviewCount = 91,
            pricePerSession = 85.0,
            priceUnit = "/diária",
            specialties = listOf("Hospedagem", "Banho & Tosa"),
            bio = "Equipe com 8 anos de experiência. Especialidades: Hospedagem e Banho & Tosa com acompanhamento veterinário.",
            verified = true,
            experienceYears = 8,
            completedServices = 510,
            responseTime = "Responde em 8 min",
            initials = "CP",
            avatarTone = CaregiverToneMint
        ),
        Caregiver(
            id = "cg_mariana",
            name = "Mariana Silva",
            roleTitle = "Pet Sitting",
            neighborhood = "Perdizes",
            distanceLabel = "2,2 km",
            rating = 4.9,
            reviewCount = 53,
            pricePerSession = 50.0,
            priceUnit = "/visita",
            specialties = listOf("Pet Sitting", "Dog Walking"),
            bio = "Cuidadora com 6 anos de experiência. Especialidade principal: Pet Sitting para cães e gatos.",
            verified = true,
            experienceYears = 6,
            completedServices = 197,
            responseTime = "Responde em 6 min",
            initials = "MS",
            avatarTone = CaregiverToneRose
        ),
        Caregiver(
            id = "cg_lucas",
            name = "Lucas Almeida",
            roleTitle = "Banho & Tosa",
            neighborhood = "Jardins",
            distanceLabel = "2,4 km",
            rating = 4.8,
            reviewCount = 44,
            pricePerSession = 80.0,
            priceUnit = "/sessão",
            specialties = listOf("Banho & Tosa", "Dog Walking"),
            bio = "Cuidador com 3 anos de experiência. Especialidade principal: Banho & Tosa sem estresse e passeios individuais.",
            verified = true,
            experienceYears = 3,
            completedServices = 185,
            responseTime = "Responde em 10 min",
            initials = "LA",
            avatarTone = CaregiverTonePeach
        ),
        Caregiver(
            id = "cg_camila",
            name = "Camila Ferreira",
            roleTitle = "Banho & Tosa Móvel",
            neighborhood = "Aclimação",
            distanceLabel = "2,6 km",
            rating = 4.9,
            reviewCount = 62,
            pricePerSession = 78.0,
            priceUnit = "/sessão",
            specialties = listOf("Banho & Tosa", "Pet Sitting"),
            bio = "Produtos hipoalergênicos, secagem silenciosa e manejo livre de estresse para cães sensíveis e gatos.",
            verified = true,
            experienceYears = 5,
            completedServices = 228,
            responseTime = "Responde em 7 min",
            initials = "CF",
            avatarTone = CaregiverToneViolet
        ),
        Caregiver(
            id = "cg_rafael",
            name = "Rafael Mendes",
            roleTitle = "Dog Walking Esportivo",
            neighborhood = "Bela Vista",
            distanceLabel = "2,0 km",
            rating = 4.8,
            reviewCount = 51,
            pricePerSession = 48.0,
            priceUnit = "/h",
            specialties = listOf("Dog Walking", "Hospedagem"),
            bio = "Rotas arborizadas com foco em gasto saudável de energia para cães jovens e atléticos.",
            verified = true,
            experienceYears = 4,
            completedServices = 173,
            responseTime = "Responde em 6 min",
            initials = "RM",
            avatarTone = CaregiverTonePeach
        )
    )

    val novemberWeekDays: List<CalendarDayOption> = listOf(
        CalendarDayOption("Sáb", "21", "2026-11-21", "21/11"),
        CalendarDayOption("Dom", "22", "2026-11-22", "22/11"),
        CalendarDayOption("Seg", "23", "2026-11-23", "23/11"),
        CalendarDayOption("Ter", "24", "2026-11-24", "24/11"),
        CalendarDayOption("Qua", "25", "2026-11-25", "25/11"),
        CalendarDayOption("Qui", "26", "2026-11-26", "26/11"),
        CalendarDayOption("Sex", "27", "2026-11-27", "27/11")
    )
}
