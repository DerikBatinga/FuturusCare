package com.example

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.Pets
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material.icons.rounded.ChatBubble
import androidx.compose.material.icons.rounded.Groups
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Pets
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.data.local.PetCareDatabase
import com.example.data.model.BookingEntity
import com.example.data.model.BookingStatus
import com.example.data.model.Caregiver
import com.example.data.repository.PetCareRepository
import com.example.ui.PetCareUiState
import com.example.ui.PetCareViewModel
import com.example.ui.components.BookingDetailBottomSheet
import com.example.ui.components.CaregiverDetailBottomSheet
import com.example.ui.components.NewBookingBottomSheet
import com.example.ui.screens.AddEditPetScreen
import com.example.ui.screens.AgendaScreen
import com.example.ui.screens.CancelBookingReasonDialog
import com.example.ui.screens.CaregiverBookingDetailBottomSheet
import com.example.ui.screens.CaregiverChatListScreen
import com.example.ui.screens.CaregiverClientsScreen
import com.example.ui.screens.CaregiverHomeScreen
import com.example.ui.screens.ChatDetailScreen
import com.example.ui.screens.ChatListScreen
import com.example.ui.screens.FuturusLogoBadge
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.IntroScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.NotificationsScreen
import com.example.ui.screens.PetsScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.RegisterScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.theme.PetCareTheme
import com.example.util.LocationHelper
import com.example.util.NotificationHelper
import kotlinx.coroutines.launch

private object Routes {
    const val SPLASH = "splash"
    const val INTRO = "intro"
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
    const val PETS = "pets"
    const val CLIENTS = "clients"
    const val AGENDA = "agenda"
    const val CHAT = "chat"
    const val PROFILE = "profile"
    const val NOTIFICATIONS = "notifications"
    const val ADD_PET = "add_pet"
    const val EDIT_PET = "add_pet?petId={petId}"
    const val CHAT_DETAIL = "chat_detail/{caregiverId}"
    const val CAREGIVER_CHAT_DETAIL = "caregiver_chat/{clientUserId}/{caregiverId}"

    fun editPet(petId: Int) = "add_pet?petId=$petId"
    fun chatDetail(caregiverId: String) = "chat_detail/$caregiverId"
    fun caregiverChatDetail(clientUserId: Int, caregiverId: String) =
        "caregiver_chat/$clientUserId/$caregiverId"
}

private data class BottomNavItem(
    val route: String,
    val labelRes: Int,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val testTag: String
)

private val bottomNavItems = listOf(
    BottomNavItem(
        route = Routes.HOME,
        labelRes = R.string.nav_home,
        selectedIcon = Icons.Rounded.Home,
        unselectedIcon = Icons.Outlined.Home,
        testTag = "nav_home"
    ),
    BottomNavItem(
        route = Routes.PETS,
        labelRes = R.string.nav_pets,
        selectedIcon = Icons.Rounded.Pets,
        unselectedIcon = Icons.Outlined.Pets,
        testTag = "nav_pets"
    ),
    BottomNavItem(
        route = Routes.AGENDA,
        labelRes = R.string.nav_agenda,
        selectedIcon = Icons.Rounded.CalendarToday,
        unselectedIcon = Icons.Outlined.CalendarToday,
        testTag = "nav_agenda"
    ),
    BottomNavItem(
        route = Routes.CHAT,
        labelRes = R.string.nav_chat,
        selectedIcon = Icons.Rounded.ChatBubble,
        unselectedIcon = Icons.Outlined.ChatBubbleOutline,
        testTag = "nav_chat"
    ),
    BottomNavItem(
        route = Routes.PROFILE,
        labelRes = R.string.nav_profile,
        selectedIcon = Icons.Rounded.Person,
        unselectedIcon = Icons.Outlined.PersonOutline,
        testTag = "nav_profile"
    )
)

