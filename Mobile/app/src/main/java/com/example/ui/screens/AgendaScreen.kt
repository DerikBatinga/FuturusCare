package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.rounded.EventAvailable
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.model.BookingEntity
import com.example.data.model.BookingStatus
import com.example.data.model.CalendarDayOption
import com.example.data.model.CaregiverMockData
import com.example.ui.PetCareUiState
import com.example.ui.components.BookingStatusCard

@Composable
fun AgendaScreen(
    uiState: PetCareUiState,
    onRefresh: () -> Unit,
    onSelectDay: (CalendarDayOption) -> Unit,
    onShowAll: () -> Unit,
    onSelectStatusFilter: (BookingStatus?) -> Unit,
    onBookingClick: (BookingEntity) -> Unit,
    onNewBookingClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val filteredBookings = uiState.filteredBookings

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("agenda_list"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Exact Top Header from Screenshot 1 & 3
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (uiState.isCaregiverMode) {
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            text = "Agenda com Clientes",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Toque em um horário para aceitar, atualizar ou desmarcar",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    Text(
                        text = stringResource(R.string.agenda_title),
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                TextButton(
                    onClick = onRefresh,
                    modifier = Modifier.testTag("agenda_refresh_button")
                ) {
                    Text(
                        text = stringResource(R.string.agenda_refresh),
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }

        // Exact Weekly Calendar Card from Screenshot 1 & 3
        item {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.65f)),
                shadowElevation = 1.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.agenda_month_title),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Icon(
                            imageVector = Icons.Outlined.CalendarToday,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        CaregiverMockData.novemberWeekDays.take(6).forEach { day ->
                            val isSelected = !uiState.showAllDates && uiState.selectedDateIso == day.dateIso
                            val hasBookings = uiState.bookings.any { it.dateIso == day.dateIso }

                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(
                                        if (isSelected) MaterialTheme.colorScheme.primary
                                        else MaterialTheme.colorScheme.surface
                                    )
                                    .clickable { onSelectDay(day) }
                                    .padding(vertical = 8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(3.dp)
                            ) {
                                Text(
                                    text = day.dayOfWeek,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary
                                    else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = day.dayNumber,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary
                                    else MaterialTheme.colorScheme.onSurface
                                )
                                Box(
                                    modifier = Modifier
                                        .size(5.dp)
                                        .clip(CircleShape)
                                        .background(
                                            when {
                                                hasBookings && isSelected -> MaterialTheme.colorScheme.onPrimary
                                                hasBookings -> MaterialTheme.colorScheme.primary
                                                else -> androidx.compose.ui.graphics.Color.Transparent
                                            }
                                        )
                                )
                            }
                        }
                    }
                }
            }
        }

        // Unified list header + compact status filter
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (uiState.showAllDates) "Todos os agendamentos" else "Agendamentos do dia",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    TextButton(onClick = onShowAll) {
                        Text(stringResource(R.string.agenda_filter_all))
                    }
                }

                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val dateScoped = if (uiState.showAllDates) uiState.bookings
                    else uiState.bookings.filter { it.dateIso == uiState.selectedDateIso }

                    FilterChip(
                        selected = uiState.selectedStatusFilter == null,
                        onClick = { onSelectStatusFilter(null) },
                        label = { Text("Todos (${dateScoped.size})") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.primary
                        )
                    )
                    BookingStatus.entries.forEach { status ->
                        val count = dateScoped.count { it.status == status }
                        FilterChip(
                            selected = uiState.selectedStatusFilter == status,
                            onClick = { onSelectStatusFilter(status) },
                            label = { Text("${status.label} ($count)") },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = status.containerColor,
                                selectedLabelColor = status.contentColor
                            )
                        )
                    }
                }
            }
        }

        if (filteredBookings.isEmpty()) {
            item {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.65f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.EventAvailable,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(32.dp)
                        )
                        Text(
                            text = stringResource(R.string.agenda_empty_title),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = if (uiState.isCaregiverMode) {
                                "Quando clientes contratarem seus serviços, você poderá gerenciar ou desmarcar com motivo aqui."
                            } else {
                                stringResource(R.string.agenda_empty_subtitle)
                            },
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        } else {
            items(filteredBookings, key = { it.id }) { booking ->
                BookingStatusCard(
                    booking = booking,
                    onClick = { onBookingClick(booking) },
                    showClientAsPrimary = uiState.isCaregiverMode
                )
            }
        }

        if (!uiState.isCaregiverMode) {
            item {
                Button(
                    onClick = onNewBookingClick,
                    shape = RoundedCornerShape(50),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("agenda_new_booking_button")
                ) {
                    Text(
                        text = stringResource(R.string.agenda_new_booking),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}
