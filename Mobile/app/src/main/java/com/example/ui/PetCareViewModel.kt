package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.model.AppNotificationEntity
import com.example.data.model.BookingEntity
import com.example.data.model.BookingStatus
import com.example.data.model.CalendarDayOption
import com.example.data.model.Caregiver
import com.example.data.model.CaregiverCertificateEntity
import com.example.data.model.CaregiverMockData
import com.example.data.model.ChatMessageEntity
import com.example.data.model.ClientChatThread
import com.example.data.model.NotificationType
import com.example.data.model.PetEntity
import com.example.data.model.ServiceCategory
import com.example.data.model.TutorProfileEntity
import com.example.data.repository.PetCareRepository
import com.example.util.DetectedLocation
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class SystemNotificationEvent(
    val id: Int,
    val title: String,
    val body: String,
    val isBookingAlert: Boolean
)

data class PetCareUiState(
    val isSessionResolved: Boolean = false,
    val activeUserId: Int? = null,
    val pets: List<PetEntity> = emptyList(),
    val bookings: List<BookingEntity> = emptyList(),
    val caregivers: List<Caregiver> = CaregiverMockData.caregivers,
    val services: List<ServiceCategory> = CaregiverMockData.services,
    val allMessages: List<ChatMessageEntity> = emptyList(),
    val certificates: List<CaregiverCertificateEntity> = emptyList(),
    val notifications: List<AppNotificationEntity> = emptyList(),
    val profile: TutorProfileEntity? = null,
    val searchQuery: String = "",
    val selectedServiceFilter: String? = null,
    val selectedDateIso: String = "2026-11-24",
    val showAllDates: Boolean = true,
    val selectedStatusFilter: BookingStatus? = null,
    val snackbarMessage: String? = null,
    val authError: String? = null,
    val isAuthLoading: Boolean = false,
    val isDetectingLocation: Boolean = false,
    val isReady: Boolean = false
) {
    val isAuthenticated: Boolean
        get() = activeUserId != null

    val isCaregiverMode: Boolean
        get() = profile?.isCaregiver == true

    val unreadNotificationCount: Int
        get() = notifications.count { !it.isRead }

    val filteredCaregivers: List<Caregiver>
        get() = caregivers.filter { caregiver ->
            val matchesService = selectedServiceFilter == null ||
                caregiver.specialties.any { it.equals(selectedServiceFilter, ignoreCase = true) }
            val q = searchQuery.trim()
            val matchesQuery = q.isEmpty() ||
                caregiver.name.contains(q, ignoreCase = true) ||
                caregiver.roleTitle.contains(q, ignoreCase = true) ||
                caregiver.neighborhood.contains(q, ignoreCase = true) ||
                caregiver.specialties.any { it.contains(q, ignoreCase = true) }
            matchesService && matchesQuery
        }

    val filteredBookings: List<BookingEntity>
        get() = bookings.filter { booking ->
            val matchesDate = showAllDates || booking.dateIso == selectedDateIso
            val matchesStatus = selectedStatusFilter == null || booking.status == selectedStatusFilter
            matchesDate && matchesStatus
        }

    val upcomingBookings: List<BookingEntity>
        get() = bookings.filter {
            it.status == BookingStatus.AGENDADO ||
                it.status == BookingStatus.EM_ANDAMENTO ||
                it.status == BookingStatus.PENDENTE
        }

    val pendingBookingsCount: Int
        get() = bookings.count { it.status == BookingStatus.PENDENTE }

    val confirmedBookingsCount: Int
        get() = bookings.count {
            it.status == BookingStatus.AGENDADO || it.status == BookingStatus.EM_ANDAMENTO
        }

    val completedBookingsCount: Int
        get() = bookings.count { it.status == BookingStatus.CONCLUIDO }

    val estimatedCaregiverEarnings: Double
        get() = bookings
            .filter {
                it.status == BookingStatus.AGENDADO ||
                    it.status == BookingStatus.EM_ANDAMENTO ||
                    it.status == BookingStatus.CONCLUIDO
            }
            .sumOf { it.priceReais }

    val clientChatThreads: List<ClientChatThread>
        get() {
            if (!isCaregiverMode) return emptyList()
            val msgsByKey = allMessages.groupBy { "${it.userId}|${it.caregiverId}" }
            val bookingsByKey = bookings.groupBy { "${it.userId}|${it.caregiverId}" }
            val keys = (msgsByKey.keys + bookingsByKey.keys).distinct()

            return keys.mapNotNull { key ->
                val threadMsgs = msgsByKey[key].orEmpty()
                val relatedBooking = bookingsByKey[key]?.lastOrNull()
                val firstMsg = threadMsgs.firstOrNull()
                val lastMsg = threadMsgs.lastOrNull()
                val clientUserId = firstMsg?.userId ?: relatedBooking?.userId ?: return@mapNotNull null
                val caregiverId = firstMsg?.caregiverId ?: relatedBooking?.caregiverId ?: return@mapNotNull null
                val clientName = threadMsgs.firstNotNullOfOrNull { it.clientName.takeIf { n -> n.isNotBlank() } }
                    ?: relatedBooking?.clientName?.takeIf { it.isNotBlank() }
                    ?: "Cliente #$clientUserId"
                val initials = clientName
                    .trim()
                    .split(" ")
                    .filter { it.isNotBlank() }
                    .take(2)
                    .joinToString("") { it.take(1).uppercase() }
                    .ifBlank { "CL" }

                ClientChatThread(
                    threadKey = key,
                    clientUserId = clientUserId,
                    caregiverId = caregiverId,
                    clientName = clientName,
                    clientInitials = initials,
                    lastMessageText = when {
                        lastMsg != null && !lastMsg.imagePath.isNullOrBlank() && lastMsg.text.isBlank() ->
                            "Foto enviada no chat"
                        lastMsg != null && !lastMsg.imagePath.isNullOrBlank() ->
                            "Foto • ${lastMsg.text}"
                        lastMsg != null -> lastMsg.text
                        relatedBooking != null ->
                            "${relatedBooking.serviceName} (${relatedBooking.petName}) • Toque para conversar"
                        else -> "Toque para iniciar conversa"
                    },
                    lastMessageHasPhoto = lastMsg?.imagePath != null,
                    lastMessageTime = lastMsg?.timeLabel ?: relatedBooking?.timeLabel ?: "",
                    lastMessageCreatedAt = lastMsg?.createdAt ?: relatedBooking?.createdAt ?: 0L,
                    relatedBookingStatus = relatedBooking?.status,
                    relatedPetName = relatedBooking?.petName
                )
            }.sortedByDescending { it.lastMessageCreatedAt }
        }
}