private val caregiverNavItems = listOf(
    BottomNavItem(
        route = Routes.HOME,
        labelRes = R.string.nav_dashboard,
        selectedIcon = Icons.Rounded.Home,
        unselectedIcon = Icons.Outlined.Home,
        testTag = "nav_home"
    ),
    BottomNavItem(
        route = Routes.CLIENTS,
        labelRes = R.string.nav_clients,
        selectedIcon = Icons.Rounded.Groups,
        unselectedIcon = Icons.Outlined.Groups,
        testTag = "nav_clients"
    ),
    BottomNavItem(
        route = Routes.AGENDA,
        labelRes = R.string.nav_agenda,
        selectedIcon = Icons.Rounded.CalendarToday,
        unselectedIcon = Icons.Outlined.CalendarToday,
        testTag = "nav_agenda"
    ),
    BottomNavItem(
        route = Routes.CHAT,
        labelRes = R.string.nav_chat,
        selectedIcon = Icons.Rounded.ChatBubble,
        unselectedIcon = Icons.Outlined.ChatBubbleOutline,
        testTag = "nav_chat"
    ),
    BottomNavItem(
        route = Routes.PROFILE,
        labelRes = R.string.nav_profile,
        selectedIcon = Icons.Rounded.Person,
        unselectedIcon = Icons.Outlined.PersonOutline,
        testTag = "nav_profile"
    )
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        NotificationHelper.ensureChannels(applicationContext)

        val database = PetCareDatabase.getInstance(applicationContext)
        val repository = PetCareRepository(
            userAccountDao = database.userAccountDao(),
            sessionDao = database.sessionDao(),
            petDao = database.petDao(),
            bookingDao = database.bookingDao(),
            chatDao = database.chatDao(),
            profileDao = database.profileDao(),
            certificateDao = database.certificateDao(),
            notificationDao = database.notificationDao()
        )
        val factory = PetCareViewModel.Factory(repository)

        setContent {
            PetCareTheme {
                val viewModel: PetCareViewModel = viewModel(factory = factory)
                PetCareApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun PetCareApp(viewModel: PetCareViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.snackbarMessage) {
        val message = uiState.snackbarMessage
        if (message != null) {
            snackbarHostState.showSnackbar(message)
            viewModel.consumeSnackbar()
        }
    }

    LaunchedEffect(viewModel) {
        viewModel.systemNotificationEvents.collect { event ->
            NotificationHelper.postSystemNotification(
                context = context,
                notificationId = event.id,
                title = event.title,
                body = event.body,
                isBookingAlert = event.isBookingAlert
            )
        }
    }

    when {
        !uiState.isSessionResolved -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(18.dp)
                ) {
                    FuturusLogoBadge(logoSize = 96.dp, showTagline = false)
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(28.dp),
                        strokeWidth = 2.5.dp
                    )
                }
            }
        }

        !uiState.isAuthenticated -> {
            AuthFlowHost(
                uiState = uiState,
                snackbarHostState = snackbarHostState,
                onLogin = viewModel::login,
                onRegister = viewModel::register,
                onClearError = viewModel::clearAuthError
            )
        }

        else -> {
            key(uiState.activeUserId) {
                AuthenticatedAppContent(
                    viewModel = viewModel,
                    uiState = uiState,
                    snackbarHostState = snackbarHostState
                )
            }
        }
    }
}

