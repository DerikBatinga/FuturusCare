package com.example.ui.screens

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.ChatBubbleOutline
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.EventBusy
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Payments
import androidx.compose.material.icons.rounded.PersonAdd
import androidx.compose.material.icons.rounded.Pets
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material.icons.rounded.Verified
import androidx.compose.material.icons.rounded.WorkspacePremium
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.model.BookingEntity
import com.example.data.model.BookingStatus
import com.example.ui.PetCareUiState
import com.example.ui.theme.DangerBg
import com.example.ui.theme.DangerText
import com.example.ui.theme.StatusCompletedBg
import com.example.ui.theme.StatusCompletedText
import com.example.ui.theme.StatusPendingBg
import com.example.ui.theme.StatusPendingText
import com.example.util.DetectedLocation
import com.example.util.LocationHelper
import kotlinx.coroutines.launch

@Composable
fun CaregiverHomeScreen(
    uiState: PetCareUiState,
    onOpenNotifications: () -> Unit,
    onDetectLocationSuccess: (DetectedLocation) -> Unit,
    onLocationError: (String) -> Unit,
    onToggleAvailability: (Boolean) -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToClients: () -> Unit,
    onNavigateToAgenda: () -> Unit,
    onNavigateToChat: () -> Unit,
    onAcceptBooking: (BookingEntity) -> Unit,
    onUpdateBookingStatus: (BookingEntity, BookingStatus) -> Unit,
    onRequestCancelBooking: (BookingEntity) -> Unit,
    onOpenClientChat: (BookingEntity) -> Unit,
    onBookingClick: (BookingEntity) -> Unit,
    onSimulateClientRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val profile = uiState.profile

    val runAutoLocation: () -> Unit = {
        coroutineScope.launch {
            val result = LocationHelper.detectCurrentLocation(context)
            result.fold(
                onSuccess = onDetectLocationSuccess,
                onFailure = { error ->
                    onLocationError(error.message ?: "Não foi possível obter a localização agora.")
                }
            )
        }
    }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { grants ->
        if (grants.values.any { it }) {
            runAutoLocation()
        } else {
            onLocationError("Permissão de localização negada.")
        }
    }

    val activeOrPending = remember(uiState.bookings) {
        uiState.bookings.filter {
            it.status == BookingStatus.PENDENTE ||
                it.status == BookingStatus.AGENDADO ||
                it.status == BookingStatus.EM_ANDAMENTO
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("caregiver_home_list"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Caregiver Header + Location + Notifications
        item {
            val firstName = profile?.name?.substringBefore(" ")?.ifBlank { "Cuidador" } ?: "Cuidador"
            val regionLabel = profile?.let {
                when {
                    it.neighborhood.isNotBlank() -> it.neighborhood
                    it.city.isNotBlank() -> it.city
                    else -> "Detectar local"
                }
            } ?: "Detectar local"

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.22f)),
                        shadowElevation = 1.dp,
                        modifier = Modifier.size(42.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.logo_simbolo),
                            contentDescription = stringResource(R.string.cd_app_logo),
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(5.dp)
                        )
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Text(
                                text = "Olá, $firstName",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            if (profile?.isVerified == true) {
                                Icon(
                                    imageVector = Icons.Rounded.Verified,
                                    contentDescription = stringResource(R.string.cd_verified_badge),
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                        Text(
                            text = "Painel do Cuidador • PetSitter",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = MaterialTheme.colorScheme.surface,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.65f)),
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .clickable {
                                if (LocationHelper.hasLocationPermission(context)) {
                                    runAutoLocation()
                                } else {
                                    locationPermissionLauncher.launch(
                                        arrayOf(
                                            Manifest.permission.ACCESS_COARSE_LOCATION,
                                            Manifest.permission.ACCESS_FINE_LOCATION
                                        )
                                    )
                                }
                            }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.LocationOn,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = regionLabel,
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.surface,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.65f))
                    ) {
                        IconButton(
                            onClick = onOpenNotifications,
                            modifier = Modifier
                                .size(40.dp)
                                .testTag("caregiver_notifications_button")
                        ) {
                            if (uiState.unreadNotificationCount > 0) {
                                BadgedBox(
                                    badge = {
                                        Badge(
                                            containerColor = MaterialTheme.colorScheme.primary,
                                            contentColor = MaterialTheme.colorScheme.onPrimary
                                        ) {
                                            Text(
                                                text = if (uiState.unreadNotificationCount > 9) "9+"
                                                else uiState.unreadNotificationCount.toString()
                                            )
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Notifications,
                                        contentDescription = stringResource(R.string.cd_notifications),
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            } else {
                                Icon(
                                    imageVector = Icons.Rounded.Notifications,
                                    contentDescription = stringResource(R.string.cd_notifications),
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // 2. Verification & Certificates Callout (until Caregiver is verified)
        if (profile != null && !profile.isVerified) {
            item {
                Surface(
                    shape = RoundedCornerShape(22.dp),
                    color = StatusPendingBg.copy(alpha = 0.72f),
                    border = BorderStroke(1.dp, StatusPendingText.copy(alpha = 0.28f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(22.dp))
                        .clickable(onClick = onNavigateToProfile)
                        .testTag("caregiver_verify_banner")
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.WorkspacePremium,
                                contentDescription = null,
                                tint = StatusPendingText,
                                modifier = Modifier.size(22.dp)
                            )
                            Text(
                                text = "Ganhe o selo Cuidador Verificado (${profile.completionCount}/${profile.totalVerificationSteps})",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = StatusPendingText
                            )
                        }
                        Text(
                            text = "Adicione foto de perfil, complete sua vitrine profissional e anexe pelo menos 1 certificado para atrair mais clientes.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Button(
                            onClick = onNavigateToProfile,
                            shape = RoundedCornerShape(50),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Text("Anexar certificados e completar perfil")
                        }
                    }
                }
            }
        }

        // 3. Availability Toggle Card
        item {
            val isAvailable = profile?.isAvailableForClients ?: true
            Surface(
                shape = RoundedCornerShape(22.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.65f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(
                                if (isAvailable) StatusCompletedBg else StatusPendingBg
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.AutoAwesome,
                            contentDescription = null,
                            tint = if (isAvailable) StatusCompletedText else StatusPendingText,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (isAvailable) "Disponível para contratações" else "Agenda pausada",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = if (isAvailable) {
                                "Clientes podem encontrar seu perfil e contratar seus serviços"
                            } else {
                                "Seu perfil está oculto temporariamente para novos pedidos"
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Switch(
                        checked = isAvailable,
                        onCheckedChange = onToggleAvailability,
                        modifier = Modifier.testTag("caregiver_availability_switch")
                    )
                }
            }
        }

        // 4. Metrics 2x2 Grid
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    CaregiverKpiCard(
                        icon = Icons.Rounded.PersonAdd,
                        value = uiState.pendingBookingsCount.toString(),
                        label = "Solicitações",
                        accentColor = StatusPendingText,
                        onClick = onNavigateToClients,
                        modifier = Modifier.weight(1f)
                    )
                    CaregiverKpiCard(
                        icon = Icons.Rounded.CalendarMonth,
                        value = uiState.confirmedBookingsCount.toString(),
                        label = "Confirmados",
                        accentColor = MaterialTheme.colorScheme.primary,
                        onClick = onNavigateToAgenda,
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    CaregiverKpiCard(
                        icon = Icons.Rounded.CheckCircle,
                        value = uiState.completedBookingsCount.toString(),
                        label = "Concluídos",
                        accentColor = StatusCompletedText,
                        onClick = onNavigateToAgenda,
                        modifier = Modifier.weight(1f)
                    )
                    CaregiverKpiCard(
                        icon = Icons.Rounded.Payments,
                        value = "R$ ${uiState.estimatedCaregiverEarnings.toInt()}",
                        label = "Ganhos previstos",
                        accentColor = MaterialTheme.colorScheme.primary,
                        onClick = onNavigateToClients,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // 5. Quick Actions Strip
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onNavigateToProfile,
                    shape = RoundedCornerShape(50)
                ) {
                    Text("Melhorar meu perfil")
                }
                OutlinedButton(
                    onClick = onNavigateToChat,
                    shape = RoundedCornerShape(50)
                ) {
                    Text("Chat com clientes (${uiState.clientChatThreads.size})")
                }
                OutlinedButton(
                    onClick = onSimulateClientRequest,
                    shape = RoundedCornerShape(50),
                    modifier = Modifier.testTag("caregiver_home_simulate_button")
                ) {
                    Text("Simular pedido")
                }
            }
        }

        // 6. Incoming Client Requests / Active Contracts
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Quem contratou você",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                TextButton(onClick = onNavigateToClients) {
                    Text("Ver tudo")
                }
            }
        }

        if (activeOrPending.isEmpty()) {
            item {
                Surface(
                    shape = RoundedCornerShape(22.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.65f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(22.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Pets,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(34.dp)
                        )
                        Text(
                            text = "Nenhuma contratação ativa no momento",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Melhore seu perfil e anexe seus certificados para aparecer com selo Verificado, ou simule um pedido para testar o atendimento.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Button(
                            onClick = onSimulateClientRequest,
                            shape = RoundedCornerShape(50)
                        ) {
                            Text("Simular contratação de cliente")
                        }
                    }
                }
            }
        } else {
            items(activeOrPending, key = { "cg_home_${it.id}" }) { booking ->
                CaregiverClientRequestCard(
                    booking = booking,
                    onClick = { onBookingClick(booking) },
                    onAccept = { onAcceptBooking(booking) },
                    onStart = { onUpdateBookingStatus(booking, BookingStatus.EM_ANDAMENTO) },
                    onComplete = { onUpdateBookingStatus(booking, BookingStatus.CONCLUIDO) },
                    onCancelWithReason = { onRequestCancelBooking(booking) },
                    onOpenChat = { onOpenClientChat(booking) }
                )
            }
        }

        item { Spacer(modifier = Modifier.height(8.dp)) }
    }
}

@Composable
fun CaregiverClientsScreen(
    uiState: PetCareUiState,
    onAcceptBooking: (BookingEntity) -> Unit,
    onUpdateBookingStatus: (BookingEntity, BookingStatus) -> Unit,
    onRequestCancelBooking: (BookingEntity) -> Unit,
    onOpenClientChat: (BookingEntity) -> Unit,
    onBookingClick: (BookingEntity) -> Unit,
    onSimulateClientRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedStatus by remember { mutableStateOf<BookingStatus?>(null) }

    val filtered = remember(uiState.bookings, selectedStatus) {
        if (selectedStatus == null) uiState.bookings
        else uiState.bookings.filter { it.status == selectedStatus }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("caregiver_clients_list"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Clientes & Contratações",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Aceite pedidos, converse ou desmarque explicando o motivo",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                TextButton(
                    onClick = onSimulateClientRequest,
                    modifier = Modifier.testTag("clients_simulate_button")
                ) {
                    Text("Simular pedido")
                }
            }
        }

        item {
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedStatus == null,
                    onClick = { selectedStatus = null },
                    label = { Text("Todos (${uiState.bookings.size})") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        selectedLabelColor = MaterialTheme.colorScheme.primary
                    )
                )
                BookingStatus.entries.forEach { status ->
                    val count = uiState.bookings.count { it.status == status }
                    FilterChip(
                        selected = selectedStatus == status,
                        onClick = {
                            selectedStatus = if (selectedStatus == status) null else status
                        },
                        label = { Text("${status.label} ($count)") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = status.containerColor,
                            selectedLabelColor = status.contentColor
                        )
                    )
                }
            }
        }

        if (filtered.isEmpty()) {
            item {
                Surface(
                    shape = RoundedCornerShape(22.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.65f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.PersonAdd,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(34.dp)
                        )
                        Text(
                            text = "Nenhum cliente neste filtro",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Quando tutores contratarem você, todos os detalhes do pet e ações de agenda aparecerão aqui.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        OutlinedButton(
                            onClick = onSimulateClientRequest,
                            shape = RoundedCornerShape(50)
                        ) {
                            Text("Simular solicitação de cliente")
                        }
                    }
                }
            }
        } else {
            items(filtered, key = { "cg_client_${it.id}" }) { booking ->
                CaregiverClientRequestCard(
                    booking = booking,
                    onClick = { onBookingClick(booking) },
                    onAccept = { onAcceptBooking(booking) },
                    onStart = { onUpdateBookingStatus(booking, BookingStatus.EM_ANDAMENTO) },
                    onComplete = { onUpdateBookingStatus(booking, BookingStatus.CONCLUIDO) },
                    onCancelWithReason = { onRequestCancelBooking(booking) },
                    onOpenChat = { onOpenClientChat(booking) }
                )
            }
        }

        item { Spacer(modifier = Modifier.height(8.dp)) }
    }
}

