package com.example.ui.screens

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.rounded.Logout
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Badge
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.DeleteOutline
import androidx.compose.material.icons.rounded.Description
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Groups
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Image
import androidx.compose.material.icons.rounded.LocationCity
import androidx.compose.material.icons.rounded.MyLocation
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.NotificationsActive
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Pets
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material.icons.rounded.PhotoCamera
import androidx.compose.material.icons.rounded.Shield
import androidx.compose.material.icons.rounded.UploadFile
import androidx.compose.material.icons.rounded.Verified
import androidx.compose.material.icons.rounded.WorkspacePremium
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.R
import com.example.data.model.Caregiver
import com.example.data.model.CaregiverCertificateEntity
import com.example.data.model.CaregiverMockData
import com.example.ui.PetCareUiState
import com.example.ui.components.CaregiverCard
import com.example.ui.components.PetAvatarImage
import com.example.ui.theme.CaregiverToneSky
import com.example.ui.theme.DangerBg
import com.example.ui.theme.DangerText
import com.example.ui.theme.StatusCompletedBg
import com.example.ui.theme.StatusCompletedText
import com.example.ui.theme.StatusPendingBg
import com.example.ui.theme.StatusPendingText
import com.example.util.DetectedLocation
import com.example.util.ImageStorageHelper
import com.example.util.LocationHelper
import com.example.util.NotificationHelper
import kotlinx.coroutines.launch
import java.io.File

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProfileScreen(
    uiState: PetCareUiState,
    onUpdateAvatar: (String) -> Unit,
    onUpdateBanner: (String) -> Unit,
    onUpdateProfileInfo: (
        name: String,
        email: String,
        phone: String,
        address: String,
        city: String,
        neighborhood: String,
        bio: String
    ) -> Unit,
    onDetectLocationSuccess: (DetectedLocation) -> Unit = {},
    onLocationError: (String) -> Unit = {},
    onToggleNotifications: (Boolean) -> Unit,
    onNavigateToPets: () -> Unit = {},
    onNavigateToAgenda: () -> Unit = {},
    onOpenNotifications: () -> Unit = {},
    onLogout: () -> Unit = {},
    onUpdateCaregiverShowcase: (
        headline: String,
        specialties: List<String>,
        pricePerSession: Double,
        priceUnit: String,
        experienceYears: Int,
        highlights: List<String>,
        bio: String,
        isAvailableForClients: Boolean
    ) -> Unit = { _, _, _, _, _, _, _, _ -> },
    onAddCertificate: (
        title: String,
        issuer: String,
        filePath: String,
        fileName: String,
        isImage: Boolean
    ) -> Unit = { _, _, _, _, _ -> },
    onRemoveCertificate: (CaregiverCertificateEntity) -> Unit = {},
    onNavigateToClients: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val profile = uiState.profile
    val isCaregiver = uiState.isCaregiverMode
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var showLogoutDialog by remember { mutableStateOf(false) }
    var isDetectingLocation by remember { mutableStateOf(false) }
    var showAttachCertDialog by remember { mutableStateOf(false) }
    var previewCertImagePath by remember { mutableStateOf<String?>(null) }

    // Inline form states synced with profile
    var name by remember(profile?.userId, profile?.name) { mutableStateOf(profile?.name.orEmpty()) }
    var email by remember(profile?.userId, profile?.email) { mutableStateOf(profile?.email.orEmpty()) }
    var phone by remember(profile?.userId, profile?.phone) { mutableStateOf(profile?.phone.orEmpty()) }
    var address by remember(profile?.userId, profile?.address) { mutableStateOf(profile?.address.orEmpty()) }
    var city by remember(profile?.userId, profile?.city) { mutableStateOf(profile?.city.orEmpty()) }
    var neighborhood by remember(profile?.userId, profile?.neighborhood) { mutableStateOf(profile?.neighborhood.orEmpty()) }
    var bio by remember(profile?.userId, profile?.bio) { mutableStateOf(profile?.bio.orEmpty()) }

    LaunchedEffect(profile?.address, profile?.city, profile?.neighborhood) {
        if (profile != null) {
            if (address.isBlank() && profile.address.isNotBlank()) address = profile.address
            if (city.isBlank() && profile.city.isNotBlank()) city = profile.city
            if (neighborhood.isBlank() && profile.neighborhood.isNotBlank()) neighborhood = profile.neighborhood
        }
    }

    // Caregiver showcase editor states synced with profile
    var cgHeadline by remember(profile?.userId, profile?.headline) {
        mutableStateOf(profile?.headline.orEmpty())
    }
    var cgSpecialties by remember(profile?.userId, profile?.specialtiesCsv, profile?.specialty) {
        mutableStateOf(
            profile?.specialtiesList?.takeIf { it.isNotEmpty() } ?: listOf("Dog Walking")
        )
    }
    var cgPriceText by remember(profile?.userId, profile?.pricePerSession) {
        val price = profile?.pricePerSession ?: 55.0
        mutableStateOf(if (price > 0) price.toInt().toString() else "55")
    }
    var cgPriceUnit by remember(profile?.userId, profile?.priceUnit) {
        mutableStateOf(profile?.priceUnit?.ifBlank { "/h" } ?: "/h")
    }
    var cgExperienceText by remember(profile?.userId, profile?.experienceYears) {
        mutableStateOf((profile?.experienceYears ?: 1).toString())
    }
    var cgHighlights by remember(profile?.userId, profile?.highlightsCsv) {
        mutableStateOf(profile?.highlightsList.orEmpty())
    }
    var cgBio by remember(profile?.userId, profile?.bio) {
        mutableStateOf(profile?.bio.orEmpty())
    }
    var cgAvailable by remember(profile?.userId, profile?.isAvailableForClients) {
        mutableStateOf(profile?.isAvailableForClients ?: true)
    }

    val hasUnsavedChanges = profile != null && (
        name.trim() != profile.name ||
            email.trim() != profile.email ||
            phone.trim() != profile.phone ||
            address.trim() != profile.address ||
            city.trim() != profile.city ||
            neighborhood.trim() != profile.neighborhood ||
            bio.trim() != profile.bio
        )

    val avatarPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            coroutineScope.launch {
                val path = ImageStorageHelper.copyUriToInternalStorage(context, uri, "avatar")
                if (path != null) onUpdateAvatar(path)
            }
        }
    }

    val bannerPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            coroutineScope.launch {
                val path = ImageStorageHelper.copyUriToInternalStorage(context, uri, "banner")
                if (path != null) onUpdateBanner(path)
            }
        }
    }

    val executeLocationDetection: () -> Unit = {
        coroutineScope.launch {
            isDetectingLocation = true
            val result = LocationHelper.detectCurrentLocation(context)
            result.fold(
                onSuccess = { detected ->
                    address = detected.address
                    city = detected.city
                    neighborhood = detected.neighborhood
                    onDetectLocationSuccess(detected)
                },
                onFailure = { error ->
                    onLocationError(error.message ?: "Não foi possível obter sua localização agora.")
                }
            )
            isDetectingLocation = false
        }
    }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { grants ->
        if (grants.values.any { it }) {
            executeLocationDetection()
        } else {
            onLocationError("Permissão de localização negada. Preencha seu endereço manualmente.")
        }
    }

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        onToggleNotifications(granted)
        if (!granted) {
            onLocationError("Permissão de notificações negada pelo sistema.")
        }
    }

    val isVerified = profile?.isVerified == true

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("profile_list"),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 28.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Top Bar Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.profile_title),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.7f))
                ) {
                    IconButton(
                        onClick = onOpenNotifications,
                        modifier = Modifier.testTag("profile_notifications_button")
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
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        } else {
                            Icon(
                                imageVector = Icons.Rounded.Notifications,
                                contentDescription = stringResource(R.string.cd_notifications),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }

        // 2. Hero Profile Card: Banner + Overlapping Avatar + Conditional Verified Badge + Photo/Banner Controls
        item {
            Surface(
                shape = RoundedCornerShape(26.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.65f)),
                shadowElevation = 1.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(216.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(164.dp)
                                .padding(horizontal = 10.dp, vertical = 10.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .background(MaterialTheme.colorScheme.primaryContainer)
                                .align(Alignment.TopCenter)
                        ) {
                            val bannerPath = profile?.bannerPath
                            if (!bannerPath.isNullOrBlank() && File(bannerPath).exists()) {
                                AsyncImage(
                                    model = File(bannerPath),
                                    contentDescription = stringResource(R.string.cd_profile_banner),
                                    contentScale = ContentScale.Crop,
                                    filterQuality = FilterQuality.High,
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else {
                                Image(
                                    painter = painterResource(id = R.drawable.img_profile_banner),
                                    contentDescription = stringResource(R.string.cd_profile_banner),
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp)
                                    .align(Alignment.BottomCenter)
                                    .background(
                                        Brush.verticalGradient(
                                            colors = listOf(
                                                Color.Transparent,
                                                Color.Black.copy(alpha = 0.18f)
                                            )
                                        )
                                    )
                            )
                        }

                        // Centered Unobstructed Profile Avatar
                        Surface(
                            shape = CircleShape,
                            shadowElevation = 4.dp,
                            color = MaterialTheme.colorScheme.primaryContainer,
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .size(108.dp)
                                .border(4.dp, MaterialTheme.colorScheme.surface, CircleShape)
                                .border(1.5.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.25f), CircleShape)
                                .clip(CircleShape)
                                .clickable {
                                    avatarPicker.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                    )
                                }
                                .testTag("profile_avatar_image")
                        ) {
                            val avatarPath = profile?.avatarPath
                            when {
                                !avatarPath.isNullOrBlank() && File(avatarPath).exists() -> {
                                    AsyncImage(
                                        model = File(avatarPath),
                                        contentDescription = stringResource(R.string.cd_profile_avatar),
                                        contentScale = ContentScale.Crop,
                                        filterQuality = FilterQuality.High,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                                !avatarPath.isNullOrBlank() -> {
                                    Image(
                                        painter = painterResource(id = R.drawable.img_tutor_avatar),
                                        contentDescription = stringResource(R.string.cd_profile_avatar),
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                                else -> {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        val initials = profile?.name
                                            ?.trim()
                                            ?.split(" ")
                                            ?.filter { it.isNotBlank() }
                                            ?.take(2)
                                            ?.joinToString("") { it.take(1).uppercase() }
                                            ?.ifBlank { "FC" }
                                            ?: "FC"
                                        Text(
                                            text = initials,
                                            style = MaterialTheme.typography.headlineMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Tutor Identity & Conditional Verified Badge
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            val fallbackRoleName = if (isCaregiver) "Cuidador FuturusCare" else "Tutor FuturusCare"
                            Text(
                                text = profile?.name?.ifBlank { fallbackRoleName } ?: fallbackRoleName,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            if (isVerified) {
                                Icon(
                                    imageVector = Icons.Rounded.Verified,
                                    contentDescription = stringResource(R.string.cd_verified_badge),
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier
                                        .size(22.dp)
                                        .testTag("verified_badge_icon")
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(50),
                            color = if (isVerified) {
                                MaterialTheme.colorScheme.primaryContainer
                            } else {
                                StatusPendingBg
                            },
                            modifier = Modifier.testTag("verified_status_pill")
                        ) {
                            val badgeLabel = when {
                                isCaregiver && isVerified -> "Cuidador Verificado"
                                isCaregiver -> "Cuidador em verificação"
                                isVerified -> stringResource(R.string.profile_tutor_verified_badge)
                                else -> stringResource(R.string.profile_tutor_pending_badge)
                            }
                            val region = profile?.formattedRegion ?: "Localização não definida"
                            Text(
                                text = "$badgeLabel • $region",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = if (isVerified) MaterialTheme.colorScheme.primary else StatusPendingText,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                            )
                        }

                        if (!profile?.bio.isNullOrBlank()) {
                            Text(
                                text = profile?.bio.orEmpty(),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Unobstructed Photo / Banner Action Buttons cleanly below the avatar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                avatarPicker.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            },
                            shape = RoundedCornerShape(50),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.35f)),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f),
                                contentColor = MaterialTheme.colorScheme.primary
                            ),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .testTag("change_photo_button")
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.PhotoCamera,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = stringResource(R.string.profile_change_photo),
                                style = MaterialTheme.typography.labelLarge,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        OutlinedButton(
                            onClick = {
                                bannerPicker.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            },
                            shape = RoundedCornerShape(50),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.35f)),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f),
                                contentColor = MaterialTheme.colorScheme.primary
                            ),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .testTag("change_banner_button")
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Image,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = stringResource(R.string.profile_change_banner),
                                style = MaterialTheme.typography.labelLarge,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }

        // 3. Verification Progress Card (Only shown until photo + all fields [+ certificates for Caregiver] are complete)
        if (profile != null && !profile.isVerified) {
            item {
                val totalSteps = profile.totalVerificationSteps.coerceAtLeast(1)
                Surface(
                    shape = RoundedCornerShape(22.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.dp, StatusPendingText.copy(alpha = 0.25f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("verification_progress_card")
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Shield,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "Desbloqueie seu selo Verificado (${profile.completionCount}/$totalSteps)",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        LinearProgressIndicator(
                            progress = { profile.completionCount / totalSteps.toFloat() },
                            color = MaterialTheme.colorScheme.primary,
                            trackColor = MaterialTheme.colorScheme.primaryContainer,
                            strokeCap = StrokeCap.Round,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                        )

                        Text(
                            text = if (isCaregiver) {
                                "Adicione foto de perfil, preencha seus dados profissionais e anexe pelo menos 1 certificado para ganhar o selo de Cuidador Verificado."
                            } else {
                                "Adicione uma foto de perfil e preencha todas as suas informações abaixo para receber o selo."
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            profile.missingVerificationItems.forEach { missing ->
                                Surface(
                                    shape = RoundedCornerShape(50),
                                    color = StatusPendingBg
                                ) {
                                    Text(
                                        text = "Falta: $missing",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = StatusPendingText,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }

                        if (isCaregiver && !profile.hasCaregiverCertificate) {
                            OutlinedButton(
                                onClick = { showAttachCertDialog = true },
                                shape = RoundedCornerShape(50),
                                modifier = Modifier.testTag("progress_attach_cert_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.UploadFile,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Anexar certificado agora")
                            }
                        }
                    }
                }
            }
        } else if (profile != null && profile.isVerified) {
            item {
                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = StatusCompletedBg,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.CheckCircle,
                            contentDescription = null,
                            tint = StatusCompletedText,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = if (isCaregiver) {
                                "Perfil e certificados validados! Seu selo Cuidador Verificado já destaca você para novos clientes."
                            } else {
                                "Perfil completo! Seu selo de Tutor Verificado está ativo para os cuidadores."
                            },
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.SemiBold,
                            color = StatusCompletedText
                        )
                    }
                }
            }
        }

        // 4. Interactive Stats Row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (isCaregiver) {
                    ProfileStatCard(
                        icon = Icons.Rounded.Groups,
                        value = uiState.bookings.size.toString(),
                        label = "Clientes",
                        onClick = onNavigateToClients,
                        modifier = Modifier.weight(1f)
                    )
                    ProfileStatCard(
                        icon = Icons.Rounded.WorkspacePremium,
                        value = uiState.certificates.size.toString(),
                        label = "Certificados",
                        onClick = { showAttachCertDialog = true },
                        modifier = Modifier.weight(1f)
                    )
                    ProfileStatCard(
                        icon = Icons.Rounded.Notifications,
                        value = uiState.notifications.size.toString(),
                        label = "Notificações",
                        onClick = onOpenNotifications,
                        modifier = Modifier.weight(1f)
                    )
                } else {
                    ProfileStatCard(
                        icon = Icons.Rounded.Pets,
                        value = uiState.pets.size.toString(),
                        label = "Meus Pets",
                        onClick = onNavigateToPets,
                        modifier = Modifier.weight(1f)
                    )
                    ProfileStatCard(
                        icon = Icons.Rounded.CalendarMonth,
                        value = uiState.bookings.size.toString(),
                        label = "Agendamentos",
                        onClick = onNavigateToAgenda,
                        modifier = Modifier.weight(1f)
                    )
                    ProfileStatCard(
                        icon = Icons.Rounded.Notifications,
                        value = uiState.notifications.size.toString(),
                        label = "Notificações",
                        onClick = onOpenNotifications,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // 5. Caregiver Sections (Certificates + Showcase Editor) OR Client "Meus Pets Cadastrados"
        if (isCaregiver) {
            item {
                CaregiverCertificatesCard(
                    certificates = uiState.certificates,
                    onAttachClick = { showAttachCertDialog = true },
                    onPreviewImage = { previewCertImagePath = it },
                    onRemoveCertificate = onRemoveCertificate
                )
            }

            item {
                val previewInitials = remember(name) {
                    name.trim()
                        .split(" ")
                        .filter { it.isNotBlank() }
                        .take(2)
                        .joinToString("") { it.take(1).uppercase() }
                        .ifBlank { "CU" }
                }
                val previewPrice = cgPriceText.toDoubleOrNull()?.takeIf { it > 0 } ?: 55.0
                val previewExp = cgExperienceText.toIntOrNull()?.coerceAtLeast(0) ?: 1
                val previewCardCaregiver = Caregiver(
                    id = "preview_caregiver",
                    name = name.ifBlank { "Seu Nome" },
                    roleTitle = cgHeadline.ifBlank {
                        cgSpecialties.firstOrNull()?.let { "$it • PetSitter" } ?: "Cuidador PetSitter"
                    },
                    neighborhood = neighborhood.ifBlank { city.ifBlank { "Seu Bairro" } },
                    distanceLabel = "Seu perfil",
                    rating = 5.0,
                    reviewCount = uiState.completedBookingsCount.coerceAtLeast(1),
                    pricePerSession = previewPrice,
                    priceUnit = cgPriceUnit,
                    specialties = cgSpecialties.ifEmpty { listOf("Dog Walking") },
                    bio = cgBio.ifBlank { "Apresente sua experiência e diferenciais para encantar os tutores." },
                    verified = isVerified,
                    experienceYears = previewExp,
                    completedServices = uiState.completedBookingsCount,
                    responseTime = if (cgAvailable) "Disponível agora" else "Agenda pausada",
                    initials = previewInitials,
                    avatarTone = CaregiverToneSky
                )

                CaregiverShowcaseEditorCard(
                    services = uiState.services.map { it.title },
                    headline = cgHeadline,
                    onHeadlineChange = { cgHeadline = it },
                    selectedSpecialties = cgSpecialties,
                    onToggleSpecialty = { spec ->
                        cgSpecialties = if (cgSpecialties.contains(spec)) {
                            if (cgSpecialties.size > 1) cgSpecialties - spec else cgSpecialties
                        } else {
                            cgSpecialties + spec
                        }
                    },
                    priceText = cgPriceText,
                    onPriceTextChange = { cgPriceText = it.filter { ch -> ch.isDigit() || ch == '.' || ch == ',' } },
                    priceUnit = cgPriceUnit,
                    onPriceUnitChange = { cgPriceUnit = it },
                    experienceText = cgExperienceText,
                    onExperienceTextChange = { cgExperienceText = it.filter { ch -> ch.isDigit() } },
                    selectedHighlights = cgHighlights,
                    onToggleHighlight = { highlight ->
                        cgHighlights = if (cgHighlights.contains(highlight)) {
                            cgHighlights - highlight
                        } else {
                            cgHighlights + highlight
                        }
                    },
                    bio = cgBio,
                    onBioChange = {
                        cgBio = it
                        bio = it
                    },
                    isAvailable = cgAvailable,
                    onAvailableChange = { cgAvailable = it },
                    previewCaregiver = previewCardCaregiver,
                    onSaveShowcase = {
                        val parsedPrice = cgPriceText.replace(",", ".").toDoubleOrNull() ?: 55.0
                        val parsedExp = cgExperienceText.toIntOrNull() ?: 1
                        onUpdateCaregiverShowcase(
                            cgHeadline,
                            cgSpecialties,
                            parsedPrice,
                            cgPriceUnit,
                            parsedExp,
                            cgHighlights,
                            cgBio,
                            cgAvailable
                        )
                    }
                )
            }
        } else if (uiState.pets.isNotEmpty()) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.profile_my_pets_section),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        TextButton(onClick = onNavigateToPets) {
                            Text(stringResource(R.string.profile_manage_pets))
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        uiState.pets.forEach { pet ->
                            Surface(
                                shape = RoundedCornerShape(18.dp),
                                color = MaterialTheme.colorScheme.surface,
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.65f)),
                                modifier = Modifier
                                    .widthIn(min = 176.dp, max = 216.dp)
                                    .clip(RoundedCornerShape(18.dp))
                                    .clickable(onClick = onNavigateToPets)
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    PetAvatarImage(pet = pet, size = 46.dp)
                                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                        Text(
                                            text = pet.name,
                                            style = MaterialTheme.typography.titleSmall,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Text(
                                            text = "${pet.species} • ${pet.ageYears}a",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // 6. Full Inline Editable "Dados do Tutor / Cuidador" Card
        item {
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.65f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isCaregiver) "Dados do Cuidador" else stringResource(R.string.profile_data_section),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        OutlinedButton(
                            onClick = {
                                if (LocationHelper.hasLocationPermission(context)) {
                                    executeLocationDetection()
                                } else {
                                    locationPermissionLauncher.launch(
                                        arrayOf(
                                            Manifest.permission.ACCESS_COARSE_LOCATION,
                                            Manifest.permission.ACCESS_FINE_LOCATION
                                        )
                                    )
                                }
                            },
                            enabled = !isDetectingLocation,
                            shape = RoundedCornerShape(50),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.35f)),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                            modifier = Modifier.testTag("profile_detect_location_button")
                        ) {
                            if (isDetectingLocation) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(14.dp),
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Rounded.MyLocation,
                                    contentDescription = null,
                                    modifier = Modifier.size(15.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Localização auto",
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    }

                    ProfileFormTextField(
                        label = "Nome completo",
                        value = name,
                        onValueChange = { name = it },
                        placeholder = "Seu nome completo",
                        leadingIcon = Icons.Rounded.Person,
                        testTag = "profile_name_input"
                    )

                    ProfileFormTextField(
                        label = "E-mail",
                        value = email,
                        onValueChange = { email = it },
                        placeholder = "seu@email.com",
                        leadingIcon = Icons.Rounded.Email,
                        keyboardType = KeyboardType.Email,
                        testTag = "profile_email_input"
                    )

                    ProfileFormTextField(
                        label = "Telefone",
                        value = phone,
                        onValueChange = { phone = it },
                        placeholder = "(11) 99999-9999",
                        leadingIcon = Icons.Rounded.Phone,
                        keyboardType = KeyboardType.Phone,
                        testTag = "profile_phone_input"
                    )

                    ProfileFormTextField(
                        label = "Endereço",
                        value = address,
                        onValueChange = { address = it },
                        placeholder = "Rua São João, 120",
                        leadingIcon = Icons.Rounded.Home,
                        testTag = "profile_address_input"
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        ProfileFormTextField(
                            label = "Cidade",
                            value = city,
                            onValueChange = { city = it },
                            placeholder = "São Paulo",
                            leadingIcon = Icons.Rounded.LocationCity,
                            modifier = Modifier.weight(1f),
                            testTag = "profile_city_input"
                        )
                        ProfileFormTextField(
                            label = "Bairro",
                            value = neighborhood,
                            onValueChange = { neighborhood = it },
                            placeholder = "Pinheiros",
                            leadingIcon = null,
                            modifier = Modifier.weight(1f),
                            testTag = "profile_neighborhood_input"
                        )
                    }

                    ProfileFormTextField(
                        label = "Sobre você",
                        value = bio,
                        onValueChange = { bio = it },
                        placeholder = "Conte sobre você e a rotina com seus pets...",
                        leadingIcon = null,
                        singleLine = false,
                        minLines = 2,
                        testTag = "profile_bio_input"
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Button(
                        onClick = {
                            onUpdateProfileInfo(
                                name,
                                email,
                                phone,
                                address,
                                city,
                                neighborhood,
                                bio
                            )
                        },
                        enabled = name.isNotBlank() && email.isNotBlank(),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("profile_save_button")
                    ) {
                        Text(
                            text = if (hasUnsavedChanges) stringResource(R.string.save_changes)
                            else "Dados salvos • Atualizar",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // 7. Preferences, Notification Center & Account Status
        item {
            Surface(
                shape = RoundedCornerShape(22.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.65f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = stringResource(R.string.profile_preferences_section),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.NotificationsActive,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = stringResource(R.string.profile_notifications),
                                style = MaterialTheme.typography.titleSmall
                            )
                            Text(
                                text = stringResource(R.string.profile_notifications_desc),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Switch(
                            checked = profile?.notificationsEnabled ?: true,
                            onCheckedChange = { wantEnable ->
                                if (wantEnable &&
                                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                                    !NotificationHelper.hasNotificationPermission(context)
                                ) {
                                    notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                } else {
                                    onToggleNotifications(wantEnable)
                                }
                            }
                        )
                    }

                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.45f))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .clickable(onClick = onOpenNotifications)
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.secondaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Notifications,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Central de Notificações",
                                style = MaterialTheme.typography.titleSmall
                            )
                            Text(
                                text = if (uiState.unreadNotificationCount > 0) {
                                    "${uiState.unreadNotificationCount} alertas não lidos"
                                } else {
                                    "Histórico de agendamentos e avisos"
                                },
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.45f))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.secondaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Badge,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = stringResource(R.string.profile_account_type_label),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = when {
                                    isCaregiver && isVerified -> "Cuidador Verificado"
                                    isCaregiver -> "Cuidador em verificação"
                                    isVerified -> "Tutor Verificado"
                                    else -> "Tutor em verificação"
                                },
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(50),
                            color = if (isVerified) StatusCompletedBg else StatusPendingBg
                        ) {
                            val totalSteps = profile?.totalVerificationSteps ?: if (isCaregiver) 10 else 8
                            Text(
                                text = if (isVerified) "Verificado" else "${profile?.completionCount ?: 0}/$totalSteps",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = if (isVerified) StatusCompletedText else StatusPendingText,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }

        // 8. Logout Button matching original TelasPerfil.kt DangerBg / DangerText colors
        item {
            Button(
                onClick = { showLogoutDialog = true },
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = DangerBg,
                    contentColor = DangerText
                ),
                border = BorderStroke(1.dp, DangerText.copy(alpha = 0.25f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("profile_logout_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.Logout,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.profile_logout),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            containerColor = MaterialTheme.colorScheme.surface,
            title = { Text("Sair da conta?") },
            text = {
                Text(
                    if (isCaregiver) {
                        "Seus certificados, perfil profissional e atendimentos ficam salvos localmente para quando você entrar novamente."
                    } else {
                        "Seus dados, pets e agendamentos ficam salvos no banco local para quando você entrar novamente."
                    }
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutDialog = false
                        onLogout()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DangerText,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(50)
                ) {
                    Text("Sair")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    if (showAttachCertDialog) {
        AttachCertificateDialog(
            onDismiss = { showAttachCertDialog = false },
            onCertificateReady = { title, issuer, filePath, fileName, isImage ->
                showAttachCertDialog = false
                onAddCertificate(title, issuer, filePath, fileName, isImage)
            },
            onError = onLocationError
        )
    }

    previewCertImagePath?.let { path ->
        Dialog(onDismissRequest = { previewCertImagePath = null }) {
            Surface(
                shape = RoundedCornerShape(22.dp),
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(modifier = Modifier.padding(12.dp)) {
                    AsyncImage(
                        model = File(path),
                        contentDescription = "Prévia do certificado",
                        contentScale = ContentScale.Fit,
                        filterQuality = FilterQuality.High,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(340.dp)
                            .clip(RoundedCornerShape(14.dp))
                    )
                    Surface(
                        shape = CircleShape,
                        color = Color.Black.copy(alpha = 0.6f),
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(6.dp)
                    ) {
                        IconButton(
                            onClick = { previewCertImagePath = null },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Close,
                                contentDescription = "Fechar",
                                tint = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CaregiverCertificatesCard(
    certificates: List<CaregiverCertificateEntity>,
    onAttachClick: () -> Unit,
    onPreviewImage: (String) -> Unit,
    onRemoveCertificate: (CaregiverCertificateEntity) -> Unit
) {
    Surface(
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.65f)),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("caregiver_certificates_card")
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.WorkspacePremium,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(22.dp)
                    )
                    Text(
                        text = "Certificados para Verificação",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                Surface(
                    shape = RoundedCornerShape(50),
                    color = if (certificates.isNotEmpty()) StatusCompletedBg else StatusPendingBg
                ) {
                    Text(
                        text = "${certificates.size} anexado(s)",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = if (certificates.isNotEmpty()) StatusCompletedText else StatusPendingText,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Text(
                text = "Anexe fotos ou PDFs de cursos, adestramento, primeiros socorros pet ou recomendações para conquistar o selo Cuidador Verificado.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (certificates.isEmpty()) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.background,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.45f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Nenhum certificado anexado ainda. Adicione ao menos 1 para liberar seu selinho.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(14.dp)
                    )
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    certificates.forEach { cert ->
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = MaterialTheme.colorScheme.background,
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.55f)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("certificate_item_${cert.id}")
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(MaterialTheme.colorScheme.primaryContainer)
                                        .let { mod ->
                                            if (cert.isImage && File(cert.filePath).exists()) {
                                                mod.clickable { onPreviewImage(cert.filePath) }
                                            } else mod
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (cert.isImage && File(cert.filePath).exists()) {
                                        AsyncImage(
                                            model = File(cert.filePath),
                                            contentDescription = cert.title,
                                            contentScale = ContentScale.Crop,
                                            filterQuality = FilterQuality.Medium,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    } else {
                                        Icon(
                                            imageVector = Icons.Rounded.Description,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                }

                                Column(
                                    modifier = Modifier.weight(1f),
                                    verticalArrangement = Arrangement.spacedBy(2.dp)
                                ) {
                                    Text(
                                        text = cert.title,
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.SemiBold,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    val sub = listOf(cert.issuer, cert.fileName)
                                        .filter { it.isNotBlank() }
                                        .joinToString(" • ")
                                    Text(
                                        text = sub,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }

                                IconButton(
                                    onClick = { onRemoveCertificate(cert) },
                                    modifier = Modifier.size(40.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.DeleteOutline,
                                        contentDescription = "Remover certificado",
                                        tint = DangerText
                                    )
                                }
                            }
                        }
                    }
                }
            }

            OutlinedButton(
                onClick = onAttachClick,
                shape = RoundedCornerShape(50),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.45f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("attach_certificate_button")
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Anexar Certificado (Foto ou PDF)",
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

@Composable
private fun AttachCertificateDialog(
    onDismiss: () -> Unit,
    onCertificateReady: (
        title: String,
        issuer: String,
        filePath: String,
        fileName: String,
        isImage: Boolean
    ) -> Unit,
    onError: (String) -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var title by remember { mutableStateOf("") }
    var issuer by remember { mutableStateOf("") }
    var isProcessing by remember { mutableStateOf(false) }

    val handleUri: (android.net.Uri?) -> Unit = { uri ->
        if (uri != null) {
            coroutineScope.launch {
                isProcessing = true
                val stored = ImageStorageHelper.copyCertificateToInternalStorage(context, uri)
                isProcessing = false
                if (stored != null) {
                    val resolvedTitle = title.trim().ifBlank {
                        stored.fileName.substringBeforeLast(".").ifBlank { "Certificado Pet" }
                    }
                    onCertificateReady(
                        resolvedTitle,
                        issuer.trim(),
                        stored.filePath,
                        stored.fileName,
                        stored.isImage
                    )
                } else {
                    onError("Não foi possível importar o arquivo selecionado.")
                }
            }
        }
    }

    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri -> handleUri(uri) }

    val documentPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri -> handleUri(uri) }

    AlertDialog(
        onDismissRequest = { if (!isProcessing) onDismiss() },
        containerColor = MaterialTheme.colorScheme.surface,
        title = {
            Text(
                text = "Anexar certificado",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Informe o nome do certificado e escolha uma foto ou documento PDF do seu aparelho.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Título do certificado") },
                    placeholder = { Text("Ex.: Primeiros Socorros Pet") },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("certificate_title_input")
                )

                OutlinedTextField(
                    value = issuer,
                    onValueChange = { issuer = it },
                    label = { Text("Instituição (opcional)") },
                    placeholder = { Text("Ex.: Escola Pet Brasil") },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("certificate_issuer_input")
                )

                if (isProcessing) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                        Text("Importando certificado...", style = MaterialTheme.typography.bodySmall)
                    }
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = {
                                photoPicker.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            },
                            shape = RoundedCornerShape(50),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.PhotoCamera,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Escolher Foto da Galeria")
                        }

                        OutlinedButton(
                            onClick = {
                                documentPicker.launch(arrayOf("application/pdf", "image/*"))
                            },
                            shape = RoundedCornerShape(50),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.UploadFile,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Escolher PDF ou Arquivo")
                        }
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss, enabled = !isProcessing) {
                Text("Fechar")
            }
        }
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun CaregiverShowcaseEditorCard(
    services: List<String>,
    headline: String,
    onHeadlineChange: (String) -> Unit,
    selectedSpecialties: List<String>,
    onToggleSpecialty: (String) -> Unit,
    priceText: String,
    onPriceTextChange: (String) -> Unit,
    priceUnit: String,
    onPriceUnitChange: (String) -> Unit,
    experienceText: String,
    onExperienceTextChange: (String) -> Unit,
    selectedHighlights: List<String>,
    onToggleHighlight: (String) -> Unit,
    bio: String,
    onBioChange: (String) -> Unit,
    isAvailable: Boolean,
    onAvailableChange: (Boolean) -> Unit,
    previewCaregiver: Caregiver,
    onSaveShowcase: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.65f)),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("caregiver_showcase_card")
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.AutoAwesome,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(22.dp)
                )
                Text(
                    text = "Melhorar Perfil para Atrair Clientes",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(MaterialTheme.colorScheme.background)
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (isAvailable) "Visível para clientes" else "Perfil pausado",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Exibir seu cartão na vitrine para novas contratações",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Switch(
                    checked = isAvailable,
                    onCheckedChange = onAvailableChange
                )
            }

            ProfileFormTextField(
                label = "Título de destaque",
                value = headline,
                onValueChange = onHeadlineChange,
                placeholder = "Ex.: Passeios educativos e atenção integral",
                leadingIcon = null,
                testTag = "caregiver_headline_input"
            )

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "Serviços que você oferece",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    services.forEach { spec ->
                        FilterChip(
                            selected = selectedSpecialties.contains(spec),
                            onClick = { onToggleSpecialty(spec) },
                            label = { Text(spec) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                selectedLabelColor = MaterialTheme.colorScheme.primary
                            )
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ProfileFormTextField(
                    label = "Valor base (R$)",
                    value = priceText,
                    onValueChange = onPriceTextChange,
                    placeholder = "55",
                    leadingIcon = null,
                    keyboardType = KeyboardType.Decimal,
                    modifier = Modifier.weight(1f),
                    testTag = "caregiver_price_input"
                )
                ProfileFormTextField(
                    label = "Experiência (anos)",
                    value = experienceText,
                    onValueChange = onExperienceTextChange,
                    placeholder = "2",
                    leadingIcon = null,
                    keyboardType = KeyboardType.Number,
                    modifier = Modifier.weight(1f),
                    testTag = "caregiver_experience_input"
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "Cobrança por",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf("/h", "/visita", "/diária", "/sessão").forEach { unit ->
                        FilterChip(
                            selected = priceUnit == unit,
                            onClick = { onPriceUnitChange(unit) },
                            label = { Text(unit) }
                        )
                    }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "Diferenciais que atraem tutores",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    CaregiverMockData.caregiverHighlightsCatalog.forEach { highlight ->
                        FilterChip(
                            selected = selectedHighlights.contains(highlight),
                            onClick = { onToggleHighlight(highlight) },
                            label = { Text(highlight, style = MaterialTheme.typography.labelSmall) }
                        )
                    }
                }
            }

            ProfileFormTextField(
                label = "Apresentação para clientes",
                value = bio,
                onValueChange = onBioChange,
                placeholder = "Conte sua experiência, rotina de passeios e cuidados especiais...",
                leadingIcon = null,
                singleLine = false,
                minLines = 2,
                testTag = "caregiver_showcase_bio_input"
            )

            Text(
                text = "Prévia de como os clientes veem você",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            CaregiverCard(
                caregiver = previewCaregiver,
                onClick = {},
                onBookClick = {}
            )

            Button(
                onClick = onSaveShowcase,
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("save_caregiver_showcase_button")
            ) {
                Text(
                    text = "Salvar perfil de Cuidador",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun ProfileFormTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: ImageVector?,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    singleLine: Boolean = true,
    minLines: Int = 1,
    testTag: String? = null
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    text = placeholder,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            leadingIcon = leadingIcon?.let { icon ->
                {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            },
            singleLine = singleLine,
            minLines = minLines,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.45f),
                unfocusedContainerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.35f),
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.7f)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .let { if (testTag != null) it.testTag(testTag) else it }
        )
    }
}

@Composable
private fun ProfileStatCard(
    icon: ImageVector,
    value: String,
    label: String,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .let { if (onClick != null) it.clickable(onClick = onClick) else it },
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.65f))
    ) {
        Column(
            modifier = Modifier.padding(vertical = 14.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(16.dp)
                )
            }
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