@Composable
private fun AuthFlowHost(
    uiState: PetCareUiState,
    snackbarHostState: SnackbarHostState,
    onLogin: (String, String, Boolean) -> Unit,
    onRegister: (
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
        isCaregiver: Boolean,
        specialty: String,
        experienceYears: String
    ) -> Unit,
    onClearError: () -> Unit
) {
    val authNavController = rememberNavController()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        NavHost(
            navController = authNavController,
            startDestination = Routes.SPLASH,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            composable(Routes.SPLASH) {
                SplashScreen(
                    onFinished = {
                        authNavController.navigate(Routes.INTRO) {
                            popUpTo(Routes.SPLASH) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }
            composable(Routes.INTRO) {
                IntroScreen(
                    onStartRegister = {
                        onClearError()
                        authNavController.navigate(Routes.REGISTER)
                    },
                    onGoToLogin = {
                        onClearError()
                        authNavController.navigate(Routes.LOGIN)
                    }
                )
            }
            composable(Routes.LOGIN) {
                LoginScreen(
                    authError = uiState.authError,
                    isLoading = uiState.isAuthLoading,
                    onLoginClick = onLogin,
                    onNavigateToRegister = {
                        authNavController.navigate(Routes.REGISTER)
                    },
                    onClearError = onClearError
                )
            }
            composable(Routes.REGISTER) {
                RegisterScreen(
                    authError = uiState.authError,
                    isLoading = uiState.isAuthLoading,
                    onBackToLogin = {
                        val popped = authNavController.popBackStack()
                        if (!popped) {
                            authNavController.navigate(Routes.LOGIN) {
                                launchSingleTop = true
                            }
                        }
                    },
                    onRegisterClick = onRegister,
                    onClearError = onClearError
                )
            }
        }
    }
}

@Composable
private fun AuthenticatedAppContent(
    viewModel: PetCareViewModel,
    uiState: PetCareUiState,
    snackbarHostState: SnackbarHostState
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    var selectedCaregiverForSheet by remember { mutableStateOf<Caregiver?>(null) }
    var showNewBookingSheet by remember { mutableStateOf(false) }
    var preselectedCaregiverForBooking by remember { mutableStateOf<Caregiver?>(null) }
    var selectedBookingForAction by remember { mutableStateOf<BookingEntity?>(null) }
    var bookingToCancelWithReason by remember { mutableStateOf<BookingEntity?>(null) }

    var requestedRuntimePermissionsOnce by remember(uiState.activeUserId) { mutableStateOf(false) }

    val startupPermissionsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { grants ->
        val locationGranted = grants[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            grants[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        val profile = uiState.profile
        if (locationGranted && profile != null && profile.city.isBlank() && profile.neighborhood.isBlank()) {
            coroutineScope.launch {
                LocationHelper.detectCurrentLocation(context).onSuccess { detected ->
                    viewModel.applyDetectedLocation(detected)
                }
            }
        }
    }

    LaunchedEffect(uiState.profile?.userId) {
        val profile = uiState.profile
        if (profile != null && !requestedRuntimePermissionsOnce) {
            requestedRuntimePermissionsOnce = true
            val missingPerms = buildList {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                    !NotificationHelper.hasNotificationPermission(context)
                ) {
                    add(Manifest.permission.POST_NOTIFICATIONS)
                }
                if (profile.city.isBlank() && profile.neighborhood.isBlank()) {
                    if (LocationHelper.hasLocationPermission(context)) {
                        LocationHelper.detectCurrentLocation(context).onSuccess { detected ->
                            viewModel.applyDetectedLocation(detected)
                        }
                    } else {
                        add(Manifest.permission.ACCESS_COARSE_LOCATION)
                        add(Manifest.permission.ACCESS_FINE_LOCATION)
                    }
                }
            }
            if (missingPerms.isNotEmpty()) {
                startupPermissionsLauncher.launch(missingPerms.toTypedArray())
            }
        }
    }

    val activeNavItems = if (uiState.isCaregiverMode) caregiverNavItems else bottomNavItems

    val showTopLevelNav = currentRoute in setOf(
        Routes.HOME,
        Routes.PETS,
        Routes.CLIENTS,
        Routes.AGENDA,
        Routes.CHAT,
        Routes.PROFILE
    )

    val navigateToTopTab: (String) -> Unit = { route ->
        navController.navigate(route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val useNavigationRail = maxWidth >= 600.dp

        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            bottomBar = {
                if (showTopLevelNav && !useNavigationRail) {
                    PetCareBottomBar(
                        items = activeNavItems,
                        currentRoute = currentRoute,
                        upcomingBookingsCount = uiState.upcomingBookings.size,
                        pendingClientsCount = if (uiState.isCaregiverMode) uiState.pendingBookingsCount else 0,
                        onNavigate = navigateToTopTab
                    )
                }
            }
        ) { innerPadding ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                if (showTopLevelNav && useNavigationRail) {
                    NavigationRail(
                        containerColor = MaterialTheme.colorScheme.surface
                    ) {
                        activeNavItems.forEach { item ->
                            val selected = currentRoute == item.route
                            NavigationRailItem(
                                selected = selected,
                                onClick = { navigateToTopTab(item.route) },
                                icon = {
                                    Icon(
                                        imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                                        contentDescription = stringResource(item.labelRes)
                                    )
                                },
                                label = { Text(stringResource(item.labelRes)) },
                                colors = NavigationRailItemDefaults.colors(
                                    selectedIconColor = MaterialTheme.colorScheme.primary,
                                    selectedTextColor = MaterialTheme.colorScheme.primary,
                                    indicatorColor = MaterialTheme.colorScheme.primaryContainer
                                ),
                                modifier = Modifier.testTag(item.testTag)
                            )
                        }
                    }
                }

                Surface(
                    color = MaterialTheme.colorScheme.background,
                    modifier = Modifier.fillMaxSize()
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = Routes.HOME,
                        modifier = Modifier
                            .fillMaxSize()
                            .widthIn(max = 840.dp)
                            .align(Alignment.CenterVertically)
                    ) {
                        composable(Routes.HOME) {
                            if (uiState.isCaregiverMode) {
                                val profile = uiState.profile
                                CaregiverHomeScreen(
                                    uiState = uiState,
                                    onOpenNotifications = {
                                        navController.navigate(Routes.NOTIFICATIONS)
                                    },
                                    onDetectLocationSuccess = viewModel::applyDetectedLocation,
                                    onLocationError = viewModel::showMessage,
                                    onToggleAvailability = { available ->
                                        if (profile != null) {
                                            viewModel.updateCaregiverShowcase(
                                                headline = profile.headline,
                                                specialties = profile.specialtiesList,
                                                pricePerSession = profile.pricePerSession,
                                                priceUnit = profile.priceUnit,
                                                experienceYears = profile.experienceYears,
                                                highlights = profile.highlightsList,
                                                bio = profile.bio,
                                                isAvailableForClients = available
                                            )
                                        }
                                    },
                                    onNavigateToProfile = { navigateToTopTab(Routes.PROFILE) },
                                    onNavigateToClients = { navigateToTopTab(Routes.CLIENTS) },
                                    onNavigateToAgenda = { navigateToTopTab(Routes.AGENDA) },
                                    onNavigateToChat = { navigateToTopTab(Routes.CHAT) },
                                    onAcceptBooking = { booking ->
                                        viewModel.updateBookingStatus(booking.id, BookingStatus.AGENDADO)
                                    },
                                    onUpdateBookingStatus = { booking, newStatus ->
                                        viewModel.updateBookingStatus(booking.id, newStatus)
                                    },
                                    onRequestCancelBooking = { booking ->
                                        bookingToCancelWithReason = booking
                                    },
                                    onOpenClientChat = { booking ->
                                        navController.navigate(
                                            Routes.caregiverChatDetail(booking.userId, booking.caregiverId)
                                        )
                                    },
                                    onBookingClick = { booking ->
                                        selectedBookingForAction = booking
                                    },
                                    onSimulateClientRequest = viewModel::simulateClientHireRequest,
                                    modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars)
                                )
                            } else {
                                HomeScreen(
                                    uiState = uiState,
                                    onSearchQueryChange = viewModel::onSearchQueryChange,
                                    onSelectServiceFilter = viewModel::onServiceFilterSelected,
                                    onClearFilters = viewModel::clearHomeFilters,
                                    onCaregiverClick = { caregiver ->
                                        selectedCaregiverForSheet = caregiver
                                    },
                                    onBookCaregiver = { caregiver ->
                                        preselectedCaregiverForBooking = caregiver
                                        showNewBookingSheet = true
                                    },
                                    onOpenNewBooking = {
                                        preselectedCaregiverForBooking = null
                                        showNewBookingSheet = true
                                    },
                                    onBookingClick = { booking ->
                                        selectedBookingForAction = booking
                                    },
                                    onSeeAllBookings = {
                                        navigateToTopTab(Routes.AGENDA)
                                    },
                                    onOpenNotifications = {
                                        navController.navigate(Routes.NOTIFICATIONS)
                                    },
                                    onDetectLocationSuccess = viewModel::applyDetectedLocation,
                                    onLocationError = viewModel::showMessage,
                                    modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars)
                                )
                            }
                        }

                        composable(Routes.CLIENTS) {
                            CaregiverClientsScreen(
                                uiState = uiState,
                                onAcceptBooking = { booking ->
                                    viewModel.updateBookingStatus(booking.id, BookingStatus.AGENDADO)
                                },
                                onUpdateBookingStatus = { booking, newStatus ->
                                    viewModel.updateBookingStatus(booking.id, newStatus)
                                },
                                onRequestCancelBooking = { booking ->
                                    bookingToCancelWithReason = booking
                                },
                                onOpenClientChat = { booking ->
                                    navController.navigate(
                                        Routes.caregiverChatDetail(booking.userId, booking.caregiverId)
                                    )
                                },
                                onBookingClick = { booking ->
                                    selectedBookingForAction = booking
                                },
                                onSimulateClientRequest = viewModel::simulateClientHireRequest,
                                modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars)
                            )
                        }

                        composable(Routes.PETS) {
                            PetsScreen(
                                pets = uiState.pets,
                                onAddPetClick = {
                                    navController.navigate(Routes.ADD_PET)
                                },
                                onEditPetClick = { pet ->
                                    navController.navigate(Routes.editPet(pet.id))
                                },
                                onDeletePetClick = viewModel::deletePet,
                                modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars)
                            )
                        }

                        composable(
                            route = Routes.EDIT_PET,
                            arguments = listOf(
                                navArgument("petId") {
                                    type = NavType.IntType
                                    defaultValue = -1
                                }
                            )
                        ) { backStackEntry ->
                            val petId = backStackEntry.arguments?.getInt("petId") ?: -1
                            val existingPet = if (petId > 0) viewModel.getPetById(petId) else null
                            AddEditPetScreen(
                                existingPet = existingPet,
                                onBack = { navController.popBackStack() },
                                onSavePet = viewModel::savePet
                            )
                        }

                        composable(Routes.AGENDA) {
                            AgendaScreen(
                                uiState = uiState,
                                onRefresh = viewModel::refreshAgenda,
                                onSelectDay = viewModel::onSelectCalendarDay,
                                onShowAll = viewModel::onShowAllDates,
                                onSelectStatusFilter = viewModel::onSelectStatusFilter,
                                onBookingClick = { booking ->
                                    selectedBookingForAction = booking
                                },
                                onNewBookingClick = {
                                    preselectedCaregiverForBooking = null
                                    showNewBookingSheet = true
                                },
                                modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars)
                            )
                        }

                        composable(Routes.CHAT) {
                            if (uiState.isCaregiverMode) {
                                CaregiverChatListScreen(
                                    threads = uiState.clientChatThreads,
                                    onOpenClientThread = { thread ->
                                        navController.navigate(
                                            Routes.caregiverChatDetail(thread.clientUserId, thread.caregiverId)
                                        )
                                    },
                                    onSimulateClientRequest = viewModel::simulateClientHireRequest,
                                    modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars)
                                )
                            } else {
                                ChatListScreen(
                                    uiState = uiState,
                                    onOpenConversation = { caregiver ->
                                        navController.navigate(Routes.chatDetail(caregiver.id))
                                    },
                                    modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars)
                                )
                            }
                        }

                        composable(
                            route = Routes.CHAT_DETAIL,
                            arguments = listOf(
                                navArgument("caregiverId") { type = NavType.StringType }
                            )
                        ) { backStackEntry ->
                            val caregiverId = backStackEntry.arguments?.getString("caregiverId") ?: ""
                            val caregiver = viewModel.getCaregiverById(caregiverId)
                            if (caregiver != null) {
                                val messages by viewModel
                                    .observeMessagesForCaregiver(caregiverId)
                                    .collectAsStateWithLifecycle(initialValue = emptyList())
                                ChatDetailScreen(
                                    headerName = caregiver.name,
                                    headerInitials = caregiver.initials,
                                    headerSubtitle = if (caregiver.verified) {
                                        "Cuidador Verificado • ${caregiver.responseTime}"
                                    } else {
                                        caregiver.roleTitle
                                    },
                                    avatarTone = caregiver.avatarTone,
                                    messages = messages,
                                    viewerIsCaregiver = false,
                                    onBack = { navController.popBackStack() },
                                    onSendMessage = { text, photoPath ->
                                        viewModel.sendMessage(caregiverId, text, photoPath)
                                    }
                                )
                            }
                        }

                        composable(
                            route = Routes.CAREGIVER_CHAT_DETAIL,
                            arguments = listOf(
                                navArgument("clientUserId") { type = NavType.IntType },
                                navArgument("caregiverId") { type = NavType.StringType }
                            )
                        ) { backStackEntry ->
                            val clientUserId = backStackEntry.arguments?.getInt("clientUserId") ?: 0
                            val caregiverId = backStackEntry.arguments?.getString("caregiverId") ?: ""
                            val thread = uiState.clientChatThreads.firstOrNull {
                                it.clientUserId == clientUserId && it.caregiverId == caregiverId
                            }
                            val relatedBooking = uiState.bookings.firstOrNull {
                                it.userId == clientUserId && it.caregiverId == caregiverId
                            }
                            val clientName = thread?.clientName
                                ?: relatedBooking?.clientName?.ifBlank { null }
                                ?: "Cliente"
                            val clientInitials = clientName
                                .trim()
                                .split(" ")
                                .filter { it.isNotBlank() }
                                .take(2)
                                .joinToString("") { it.take(1).uppercase() }
                                .ifBlank { "CL" }
                            val subtitle = relatedBooking?.let {
                                "${it.serviceName} • Pet: ${it.petName}"
                            } ?: "Conversa com cliente"
                            val messages by viewModel
                                .observeMessagesForClientThread(clientUserId, caregiverId)
                                .collectAsStateWithLifecycle(initialValue = emptyList())

                            ChatDetailScreen(
                                headerName = clientName,
                                headerInitials = clientInitials,
                                headerSubtitle = subtitle,
                                avatarTone = MaterialTheme.colorScheme.primaryContainer,
                                messages = messages,
                                viewerIsCaregiver = true,
                                onBack = { navController.popBackStack() },
                                onSendMessage = { text, photoPath ->
                                    viewModel.sendCaregiverMessage(
                                        clientUserId = clientUserId,
                                        caregiverId = caregiverId,
                                        clientName = clientName,
                                        rawText = text,
                                        imagePath = photoPath
                                    )
                                }
                            )
                        }

                        composable(Routes.PROFILE) {
                            ProfileScreen(
                                uiState = uiState,
                                onUpdateAvatar = viewModel::updateProfileAvatar,
                                onUpdateBanner = viewModel::updateProfileBanner,
                                onUpdateProfileInfo = viewModel::updateProfileInfo,
                                onDetectLocationSuccess = viewModel::applyDetectedLocation,
                                onLocationError = viewModel::showMessage,
                                onToggleNotifications = viewModel::toggleNotifications,
                                onNavigateToPets = { navigateToTopTab(Routes.PETS) },
                                onNavigateToAgenda = { navigateToTopTab(Routes.AGENDA) },
                                onOpenNotifications = {
                                    navController.navigate(Routes.NOTIFICATIONS)
                                },
                                onLogout = viewModel::logout,
                                onUpdateCaregiverShowcase = viewModel::updateCaregiverShowcase,
                                onAddCertificate = viewModel::addCaregiverCertificate,
                                onRemoveCertificate = viewModel::removeCaregiverCertificate,
                                onNavigateToClients = { navigateToTopTab(Routes.CLIENTS) },
                                modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars)
                            )
                        }

                        composable(Routes.NOTIFICATIONS) {
                            NotificationsScreen(
                                notifications = uiState.notifications,
                                onBack = { navController.popBackStack() },
                                onNotificationClick = { notification ->
                                    viewModel.markNotificationAsRead(notification.id)
                                    val target = notification.relatedRoute
                                    if (!target.isNullOrBlank()) {
                                        navController.popBackStack()
                                        when {
                                            target in setOf(
                                                Routes.HOME,
                                                Routes.PETS,
                                                Routes.CLIENTS,
                                                Routes.AGENDA,
                                                Routes.CHAT,
                                                Routes.PROFILE
                                            ) -> navigateToTopTab(target)
                                            target.startsWith("chat_detail/") ->
                                                navController.navigate(target)
                                        }
                                    }
                                },
                                onMarkAllRead = viewModel::markAllNotificationsAsRead,
                                onClearRead = viewModel::clearReadNotifications
                            )
                        }
                    }
                }
            }
        }
    }

    selectedCaregiverForSheet?.let { caregiver ->
        CaregiverDetailBottomSheet(
            caregiver = caregiver,
            onDismiss = { selectedCaregiverForSheet = null },
            onBookNow = { chosen ->
                selectedCaregiverForSheet = null
                preselectedCaregiverForBooking = chosen
                showNewBookingSheet = true
            },
            onChatNow = { chosen ->
                selectedCaregiverForSheet = null
                navController.navigate(Routes.chatDetail(chosen.id))
            }
        )
    }

    if (showNewBookingSheet) {
        NewBookingBottomSheet(
            caregivers = uiState.caregivers,
            pets = uiState.pets,
            initialCaregiver = preselectedCaregiverForBooking,
            initialService = uiState.selectedServiceFilter,
            onDismiss = { showNewBookingSheet = false },
            onConfirmBooking = { caregiver, pet, serviceName, dayOption, timeLabel, notes ->
                viewModel.createBooking(caregiver, pet, serviceName, dayOption, timeLabel, notes)
                showNewBookingSheet = false
            },
            onNavigateToAddPet = {
                showNewBookingSheet = false
                navController.navigate(Routes.ADD_PET)
            }
        )
    }

    selectedBookingForAction?.let { booking ->
        val freshBooking = uiState.bookings.firstOrNull { it.id == booking.id } ?: booking
        if (uiState.isCaregiverMode) {
            CaregiverBookingDetailBottomSheet(
                booking = freshBooking,
                onDismiss = { selectedBookingForAction = null },
                onChangeStatus = { newStatus ->
                    viewModel.updateBookingStatus(freshBooking.id, newStatus)
                },
                onRequestCancelWithReason = { target ->
                    selectedBookingForAction = null
                    bookingToCancelWithReason = target
                },
                onOpenChat = { target ->
                    selectedBookingForAction = null
                    navController.navigate(
                        Routes.caregiverChatDetail(target.userId, target.caregiverId)
                    )
                }
            )
        } else {
            BookingDetailBottomSheet(
                booking = freshBooking,
                onDismiss = { selectedBookingForAction = null },
                onChangeStatus = { newStatus ->
                    viewModel.updateBookingStatus(freshBooking.id, newStatus)
                },
                onOpenChat = { caregiverId ->
                    selectedBookingForAction = null
                    navController.navigate(Routes.chatDetail(caregiverId))
                },
                onDeleteBooking = {
                    viewModel.deleteBooking(freshBooking.id)
                    selectedBookingForAction = null
                }
            )
        }
    }

    bookingToCancelWithReason?.let { target ->
        CancelBookingReasonDialog(
            booking = target,
            onDismiss = { bookingToCancelWithReason = null },
            onConfirmCancel = { reason ->
                viewModel.cancelBookingWithReason(target.id, reason)
                bookingToCancelWithReason = null
            }
        )
    }
}