private data class FilterState(
    val searchQuery: String = "",
    val selectedServiceFilter: String? = null,
    val selectedDateIso: String = "2026-11-24",
    val showAllDates: Boolean = true,
    val selectedStatusFilter: BookingStatus? = null,
    val snackbarMessage: String? = null,
    val authError: String? = null,
    val isAuthLoading: Boolean = false,
    val isDetectingLocation: Boolean = false
)

private sealed interface SessionSnapshot {
    data object Loading : SessionSnapshot
    data class Resolved(val userId: Int?) : SessionSnapshot
}

private data class UserDataBundle(
    val userId: Int?,
    val pets: List<PetEntity>,
    val bookings: List<BookingEntity>,
    val caregivers: List<Caregiver>,
    val messages: List<ChatMessageEntity>,
    val certificates: List<CaregiverCertificateEntity>,
    val notifications: List<AppNotificationEntity>,
    val profile: TutorProfileEntity?
)

@OptIn(ExperimentalCoroutinesApi::class)
class PetCareViewModel(
    private val repository: PetCareRepository
) : ViewModel() {

    private val filterState = MutableStateFlow(FilterState())

    private val _systemNotificationEvents = Channel<SystemNotificationEvent>(Channel.BUFFERED)
    val systemNotificationEvents: Flow<SystemNotificationEvent> = _systemNotificationEvents.receiveAsFlow()

    private val sessionFlow: Flow<SessionSnapshot> = repository.activeUserId
        .map<Int?, SessionSnapshot> { SessionSnapshot.Resolved(it) }
        .onStart { emit(SessionSnapshot.Loading) }

    private val userDataFlow: Flow<Pair<SessionSnapshot, UserDataBundle>> = sessionFlow.flatMapLatest { session ->
        when (session) {
            is SessionSnapshot.Loading -> flowOf(
                session to UserDataBundle(
                    userId = null,
                    pets = emptyList(),
                    bookings = emptyList(),
                    caregivers = repository.caregivers,
                    messages = emptyList(),
                    certificates = emptyList(),
                    notifications = emptyList(),
                    profile = null
                )
            )
            is SessionSnapshot.Resolved -> {
                val uid = session.userId
                if (uid == null) {
                    flowOf(
                        session to UserDataBundle(
                            userId = null,
                            pets = emptyList(),
                            bookings = emptyList(),
                            caregivers = repository.caregivers,
                            messages = emptyList(),
                            certificates = emptyList(),
                            notifications = emptyList(),
                            profile = null
                        )
                    )
                } else {
                    repository.observeProfile(uid).flatMapLatest { profile ->
                        if (profile?.isCaregiver == true) {
                            combine(
                                repository.observeBookingsForCaregiver(uid),
                                repository.observeAllCaregivers(),
                                repository.observeMessagesForCaregiverAccount(uid),
                                repository.observeCertificates(uid),
                                repository.observeNotifications(uid)
                            ) { bookings, caregivers, messages, certs, notifications ->
                                session to UserDataBundle(
                                    userId = uid,
                                    pets = emptyList(),
                                    bookings = bookings,
                                    caregivers = caregivers,
                                    messages = messages,
                                    certificates = certs,
                                    notifications = notifications,
                                    profile = profile
                                )
                            }
                        } else {
                            combine(
                                repository.observePets(uid),
                                repository.observeBookings(uid),
                                repository.observeAllCaregivers(),
                                repository.observeAllMessages(uid),
                                repository.observeNotifications(uid)
                            ) { pets, bookings, caregivers, messages, notifications ->
                                session to UserDataBundle(
                                    userId = uid,
                                    pets = pets,
                                    bookings = bookings,
                                    caregivers = caregivers,
                                    messages = messages,
                                    certificates = emptyList(),
                                    notifications = notifications,
                                    profile = profile
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    val uiState: StateFlow<PetCareUiState> = combine(userDataFlow, filterState) { (session, data), filters ->
        val resolved = session is SessionSnapshot.Resolved
        PetCareUiState(
            isSessionResolved = resolved,
            activeUserId = data.userId,
            pets = data.pets,
            bookings = data.bookings,
            caregivers = data.caregivers,
            services = repository.services,
            allMessages = data.messages,
            certificates = data.certificates,
            notifications = data.notifications,
            profile = data.profile,
            searchQuery = filters.searchQuery,
            selectedServiceFilter = filters.selectedServiceFilter,
            selectedDateIso = filters.selectedDateIso,
            showAllDates = filters.showAllDates,
            selectedStatusFilter = filters.selectedStatusFilter,
            snackbarMessage = filters.snackbarMessage,
            authError = filters.authError,
            isAuthLoading = filters.isAuthLoading,
            isDetectingLocation = filters.isDetectingLocation,
            isReady = resolved
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = PetCareUiState()
    )

    fun observeMessagesForCaregiver(caregiverId: String): Flow<List<ChatMessageEntity>> {
        val uid = uiState.value.activeUserId ?: return flowOf(emptyList())
        return repository.observeMessagesForCaregiver(uid, caregiverId)
    }

    fun observeMessagesForClientThread(clientUserId: Int, caregiverId: String): Flow<List<ChatMessageEntity>> {
        val caregiverUid = uiState.value.activeUserId ?: return flowOf(emptyList())
        return repository.observeThreadMessagesForCaregiverAccount(caregiverUid, clientUserId, caregiverId)
    }

    fun getCaregiverById(caregiverId: String): Caregiver? =
        uiState.value.caregivers.firstOrNull { it.id == caregiverId }
            ?: repository.caregivers.firstOrNull { it.id == caregiverId }

    fun getPetById(petId: Int): PetEntity? =
        uiState.value.pets.firstOrNull { it.id == petId }

    fun clearAuthError() {
        filterState.update { it.copy(authError = null) }
    }

    fun login(email: String, password: String, isCaregiver: Boolean = false) {
        viewModelScope.launch {
            filterState.update { it.copy(isAuthLoading = true, authError = null) }
            val result = repository.loginAccount(
                email = email,
                password = password,
                expectedCaregiver = isCaregiver
            )
            result.fold(
                onSuccess = {
                    filterState.update {
                        it.copy(
                            isAuthLoading = false,
                            authError = null,
                            snackbarMessage = "Login realizado com sucesso!"
                        )
                    }
                },
                onFailure = { error ->
                    filterState.update {
                        it.copy(
                            isAuthLoading = false,
                            authError = error.message ?: "Não foi possível entrar."
                        )
                    }
                }
            )
        }
    }

    fun register(
        name: String,
        email: String,
        password: String,
        confirmPassword: String,
        phone: String,
        address: String,
        city: String,
        neighborhood: String,
        bio: String,
        avatarPath: String?,
        isCaregiver: Boolean = false,
        specialty: String = "",
        experienceYears: String = ""
    ) {
        if (!isCaregiver && password != confirmPassword) {
            filterState.update { it.copy(authError = "As senhas precisam ser iguais.") }
            return
        }
        viewModelScope.launch {
            filterState.update { it.copy(isAuthLoading = true, authError = null) }
            val result = repository.registerAccount(
                name = name,
                email = email,
                password = password,
                phone = phone,
                address = address,
                city = city,
                neighborhood = neighborhood,
                bio = bio,
                avatarPath = avatarPath,
                isCaregiver = isCaregiver,
                specialty = specialty,
                experienceYears = experienceYears
            )
            result.fold(
                onSuccess = { newUserId ->
                    filterState.update {
                        it.copy(
                            isAuthLoading = false,
                            authError = null,
                            snackbarMessage = "Conta criada com sucesso! Bem-vindo ao FuturusCare."
                        )
                    }
                    _systemNotificationEvents.trySend(
                        SystemNotificationEvent(
                            id = (newUserId * 100) + 1,
                            title = if (isCaregiver) "Painel de Cuidador ativado!" else "Bem-vindo ao FuturusCare!",
                            body = if (isCaregiver) {
                                "Adicione foto, destaque seu perfil e anexe um certificado para liberar o selo Verificado."
                            } else {
                                "Sua conta foi criada. Cadastre um pet e complete seu perfil para liberar o selo Verificado."
                            },
                            isBookingAlert = false
                        )
                    )
                },
                onFailure = { error ->
                    filterState.update {
                        it.copy(
                            isAuthLoading = false,
                            authError = error.message ?: "Erro ao cadastrar conta."
                        )
                    }
                }
            )
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
            filterState.update {
                FilterState(snackbarMessage = "Você saiu da sua conta.")
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        filterState.update { it.copy(searchQuery = query) }
    }

    fun onServiceFilterSelected(serviceTitle: String?) {
        filterState.update { current ->
            val next = if (current.selectedServiceFilter == serviceTitle) null else serviceTitle
            current.copy(selectedServiceFilter = next)
        }
    }

    fun clearHomeFilters() {
        filterState.update { it.copy(searchQuery = "", selectedServiceFilter = null) }
    }

    fun onSelectCalendarDay(day: CalendarDayOption) {
        filterState.update {
            it.copy(
                selectedDateIso = day.dateIso,
                showAllDates = false
            )
        }
    }

    fun onShowAllDates() {
        filterState.update {
            it.copy(
                showAllDates = true,
                selectedStatusFilter = null
            )
        }
    }

    fun onSelectStatusFilter(status: BookingStatus?) {
        filterState.update { current ->
            val next = if (current.selectedStatusFilter == status) null else status
            current.copy(selectedStatusFilter = next)
        }
    }

    fun refreshAgenda() {
        filterState.update { it.copy(snackbarMessage = "Agenda atualizada") }
    }

    fun savePet(
        existingId: Int?,
        name: String,
        species: String,
        breed: String,
        ageYears: String,
        weightKg: String,
        specialNotes: String,
        photoPath: String?
    ) {
        val uid = uiState.value.activeUserId ?: return
        val cleanName = name.trim()
        if (cleanName.isEmpty()) return

        viewModelScope.launch {
            if (existingId != null && existingId > 0) {
                val old = uiState.value.pets.firstOrNull { it.id == existingId }
                repository.updatePet(
                    PetEntity(
                        id = existingId,
                        userId = uid,
                        name = cleanName,
                        species = species.trim().ifEmpty { "Cão" },
                        breed = breed.trim().ifEmpty { "SRD" },
                        ageYears = ageYears.trim().ifEmpty { "1" },
                        weightKg = weightKg.trim().ifEmpty { "5.0" },
                        specialNotes = specialNotes.trim(),
                        photoPath = photoPath ?: old?.photoPath,
                        createdAt = old?.createdAt ?: System.currentTimeMillis()
                    )
                )
                filterState.update { it.copy(snackbarMessage = "$cleanName atualizado com sucesso!") }
            } else {
                val petId = repository.addPet(
                    PetEntity(
                        userId = uid,
                        name = cleanName,
                        species = species.trim().ifEmpty { "Cão" },
                        breed = breed.trim().ifEmpty { "SRD" },
                        ageYears = ageYears.trim().ifEmpty { "1" },
                        weightKg = weightKg.trim().ifEmpty { "5.0" },
                        specialNotes = specialNotes.trim(),
                        photoPath = photoPath
                    )
                ).toInt()
                dispatchNotification(
                    userId = uid,
                    notificationId = 2000 + petId,
                    title = "Novo pet cadastrado: $cleanName",
                    body = "$cleanName já está pronto para receber cuidados e agendamentos no FuturusCare.",
                    type = NotificationType.PET_ADDED,
                    relatedRoute = "pets",
                    isBookingAlert = false
                )
                filterState.update { it.copy(snackbarMessage = "$cleanName cadastrado!") }
            }
        }
    }

    fun deletePet(pet: PetEntity) {
        viewModelScope.launch {
            repository.deletePet(pet.id)
            filterState.update { it.copy(snackbarMessage = "${pet.name} removido da lista") }
        }
    }

    fun createBooking(
        caregiver: Caregiver,
        pet: PetEntity,
        serviceName: String,
        dayOption: CalendarDayOption,
        timeLabel: String,
        notes: String
    ) {
        val uid = uiState.value.activeUserId ?: return
        val clientProfile = uiState.value.profile
        val localCaregiverUid = caregiver.id
            .takeIf { it.startsWith("local_user_") }
            ?.removePrefix("local_user_")
            ?.toIntOrNull()

        viewModelScope.launch {
            val service = repository.services.firstOrNull { it.title == serviceName }
            val initialStatus = if (localCaregiverUid != null) BookingStatus.PENDENTE else BookingStatus.AGENDADO
            val bookingId = repository.createBooking(
                BookingEntity(
                    userId = uid,
                    caregiverId = caregiver.id,
                    caregiverUserId = localCaregiverUid,
                    caregiverName = caregiver.name,
                    caregiverInitials = caregiver.initials,
                    clientName = clientProfile?.name.orEmpty(),
                    clientPhone = clientProfile?.phone.orEmpty(),
                    petId = pet.id,
                    petName = pet.name,
                    petSpecies = pet.species,
                    serviceName = serviceName,
                    dateIso = dayOption.dateIso,
                    dateLabel = dayOption.dateLabel,
                    timeLabel = timeLabel,
                    status = initialStatus,
                    priceReais = service?.basePrice ?: caregiver.pricePerSession,
                    notes = notes.trim()
                )
            ).toInt()

            dispatchNotification(
                userId = uid,
                notificationId = 3000 + bookingId,
                title = if (localCaregiverUid != null) "Solicitação enviada!" else "Agendamento confirmado!",
                body = "$serviceName para ${pet.name} com ${caregiver.name} em ${dayOption.dateLabel} às $timeLabel.",
                type = NotificationType.BOOKING_CREATED,
                relatedRoute = "agenda",
                isBookingAlert = true
            )

            if (localCaregiverUid != null && localCaregiverUid != uid) {
                repository.addNotification(
                    userId = localCaregiverUid,
                    title = "Novo cliente contratou você!",
                    body = "${clientProfile?.name ?: "Um tutor"} solicitou $serviceName para ${pet.name} em ${dayOption.dateLabel} às $timeLabel.",
                    type = NotificationType.BOOKING_CREATED,
                    relatedRoute = "clients"
                )
            }

            filterState.update {
                it.copy(
                    selectedDateIso = dayOption.dateIso,
                    showAllDates = true,
                    snackbarMessage = "Agendamento com ${caregiver.name} criado!"
                )
            }
        }
    }

    fun updateBookingStatus(bookingId: Int, status: BookingStatus) {
        val uid = uiState.value.activeUserId ?: return
        val booking = uiState.value.bookings.firstOrNull { it.id == bookingId }
        val isCaregiver = uiState.value.isCaregiverMode
        viewModelScope.launch {
            repository.updateBookingStatus(bookingId, status)
            val title = if (isCaregiver) {
                when (status) {
                    BookingStatus.AGENDADO -> "Contratação aceita!"
                    BookingStatus.EM_ANDAMENTO -> "Atendimento iniciado"
                    BookingStatus.CONCLUIDO -> "Atendimento concluído!"
                    else -> "Status do atendimento atualizado"
                }
            } else {
                "Status do agendamento atualizado"
            }

            val body = if (booking != null) {
                if (isCaregiver) {
                    "${booking.serviceName} para ${booking.petName} (Tutor: ${booking.clientName.ifBlank { "Cliente" }}) agora está: ${status.label}."
                } else {
                    "${booking.serviceName} (${booking.petName}) com ${booking.caregiverName} agora está: ${status.label}."
                }
            } else {
                "Um agendamento foi atualizado para ${status.label}."
            }

            dispatchNotification(
                userId = uid,
                notificationId = 4000 + bookingId,
                title = title,
                body = body,
                type = NotificationType.BOOKING_STATUS,
                relatedRoute = "agenda",
                isBookingAlert = true
            )

            if (isCaregiver && booking != null && booking.userId > 0 && booking.userId != uid) {
                repository.addNotification(
                    userId = booking.userId,
                    title = "${booking.caregiverName}: ${status.label}",
                    body = "Seu agendamento de ${booking.serviceName} (${booking.petName}) em ${booking.dateLabel} foi atualizado para ${status.label}.",
                    type = NotificationType.BOOKING_STATUS,
                    relatedRoute = "agenda"
                )
            }

            filterState.update { it.copy(snackbarMessage = "Status alterado para ${status.label}") }
        }
    }

    fun cancelBookingWithReason(bookingId: Int, rawReason: String) {
        val reason = rawReason.trim()
        if (reason.length < 4) {
            filterState.update { it.copy(snackbarMessage = "Explique o motivo para desmarcar.") }
            return
        }
        val uid = uiState.value.activeUserId ?: return
        val booking = uiState.value.bookings.firstOrNull { it.id == bookingId }
        val isCaregiver = uiState.value.isCaregiverMode

        viewModelScope.launch {
            repository.cancelBookingWithReason(
                bookingId = bookingId,
                reason = reason,
                cancelledByCaregiver = isCaregiver
            )

            val title = if (isCaregiver) "Atendimento desmarcado" else "Agendamento cancelado"
            val body = if (booking != null) {
                "${booking.serviceName} (${booking.petName}) em ${booking.dateLabel} às ${booking.timeLabel} foi desmarcado. Motivo: $reason"
            } else {
                "Agendamento desmarcado. Motivo: $reason"
            }

            dispatchNotification(
                userId = uid,
                notificationId = 4500 + bookingId,
                title = title,
                body = body,
                type = NotificationType.BOOKING_STATUS,
                relatedRoute = "agenda",
                isBookingAlert = true
            )

            if (isCaregiver && booking != null && booking.userId > 0 && booking.userId != uid) {
                repository.addNotification(
                    userId = booking.userId,
                    title = "${booking.caregiverName} desmarcou o atendimento",
                    body = "${booking.serviceName} (${booking.petName}) em ${booking.dateLabel}: $reason",
                    type = NotificationType.BOOKING_STATUS,
                    relatedRoute = "agenda"
                )
            }

            filterState.update { it.copy(snackbarMessage = "Agendamento desmarcado com motivo registrado.") }
        }
    }

    fun deleteBooking(bookingId: Int) {
        viewModelScope.launch {
            repository.deleteBooking(bookingId)
            filterState.update { it.copy(snackbarMessage = "Agendamento removido") }
        }
    }

    /**
     * Envia mensagem de texto e/ou foto do Cliente para o Cuidador.
     */
    fun sendMessage(caregiverId: String, rawText: String, imagePath: String? = null) {
        val uid = uiState.value.activeUserId ?: return
        val text = rawText.trim()
        if (text.isEmpty() && imagePath.isNullOrBlank()) return

        val clientProfile = uiState.value.profile
        val localCaregiverUid = caregiverId
            .takeIf { it.startsWith("local_user_") }
            ?.removePrefix("local_user_")
            ?.toIntOrNull()
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val currentTime = timeFormat.format(Date())

        viewModelScope.launch {
            repository.sendMessage(
                ChatMessageEntity(
                    userId = uid,
                    caregiverId = caregiverId,
                    caregiverUserId = localCaregiverUid,
                    clientName = clientProfile?.name.orEmpty(),
                    senderIsUser = true,
                    senderRole = "CLIENT",
                    text = text,
                    imagePath = imagePath,
                    timeLabel = currentTime
                )
            )

            if (localCaregiverUid != null && localCaregiverUid != uid) {
                repository.addNotification(
                    userId = localCaregiverUid,
                    title = "Nova mensagem de ${clientProfile?.name ?: "Cliente"}",
                    body = text.ifBlank { "Enviou uma foto no chat para você." },
                    type = NotificationType.CHAT_MESSAGE,
                    relatedRoute = "chat"
                )
                return@launch
            }

            delay(750L)
            val caregiver = getCaregiverById(caregiverId)
            val reply = when {
                !imagePath.isNullOrBlank() && text.isBlank() ->
                    "Recebi a foto! Obrigado por mostrar, isso ajuda bastante para cuidar do seu pet com segurança."
                !imagePath.isNullOrBlank() ->
                    "Obrigado pela foto e pelas informações! Já anotei tudo aqui para o atendimento."
                text.contains("horário", ignoreCase = true) || text.contains("hora", ignoreCase = true) ->
                    "Tenho disponibilidade pela manhã e no fim da tarde! Qual horário fica melhor para o seu pet?"
                text.contains("preço", ignoreCase = true) || text.contains("valor", ignoreCase = true) ->
                    "Meu valor base é R$ ${caregiver?.pricePerSession?.toInt() ?: 45}${caregiver?.priceUnit ?: "/h"}, já incluindo fotos e relatório do cuidado."
                text.contains("oi", ignoreCase = true) || text.contains("olá", ignoreCase = true) ->
                    "Olá! Que alegria falar com você. Como posso ajudar com o seu pet hoje?"
                else ->
                    "Combinado! Fico à disposição para cuidar com todo carinho. Se quiser, já pode confirmar pela aba Agenda."
            }
            repository.sendMessage(
                ChatMessageEntity(
                    userId = uid,
                    caregiverId = caregiverId,
                    caregiverUserId = null,
                    clientName = clientProfile?.name.orEmpty(),
                    senderIsUser = false,
                    senderRole = "CAREGIVER",
                    text = reply,
                    imagePath = null,
                    timeLabel = timeFormat.format(Date())
                )
            )
            if (caregiver != null) {
                dispatchNotification(
                    userId = uid,
                    notificationId = 5000 + caregiver.id.hashCode(),
                    title = "Nova mensagem de ${caregiver.name}",
                    body = reply,
                    type = NotificationType.CHAT_MESSAGE,
                    relatedRoute = "chat_detail/${caregiver.id}",
                    isBookingAlert = false
                )
            }
        }
    }

    /**
     * Envia mensagem de texto e/ou foto do Cuidador para o Cliente.
     */
    fun sendCaregiverMessage(
        clientUserId: Int,
        caregiverId: String,
        clientName: String,
        rawText: String,
        imagePath: String? = null
    ) {
        val caregiverUid = uiState.value.activeUserId ?: return
        val text = rawText.trim()
        if (text.isEmpty() && imagePath.isNullOrBlank()) return

        val caregiverProfile = uiState.value.profile
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val currentTime = timeFormat.format(Date())

        viewModelScope.launch {
            repository.sendMessage(
                ChatMessageEntity(
                    userId = clientUserId,
                    caregiverId = caregiverId,
                    caregiverUserId = caregiverUid,
                    clientName = clientName,
                    senderIsUser = true,
                    senderRole = "CAREGIVER",
                    text = text,
                    imagePath = imagePath,
                    timeLabel = currentTime
                )
            )

            if (clientUserId > 0 && clientUserId != caregiverUid) {
                repository.addNotification(
                    userId = clientUserId,
                    title = "Nova mensagem de ${caregiverProfile?.name ?: "Cuidador"}",
                    body = text.ifBlank { "Enviou uma foto no chat." },
                    type = NotificationType.CHAT_MESSAGE,
                    relatedRoute = "chat_detail/$caregiverId"
                )
            } else if (caregiverId.startsWith("sim_client_")) {
                delay(850L)
                val reply = if (!imagePath.isNullOrBlank()) {
                    "Adorei a foto! Que tranquilidade saber que está tudo bem por aí."
                } else {
                    "Perfeito, obrigado pelo retorno rápido! Qualquer dúvida pode mandar foto ou mensagem por aqui."
                }
                repository.sendMessage(
                    ChatMessageEntity(
                        userId = clientUserId,
                        caregiverId = caregiverId,
                        caregiverUserId = caregiverUid,
                        clientName = clientName,
                        senderIsUser = false,
                        senderRole = "CLIENT",
                        text = reply,
                        imagePath = null,
                        timeLabel = timeFormat.format(Date())
                    )
                )
                dispatchNotification(
                    userId = caregiverUid,
                    notificationId = 5500 + caregiverId.hashCode(),
                    title = "Mensagem de $clientName",
                    body = reply,
                    type = NotificationType.CHAT_MESSAGE,
                    relatedRoute = "chat",
                    isBookingAlert = false
                )
            }
        }
    }

    /**
     * Cria uma solicitação simulada de cliente para o cuidador testar aceitar, desmarcar com motivo, chat e notificações.
     */
    fun simulateClientHireRequest() {
        val uid = uiState.value.activeUserId ?: return
        val profile = uiState.value.profile ?: return
        if (!profile.isCaregiver) return

        val sampleClients = listOf(
            Triple("Mariana Costa", "(11) 98741-2210", "Pipoca" to "Cão • Shih-tzu"),
            Triple("Lucas Pereira", "(11) 99124-4088", "Bento" to "Cão • Bulldog Francês"),
            Triple("Fernanda Rocha", "(11) 97655-1834", "Mia" to "Gato • Siamês")
        )
        val index = uiState.value.bookings.size % sampleClients.size
        val (clientName, clientPhone, petPair) = sampleClients[index]
        val day = CaregiverMockData.novemberWeekDays[
            (3 + uiState.value.bookings.size) % CaregiverMockData.novemberWeekDays.size
        ]
        val times = listOf("09:00", "11:00", "14:30", "17:00")
        val timeLabel = times[uiState.value.bookings.size % times.size]
        val serviceName = profile.specialtiesList.firstOrNull() ?: "Dog Walking"
        val price = profile.pricePerSession.takeIf { it > 0.0 } ?: 55.0
        val caregiverThreadId = "sim_client_${System.currentTimeMillis()}"
        val initials = profile.name
            .trim()
            .split(" ")
            .filter { it.isNotBlank() }
            .take(2)
            .joinToString("") { it.take(1).uppercase() }
            .ifBlank { "CU" }
        val currentTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())

        viewModelScope.launch {
            val bookingId = repository.createBooking(
                BookingEntity(
                    userId = 0,
                    caregiverId = caregiverThreadId,
                    caregiverUserId = uid,
                    caregiverName = profile.name,
                    caregiverInitials = initials,
                    clientName = clientName,
                    clientPhone = clientPhone,
                    petId = 0,
                    petName = petPair.first,
                    petSpecies = petPair.second,
                    serviceName = serviceName,
                    dateIso = day.dateIso,
                    dateLabel = day.dateLabel,
                    timeLabel = timeLabel,
                    status = BookingStatus.PENDENTE,
                    priceReais = price,
                    notes = "Olá ${profile.name.substringBefore(" ")}, gostaria de contratar $serviceName para ${petPair.first} (${petPair.second})."
                )
            ).toInt()

            repository.sendMessage(
                ChatMessageEntity(
                    userId = 0,
                    caregiverId = caregiverThreadId,
                    caregiverUserId = uid,
                    clientName = clientName,
                    senderIsUser = false,
                    senderRole = "CLIENT",
                    text = "Oi! Acabei de enviar uma solicitação de $serviceName para ${petPair.first} no dia ${day.dateLabel} às $timeLabel. Você tem disponibilidade?",
                    imagePath = null,
                    timeLabel = currentTime
                )
            )

            dispatchNotification(
                userId = uid,
                notificationId = 8000 + bookingId,
                title = "Nova contratação recebida!",
                body = "$clientName quer agendar $serviceName para ${petPair.first} em ${day.dateLabel} às $timeLabel.",
                type = NotificationType.BOOKING_CREATED,
                relatedRoute = "clients",
                isBookingAlert = true
            )

            filterState.update {
                it.copy(snackbarMessage = "Nova solicitação de $clientName recebida!")
            }
        }
    }

    fun updateProfileInfo(
        name: String,
        email: String,
        phone: String,
        address: String,
        city: String,
        neighborhood: String,
        bio: String
    ) {
        val current = uiState.value.profile ?: return
        viewModelScope.launch {
            val updated = current.copy(
                name = name.trim(),
                email = email.trim(),
                phone = phone.trim(),
                address = address.trim(),
                city = city.trim(),
                neighborhood = neighborhood.trim(),
                bio = bio.trim()
            )
            persistProfileWithVerificationCheck(current, updated)
            filterState.update { it.copy(snackbarMessage = "Dados do perfil atualizados!") }
        }
    }

    fun updateCaregiverShowcase(
        headline: String,
        specialties: List<String>,
        pricePerSession: Double,
        priceUnit: String,
        experienceYears: Int,
        highlights: List<String>,
        bio: String,
        isAvailableForClients: Boolean
    ) {
        val current = uiState.value.profile ?: return
        val cleanSpecs = specialties.map { it.trim() }.filter { it.isNotBlank() }
        val primarySpec = cleanSpecs.firstOrNull() ?: current.specialty.ifBlank { "Dog Walking" }
        viewModelScope.launch {
            val updated = current.copy(
                headline = headline.trim(),
                specialty = primarySpec,
                specialtiesCsv = cleanSpecs.ifEmpty { listOf(primarySpec) }.joinToString(","),
                pricePerSession = pricePerSession.coerceAtLeast(10.0),
                priceUnit = priceUnit.ifBlank { "/h" },
                experienceYears = experienceYears.coerceAtLeast(0),
                highlightsCsv = highlights.joinToString(","),
                bio = bio.trim(),
                isAvailableForClients = isAvailableForClients
            )
            persistProfileWithVerificationCheck(current, updated)
            filterState.update { it.copy(snackbarMessage = "Perfil profissional atualizado!") }
        }
    }

    fun addCaregiverCertificate(
        title: String,
        issuer: String,
        filePath: String,
        fileName: String,
        isImage: Boolean
    ) {
        val current = uiState.value.profile ?: return
        val cleanTitle = title.trim().ifBlank { "Certificado Profissional" }
        viewModelScope.launch {
            val newCount = repository.addCertificate(
                CaregiverCertificateEntity(
                    userId = current.userId,
                    title = cleanTitle,
                    issuer = issuer.trim(),
                    filePath = filePath,
                    fileName = fileName,
                    isImage = isImage
                )
            )
            val updated = current.copy(certificateCount = newCount)
            persistProfileWithVerificationCheck(current, updated)
            dispatchNotification(
                userId = current.userId,
                notificationId = 7100 + newCount,
                title = "Certificado anexado",
                body = "\"$cleanTitle\" foi adicionado ao seu perfil de cuidador.",
                type = NotificationType.CERTIFICATE_ADDED,
                relatedRoute = "profile",
                isBookingAlert = false
            )
            filterState.update { it.copy(snackbarMessage = "Certificado anexado com sucesso!") }
        }
    }

    fun removeCaregiverCertificate(certificate: CaregiverCertificateEntity) {
        val current = uiState.value.profile ?: return
        viewModelScope.launch {
            val newCount = repository.deleteCertificate(certificate.id, current.userId)
            val updated = current.copy(certificateCount = newCount)
            persistProfileWithVerificationCheck(current, updated)
            filterState.update { it.copy(snackbarMessage = "Certificado removido.") }
        }
    }

    fun updateProfileAvatar(path: String) {
        val current = uiState.value.profile ?: return
        viewModelScope.launch {
            val updated = current.copy(avatarPath = path)
            persistProfileWithVerificationCheck(current, updated)
            filterState.update { it.copy(snackbarMessage = "Foto de perfil atualizada!") }
        }
    }

    fun updateProfileBanner(path: String) {
        val current = uiState.value.profile ?: return
        viewModelScope.launch {
            repository.updateProfile(current.copy(bannerPath = path))
            filterState.update { it.copy(snackbarMessage = "Banner atualizado!") }
        }
    }

    fun setDetectingLocation(detecting: Boolean) {
        filterState.update { it.copy(isDetectingLocation = detecting) }
    }

    fun applyDetectedLocation(location: DetectedLocation) {
        val current = uiState.value.profile ?: return
        viewModelScope.launch {
            val updated = current.copy(
                address = location.address.ifBlank { current.address },
                neighborhood = location.neighborhood.ifBlank { current.neighborhood },
                city = location.city.ifBlank { current.city }
            )
            persistProfileWithVerificationCheck(current, updated)
            dispatchNotification(
                userId = current.userId,
                notificationId = 6001,
                title = "Localização atualizada",
                body = "${updated.neighborhood}, ${updated.city} definida automaticamente pelo dispositivo.",
                type = NotificationType.LOCATION_UPDATED,
                relatedRoute = "profile",
                isBookingAlert = false
            )
            filterState.update {
                it.copy(
                    isDetectingLocation = false,
                    snackbarMessage = "Localização detectada: ${updated.formattedRegion}"
                )
            }
        }
    }

    private suspend fun persistProfileWithVerificationCheck(
        previous: TutorProfileEntity,
        candidate: TutorProfileEntity
    ) {
        val unlockedNow = candidate.isVerified && !previous.verifiedNotified
        val finalProfile = candidate.copy(
            verifiedNotified = candidate.isVerified
        )
        repository.updateProfile(finalProfile)

        if (unlockedNow) {
            val badgeTitle = if (finalProfile.isCaregiver) {
                "Selo Cuidador Verificado liberado!"
            } else {
                "Selo Tutor Verificado liberado!"
            }
            val badgeBody = if (finalProfile.isCaregiver) {
                "Foto, dados profissionais e certificado validados! Seu perfil agora exibe o selo oficial para atrair clientes."
            } else {
                "Sua foto e todas as informações foram preenchidas. Agora seu perfil exibe o selo oficial de verificação."
            }
            dispatchNotification(
                userId = finalProfile.userId,
                notificationId = 7001,
                title = badgeTitle,
                body = badgeBody,
                type = NotificationType.PROFILE_VERIFIED,
                relatedRoute = "profile",
                isBookingAlert = false
            )
        }
    }

    fun toggleNotifications(enabled: Boolean) {
        val current = uiState.value.profile ?: return
        viewModelScope.launch {
            repository.updateProfile(current.copy(notificationsEnabled = enabled))
            filterState.update {
                it.copy(
                    snackbarMessage = if (enabled) "Notificações ativadas" else "Notificações silenciadas"
                )
            }
        }
    }

    fun markNotificationAsRead(notificationId: Int) {
        viewModelScope.launch {
            repository.markNotificationAsRead(notificationId)
        }
    }

    fun markAllNotificationsAsRead() {
        val uid = uiState.value.activeUserId ?: return
        viewModelScope.launch {
            repository.markAllNotificationsAsRead(uid)
        }
    }

    fun clearReadNotifications() {
        val uid = uiState.value.activeUserId ?: return
        viewModelScope.launch {
            repository.clearReadNotifications(uid)
            filterState.update { it.copy(snackbarMessage = "Notificações lidas removidas") }
        }
    }

    private suspend fun dispatchNotification(
        userId: Int,
        notificationId: Int,
        title: String,
        body: String,
        type: NotificationType,
        relatedRoute: String?,
        isBookingAlert: Boolean
    ) {
        repository.addNotification(
            userId = userId,
            title = title,
            body = body,
            type = type,
            relatedRoute = relatedRoute
        )
        val notificationsEnabled = uiState.value.profile?.notificationsEnabled ?: true
        if (notificationsEnabled) {
            _systemNotificationEvents.trySend(
                SystemNotificationEvent(
                    id = notificationId,
                    title = title,
                    body = body,
                    isBookingAlert = isBookingAlert
                )
            )
        }
    }

    fun showMessage(message: String) {
        filterState.update { it.copy(snackbarMessage = message) }
    }

    fun consumeSnackbar() {
        filterState.update { it.copy(snackbarMessage = null) }
    }

    class Factory(private val repository: PetCareRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PetCareViewModel::class.java)) {
                return PetCareViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
