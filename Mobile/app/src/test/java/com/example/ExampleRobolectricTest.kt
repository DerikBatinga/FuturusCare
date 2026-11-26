package com.example

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTextInput
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.PetCareDatabase
import com.example.data.model.AppNotificationEntity
import com.example.data.model.BookingEntity
import com.example.data.model.BookingStatus
import com.example.data.model.CaregiverMockData
import com.example.data.model.NotificationType
import com.example.data.model.TutorProfileEntity
import com.example.data.repository.PetCareRepository
import com.example.ui.PetCareUiState
import com.example.ui.screens.AddEditPetScreen
import com.example.ui.screens.AgendaScreen
import com.example.ui.screens.CancelBookingReasonDialog
import com.example.ui.screens.CaregiverHomeScreen
import com.example.ui.screens.ChatDetailScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.IntroScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.NotificationsScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.RegisterScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.theme.CaregiverToneSky
import com.example.ui.theme.PetCareTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `app name matches FuturusCare and original logo drawables exist`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("FuturusCare", appName)
        assertTrue(R.drawable.logo_simbolo != 0)
        assertTrue(R.drawable.logo != 0)
        assertTrue(R.drawable.icone_frente != 0)
        assertTrue(R.drawable.login_hero != 0)
        assertTrue(R.drawable.intro_dog != 0)
        assertTrue(R.drawable.intro_pets != 0)
        assertTrue(R.drawable.ic_notification_paw != 0)
    }

    @Test
    fun `home screen shows expanded mocked caregivers`() {
        val uiState = PetCareUiState(
            caregivers = CaregiverMockData.caregivers,
            services = CaregiverMockData.services,
            isReady = true
        )
        composeTestRule.setContent {
            PetCareTheme {
                HomeScreen(
                    uiState = uiState,
                    onSearchQueryChange = {},
                    onSelectServiceFilter = {},
                    onClearFilters = {},
                    onCaregiverClick = {},
                    onBookCaregiver = {},
                    onOpenNewBooking = {},
                    onBookingClick = {},
                    onSeeAllBookings = {}
                )
            }
        }
        composeTestRule.waitForIdle()

        val expectedCaregivers = listOf(
            "cg_lucia" to "Lucia Caminos",
            "cg_jason" to "Jason Duval",
            "cg_sabrina" to "Sabrina Carpenter",
            "cg_benito" to "Benito Antonio",
            "cg_derik" to "Derik"
        )
        expectedCaregivers.forEach { (id, name) ->
            composeTestRule.onNodeWithTag("home_list")
                .performScrollToNode(hasTestTag("caregiver_card_$id"))
            composeTestRule.onNodeWithTag("caregiver_card_$id").assertIsDisplayed()
            composeTestRule.onNodeWithText(name).assertIsDisplayed()
        }
    }

    @Test
    fun `agenda shows status inside booking card without separated status sections`() {
        val sampleBooking = BookingEntity(
            id = 42,
            userId = 1,
            caregiverId = "cg_derik",
            caregiverName = "Derik",
            caregiverInitials = "D",
            petId = 1,
            petName = "Thor",
            serviceName = "Dog Walking",
            dateIso = "2026-11-24",
            dateLabel = "24/11",
            timeLabel = "11:00",
            status = BookingStatus.AGENDADO,
            priceReais = 45.0
        )
        val uiState = PetCareUiState(
            bookings = listOf(sampleBooking),
            isReady = true
        )
        composeTestRule.setContent {
            PetCareTheme {
                AgendaScreen(
                    uiState = uiState,
                    onRefresh = {},
                    onSelectDay = {},
                    onShowAll = {},
                    onSelectStatusFilter = {},
                    onBookingClick = {},
                    onNewBookingClick = {}
                )
            }
        }
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("agenda_list")
            .performScrollToNode(hasTestTag("booking_card_42"))
        composeTestRule.onNodeWithTag("booking_card_42").assertIsDisplayed()
        composeTestRule.onNodeWithTag("booking_status_badge_42", useUnmergedTree = true)
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("24/11 às 11:00", useUnmergedTree = true)
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("Nenhum agendamento neste estado.").assertDoesNotExist()
    }

    @Test
    fun `profile action buttons do not overlap profile avatar`() {
        val uiState = PetCareUiState(
            profile = TutorProfileEntity(
                userId = 1,
                name = "Derik Silva",
                email = "derik@futuruscare.app",
                phone = "(11) 96333-2972",
                address = "Rua São João, 120",
                city = "São Paulo",
                neighborhood = "Pinheiros",
                bio = "Tutor de cães e gatos"
            ),
            isReady = true
        )
        composeTestRule.setContent {
            PetCareTheme {
                ProfileScreen(
                    uiState = uiState,
                    onUpdateAvatar = {},
                    onUpdateBanner = {},
                    onUpdateProfileInfo = { _, _, _, _, _, _, _ -> },
                    onToggleNotifications = {}
                )
            }
        }
        composeTestRule.waitForIdle()

        val avatarBounds = composeTestRule.onNodeWithTag("profile_avatar_image")
            .fetchSemanticsNode().boundsInRoot
        val photoBtnBounds = composeTestRule.onNodeWithTag("change_photo_button")
            .fetchSemanticsNode().boundsInRoot
        val bannerBtnBounds = composeTestRule.onNodeWithTag("change_banner_button")
            .fetchSemanticsNode().boundsInRoot

        assertTrue("Alterar foto button should appear cleanly below the avatar", photoBtnBounds.top >= avatarBounds.bottom)
        assertTrue("Alterar banner button should appear cleanly below the avatar", bannerBtnBounds.top >= avatarBounds.bottom)
    }

    @Test
    fun `verified badge stays hidden when avatar or info is missing`() {
        val incompleteProfile = TutorProfileEntity(
            userId = 1,
            name = "Derik Silva",
            email = "derik@futuruscare.app",
            phone = "(11) 96333-2972",
            address = "Rua São João, 120",
            city = "São Paulo",
            neighborhood = "Pinheiros",
            bio = "Tutor apaixonado",
            avatarPath = null
        )
        assertFalse(incompleteProfile.isVerified)

        val uiState = PetCareUiState(profile = incompleteProfile, isReady = true)
        composeTestRule.setContent {
            PetCareTheme {
                ProfileScreen(
                    uiState = uiState,
                    onUpdateAvatar = {},
                    onUpdateBanner = {},
                    onUpdateProfileInfo = { _, _, _, _, _, _, _ -> },
                    onToggleNotifications = {}
                )
            }
        }
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("verified_badge_icon", useUnmergedTree = true).assertDoesNotExist()
        composeTestRule.onNodeWithTag("profile_list")
            .performScrollToNode(hasTestTag("verification_progress_card"))
        composeTestRule.onNodeWithTag("verification_progress_card").assertIsDisplayed()
    }

    @Test
    fun `verified badge appears when avatar photo and all fields are completed`() {
        val completeProfile = TutorProfileEntity(
            userId = 1,
            name = "Derik Silva",
            email = "derik@futuruscare.app",
            phone = "(11) 96333-2972",
            address = "Rua São João, 120",
            city = "São Paulo",
            neighborhood = "Pinheiros",
            bio = "Tutor dedicado ao bem-estar do pet",
            avatarPath = "local/avatar_user.jpg"
        )
        assertTrue(completeProfile.isVerified)

        val uiState = PetCareUiState(profile = completeProfile, isReady = true)
        composeTestRule.setContent {
            PetCareTheme {
                ProfileScreen(
                    uiState = uiState,
                    onUpdateAvatar = {},
                    onUpdateBanner = {},
                    onUpdateProfileInfo = { _, _, _, _, _, _, _ -> },
                    onToggleNotifications = {}
                )
            }
        }
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("verified_badge_icon", useUnmergedTree = true).assertIsDisplayed()
        composeTestRule.onNodeWithTag("verification_progress_card").assertDoesNotExist()
    }

    @Test
    fun `add pet screen renders camera icon and submits pet`() {
        var savedName = ""
        composeTestRule.setContent {
            PetCareTheme {
                AddEditPetScreen(
                    existingPet = null,
                    onBack = {},
                    onSavePet = { _, name, _, _, _, _, _, _ ->
                        savedName = name
                    }
                )
            }
        }
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("pet_photo_picker_circle").assertIsDisplayed()
        composeTestRule.onNodeWithTag("pet_name_input").performTextInput("Thor")
        composeTestRule.onNodeWithTag("submit_pet_button").performScrollTo().performClick()
        composeTestRule.waitForIdle()
        assertEquals("Thor", savedName)
    }

    @Test
    fun `splash and intro screens display original branding and start button`() {
        composeTestRule.setContent {
            PetCareTheme {
                SplashScreen(onFinished = {})
            }
        }
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("splash_screen").assertIsDisplayed()
        composeTestRule.onNodeWithText("Cuidando de quem você mais ama").assertIsDisplayed()
    }

    @Test
    fun `intro screen displays welcome and start button`() {
        var started = false
        composeTestRule.setContent {
            PetCareTheme {
                IntroScreen(
                    onStartRegister = { started = true },
                    onGoToLogin = {}
                )
            }
        }
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("intro_screen").assertIsDisplayed()
        composeTestRule.onNodeWithText("Bem-vindo(a)!").assertIsDisplayed()
        composeTestRule.onNodeWithTag("intro_start_button").performClick()
        assertTrue(started)
    }

    @Test
    fun `login screen displays original card, toggles and inputs`() {
        var registerClicked = false
        composeTestRule.setContent {
            PetCareTheme {
                LoginScreen(
                    authError = null,
                    isLoading = false,
                    onLoginClick = { _, _, _ -> },
                    onNavigateToRegister = { registerClicked = true },
                    onClearError = {}
                )
            }
        }
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("futurus_brand_logo").assertIsDisplayed()
        composeTestRule.onNodeWithText("Acesse sua Conta").assertIsDisplayed()
        composeTestRule.onNodeWithTag("login_client_tab").assertIsDisplayed()
        composeTestRule.onNodeWithTag("login_sitter_tab").assertIsDisplayed()
        composeTestRule.onNodeWithTag("login_email_input").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithTag("login_password_input").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithTag("go_to_register_button").performScrollTo().performClick()
        assertTrue(registerClicked)
    }

    @Test
    fun `register screen displays original tabs, photo picker and auto location button`() {
        composeTestRule.setContent {
            PetCareTheme {
                RegisterScreen(
                    authError = null,
                    isLoading = false,
                    onBackToLogin = {},
                    onRegisterClick = { _, _, _, _, _, _, _, _, _, _, _, _, _ -> },
                    onClearError = {}
                )
            }
        }
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("register_client_tab").assertIsDisplayed()
        composeTestRule.onNodeWithTag("register_caregiver_tab").assertIsDisplayed()
        composeTestRule.onNodeWithTag("register_photo_picker").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithTag("register_name_input").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithTag("register_detect_location_button").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithTag("register_terms_checkbox").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithTag("register_read_terms_button").performScrollTo().performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Termos de Serviço e Privacidade").assertIsDisplayed()
        composeTestRule.onNodeWithText("Aceitar e continuar").performClick()
        composeTestRule.waitForIdle()
    }

    @Test
    fun `notifications screen shows unread alerts and categories`() {
        val sampleNotifications = listOf(
            AppNotificationEntity(
                id = 1,
                userId = 1,
                title = "Agendamento confirmado!",
                body = "Lucia Caminos vai passear com Thor.",
                type = NotificationType.BOOKING_CREATED,
                isRead = false,
                relatedRoute = "agenda",
                timeLabel = "14:20"
            )
        )
        composeTestRule.setContent {
            PetCareTheme {
                NotificationsScreen(
                    notifications = sampleNotifications,
                    onBack = {},
                    onNotificationClick = {},
                    onMarkAllRead = {},
                    onClearRead = {}
                )
            }
        }
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("notification_card_1").assertIsDisplayed()
        composeTestRule.onNodeWithText("Agendamento confirmado!").assertIsDisplayed()
    }

    @Test
    fun `new registered account starts with zero pets and zero bookings and supports login`() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val db = Room.inMemoryDatabaseBuilder(context, PetCareDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        try {
            val repository = PetCareRepository(
                userAccountDao = db.userAccountDao(),
                sessionDao = db.sessionDao(),
                petDao = db.petDao(),
                bookingDao = db.bookingDao(),
                chatDao = db.chatDao(),
                profileDao = db.profileDao(),
                certificateDao = db.certificateDao(),
                notificationDao = db.notificationDao()
            )

            val registerResult = repository.registerAccount(
                name = "Derik",
                email = "derik@futuruscare.app",
                password = "password123",
                phone = "11963332972",
                address = "Rua São João",
                city = "São Paulo",
                neighborhood = "Centro",
                bio = "",
                avatarPath = null
            )
            assertTrue(registerResult.isSuccess)
            val userId = registerResult.getOrThrow()

            val pets = repository.observePets(userId).first()
            val bookings = repository.observeBookings(userId).first()
            val profile = repository.observeProfile(userId).first()
            val notifications = repository.observeNotifications(userId).first()

            assertTrue("New account must start with 0 pets", pets.isEmpty())
            assertTrue("New account must start with 0 bookings", bookings.isEmpty())
            assertFalse("New account without photo/bio must not be verified yet", profile!!.isVerified)
            assertEquals(1, notifications.size)

            repository.logout()
            val wrongRoleLogin = repository.loginAccount("derik@futuruscare.app", "password123", expectedCaregiver = true)
            assertTrue("Client account must reject PetSitter role mismatch", wrongRoleLogin.isFailure)

            val badLogin = repository.loginAccount("derik@futuruscare.app", "wrongpass", expectedCaregiver = false)
            assertTrue(badLogin.isFailure)

            val goodLogin = repository.loginAccount("derik@futuruscare.app", "password123", expectedCaregiver = false)
            assertTrue(goodLogin.isSuccess)
            assertEquals(userId, goodLogin.getOrThrow())
        } finally {
            db.close()
        }
    }

    @Test
    fun `caregiver verified badge requires certificate in addition to photo and profile info`() {
        val caregiverWithoutCert = TutorProfileEntity(
            userId = 2,
            name = "Lucia Caminos",
            email = "lucia@futuruscare.app",
            phone = "(11) 99888-7766",
            address = "Rua das Flores, 50",
            city = "São Paulo",
            neighborhood = "Vila Mariana",
            bio = "Especialista em comportamento canino",
            isCaregiver = true,
            specialty = "Dog Walking",
            specialtiesCsv = "Dog Walking,Pet Sitting",
            pricePerSession = 60.0,
            experienceYears = 5,
            certificateCount = 0,
            avatarPath = "local/avatar_lucia.jpg"
        )
        assertFalse("Caregiver without certificate must not be verified", caregiverWithoutCert.isVerified)
        assertTrue(caregiverWithoutCert.missingVerificationItems.contains("Anexar certificado"))

        val caregiverWithCert = caregiverWithoutCert.copy(certificateCount = 1)
        assertTrue("Caregiver with photo, fields, and certificate must be verified", caregiverWithCert.isVerified)
    }

    @Test
    fun `caregiver dashboard shows certificates card and chat screen exposes photo attachment button`() {
        val caregiverProfile = TutorProfileEntity(
            userId = 3,
            name = "Jason Duval",
            email = "jason@futuruscare.app",
            phone = "(11) 98888-1122",
            address = "Av. Paulista, 900",
            city = "São Paulo",
            neighborhood = "Bela Vista",
            bio = "Cuidador dedicado",
            isCaregiver = true,
            specialty = "Hospedagem",
            certificateCount = 0
        )
        val uiState = PetCareUiState(
            activeUserId = 3,
            profile = caregiverProfile,
            isReady = true
        )
        composeTestRule.setContent {
            PetCareTheme {
                CaregiverHomeScreen(
                    uiState = uiState,
                    onOpenNotifications = {},
                    onDetectLocationSuccess = {},
                    onLocationError = {},
                    onToggleAvailability = {},
                    onNavigateToProfile = {},
                    onNavigateToClients = {},
                    onNavigateToAgenda = {},
                    onNavigateToChat = {},
                    onAcceptBooking = {},
                    onUpdateBookingStatus = { _, _ -> },
                    onRequestCancelBooking = {},
                    onOpenClientChat = {},
                    onBookingClick = {},
                    onSimulateClientRequest = {}
                )
            }
        }
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("caregiver_verify_banner").assertIsDisplayed()
        composeTestRule.onNodeWithTag("caregiver_home_simulate_button").performScrollTo().assertIsDisplayed()
    }

    @Test
    fun `chat detail screen displays photo attachment button and cancel dialog captures reason`() {
        var submittedReason = ""
        val sampleBooking = BookingEntity(
            id = 99,
            userId = 1,
            caregiverId = "local_user_3",
            caregiverUserId = 3,
            caregiverName = "Jason Duval",
            caregiverInitials = "JD",
            clientName = "Mariana Costa",
            petId = 1,
            petName = "Pipoca",
            serviceName = "Dog Walking",
            dateIso = "2026-11-25",
            dateLabel = "25/11",
            timeLabel = "10:00",
            status = BookingStatus.PENDENTE
        )
        composeTestRule.setContent {
            PetCareTheme {
                ChatDetailScreen(
                    headerName = "Lucia Caminos",
                    headerInitials = "LC",
                    headerSubtitle = "Cuidador Verificado",
                    avatarTone = CaregiverToneSky,
                    messages = emptyList(),
                    onBack = {},
                    onSendMessage = { _, _ -> }
                )
                CancelBookingReasonDialog(
                    booking = sampleBooking,
                    onDismiss = {},
                    onConfirmCancel = { submittedReason = it }
                )
            }
        }
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("chat_attach_photo_button").assertExists()
        composeTestRule.onNodeWithTag("cancel_reason_input").performTextInput("Imprevisto médico hoje")
        composeTestRule.onNodeWithTag("confirm_cancel_with_reason_button").performClick()
        assertEquals("Imprevisto médico hoje", submittedReason)
    }
}