@Composable
private fun PetCareBottomBar(
    items: List<BottomNavItem>,
    currentRoute: String?,
    upcomingBookingsCount: Int,
    pendingClientsCount: Int = 0,
    onNavigate: (String) -> Unit
) {
    Surface(
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.55f)),
        shadowElevation = 6.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 8.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                val selected = currentRoute == item.route
                val tint by animateColorAsState(
                    targetValue = if (selected) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
                    animationSpec = tween(durationMillis = 180),
                    label = "bottom_nav_tint"
                )
                val indicatorWidth by animateDpAsState(
                    targetValue = if (selected) 28.dp else 0.dp,
                    animationSpec = tween(durationMillis = 200),
                    label = "bottom_nav_indicator"
                )

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .minimumInteractiveComponentSize()
                        .clip(RoundedCornerShape(14.dp))
                        .clickable { onNavigate(item.route) }
                        .padding(vertical = 4.dp)
                        .testTag(item.testTag),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    val badgeCount = when (item.route) {
                        Routes.AGENDA -> upcomingBookingsCount
                        Routes.CLIENTS -> pendingClientsCount
                        else -> 0
                    }
                    if (badgeCount > 0) {
                        BadgedBox(
                            badge = {
                                Badge(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    contentColor = MaterialTheme.colorScheme.onPrimary
                                ) {
                                    Text(
                                        text = if (badgeCount > 9) "9+" else badgeCount.toString(),
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                }
                            }
                        ) {
                            Icon(
                                imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                                contentDescription = stringResource(item.labelRes),
                                tint = tint,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    } else {
                        Icon(
                            imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                            contentDescription = stringResource(item.labelRes),
                            tint = tint,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Text(
                        text = stringResource(item.labelRes),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                        color = tint,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Box(
                        modifier = Modifier
                            .width(indicatorWidth)
                            .height(3.dp)
                            .clip(RoundedCornerShape(50))
                            .background(MaterialTheme.colorScheme.primary)
                    )
                }
            }
        }
    }
}