@Composable
private fun CaregiverKpiCard(
    icon: ImageVector,
    value: String,
    label: String,
    accentColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.65f))
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(accentColor.copy(alpha = 0.14f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(18.dp)
                )
            }
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun CaregiverClientRequestCard(
    booking: BookingEntity,
    onClick: () -> Unit,
    onAccept: () -> Unit,
    onStart: () -> Unit,
    onComplete: () -> Unit,
    onCancelWithReason: () -> Unit,
    onOpenChat: () -> Unit,
    modifier: Modifier = Modifier
) {
    val clientDisplay = booking.clientName.ifBlank { "Cliente" }
    val clientInitials = clientDisplay
        .trim()
        .split(" ")
        .filter { it.isNotBlank() }
        .take(2)
        .joinToString("") { it.take(1).uppercase() }
        .ifBlank { "CL" }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .clickable(onClick = onClick)
            .testTag("caregiver_booking_card_${booking.id}"),
        shape = RoundedCornerShape(22.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.65f)),
        shadowElevation = 1.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = clientInitials,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            text = clientDisplay,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        val petText = if (booking.petSpecies.isNotBlank()) {
                            "Pet: ${booking.petName} (${booking.petSpecies})"
                        } else {
                            "Pet: ${booking.petName}"
                        }
                        Text(
                            text = petText,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(50),
                    color = booking.status.containerColor
                ) {
                    Text(
                        text = booking.status.label,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = booking.status.contentColor,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Schedule,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "${booking.dateLabel} às ${booking.timeLabel}",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Text(
                    text = "${booking.serviceName} • R$ ${booking.priceReais.toInt()}",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            if (booking.notes.isNotBlank()) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.background,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = booking.notes,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }

            if (booking.cancellationReason.isNotBlank()) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = booking.status.containerColor.copy(alpha = 0.7f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Motivo informado: ${booking.cancellationReason}",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium,
                        color = booking.status.contentColor,
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.45f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onOpenChat,
                    shape = RoundedCornerShape(50),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                    modifier = Modifier.height(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.ChatBubbleOutline,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Chat", style = MaterialTheme.typography.labelMedium)
                }

                when (booking.status) {
                    BookingStatus.PENDENTE -> {
                        OutlinedButton(
                            onClick = onCancelWithReason,
                            shape = RoundedCornerShape(50),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = DangerText),
                            border = BorderStroke(1.dp, DangerText.copy(alpha = 0.35f)),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp)
                                .testTag("caregiver_decline_${booking.id}")
                        ) {
                            Text("Desmarcar", style = MaterialTheme.typography.labelMedium)
                        }
                        Button(
                            onClick = onAccept,
                            shape = RoundedCornerShape(50),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp)
                                .testTag("caregiver_accept_${booking.id}")
                        ) {
                            Text("Aceitar", style = MaterialTheme.typography.labelMedium)
                        }
                    }

                    BookingStatus.AGENDADO -> {
                        OutlinedButton(
                            onClick = onCancelWithReason,
                            shape = RoundedCornerShape(50),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = DangerText),
                            border = BorderStroke(1.dp, DangerText.copy(alpha = 0.35f)),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp)
                        ) {
                            Text("Desmarcar", style = MaterialTheme.typography.labelMedium)
                        }
                        Button(
                            onClick = onStart,
                            shape = RoundedCornerShape(50),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp)
                        ) {
                            Text("Iniciar", style = MaterialTheme.typography.labelMedium)
                        }
                    }

                    BookingStatus.EM_ANDAMENTO -> {
                        OutlinedButton(
                            onClick = onCancelWithReason,
                            shape = RoundedCornerShape(50),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = DangerText),
                            border = BorderStroke(1.dp, DangerText.copy(alpha = 0.35f)),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp)
                        ) {
                            Text("Desmarcar", style = MaterialTheme.typography.labelMedium)
                        }
                        Button(
                            onClick = onComplete,
                            shape = RoundedCornerShape(50),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp)
                        ) {
                            Text("Concluir", style = MaterialTheme.typography.labelMedium)
                        }
                    }

                    BookingStatus.CONCLUIDO, BookingStatus.RECUSADO -> {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun CaregiverBookingDetailBottomSheet(
    booking: BookingEntity,
    onDismiss: () -> Unit,
    onChangeStatus: (BookingStatus) -> Unit,
    onRequestCancelWithReason: (BookingEntity) -> Unit,
    onOpenChat: (BookingEntity) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 28.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = booking.serviceName,
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        text = "Cliente: ${booking.clientName.ifBlank { "Tutor" }} • Pet: ${booking.petName}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    if (booking.clientPhone.isNotBlank()) {
                        Text(
                            text = "Contato: ${booking.clientPhone}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                Surface(
                    shape = RoundedCornerShape(50),
                    color = booking.status.containerColor
                ) {
                    Text(
                        text = booking.status.label,
                        style = MaterialTheme.typography.labelMedium,
                        color = booking.status.contentColor,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Schedule,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = "${booking.dateLabel} às ${booking.timeLabel} • R$ ${booking.priceReais.toInt()}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
            }

            if (booking.notes.isNotBlank()) {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.background,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = booking.notes,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            if (booking.cancellationReason.isNotBlank()) {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = booking.status.containerColor,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Motivo registrado: ${booking.cancellationReason}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = booking.status.contentColor,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Gerenciar atendimento",
                    style = MaterialTheme.typography.titleSmall
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(
                        BookingStatus.AGENDADO to "Confirmar / Aceitar",
                        BookingStatus.EM_ANDAMENTO to "Em andamento",
                        BookingStatus.CONCLUIDO to "Concluído"
                    ).forEach { (status, label) ->
                        FilterChip(
                            selected = booking.status == status,
                            onClick = { onChangeStatus(status) },
                            label = { Text(label) }
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (booking.status != BookingStatus.RECUSADO && booking.status != BookingStatus.CONCLUIDO) {
                    OutlinedButton(
                        onClick = { onRequestCancelWithReason(booking) },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = DangerText),
                        border = BorderStroke(1.dp, DangerText.copy(alpha = 0.35f)),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("caregiver_sheet_cancel_button")
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.EventBusy,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Desmarcar")
                    }
                }

                Button(
                    onClick = { onOpenChat(booking) },
                    shape = RoundedCornerShape(50),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.ChatBubbleOutline,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Chat Cliente")
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CancelBookingReasonDialog(
    booking: BookingEntity,
    onDismiss: () -> Unit,
    onConfirmCancel: (String) -> Unit
) {
    val presets = remember {
        listOf(
            "Conflito de horário na agenda",
            "Indisponível nesta data",
            "Distância fora da área atendida",
            "Imprevisto pessoal"
        )
    }
    var reason by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface,
        title = {
            Text(
                text = "Desmarcar e explicar motivo",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Informe a ${booking.clientName.ifBlank { "cliente" }} por que o atendimento (${booking.serviceName} • ${booking.dateLabel}) será desmarcado:",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    presets.forEach { preset ->
                        FilterChip(
                            selected = reason == preset,
                            onClick = { reason = preset },
                            label = { Text(preset, style = MaterialTheme.typography.labelSmall) }
                        )
                    }
                }

                OutlinedTextField(
                    value = reason,
                    onValueChange = { reason = it },
                    label = { Text("Motivo para o cliente") },
                    placeholder = { Text("Explique claramente o motivo...") },
                    minLines = 3,
                    maxLines = 4,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("cancel_reason_input")
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirmCancel(reason) },
                enabled = reason.trim().length >= 4,
                colors = ButtonDefaults.buttonColors(
                    containerColor = DangerText,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(50),
                modifier = Modifier.testTag("confirm_cancel_with_reason_button")
            ) {
                Text("Confirmar desmarcação")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Voltar")
            }
        }
    )
}
