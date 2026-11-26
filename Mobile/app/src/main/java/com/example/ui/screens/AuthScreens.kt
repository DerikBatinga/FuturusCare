package com.example.ui.screens

import android.Manifest
import android.util.Patterns
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.AddAPhoto
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.Description
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.MyLocation
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.BuildConfig
import com.example.R
import com.example.ui.theme.IntroDot
import com.example.ui.theme.IntroInactive
import com.example.ui.theme.IntroTitle
import com.example.ui.theme.IntroValues
import com.example.ui.theme.OriginalCream
import com.example.ui.theme.OriginalLoginCard
import com.example.ui.theme.OriginalViolet
import com.example.util.ImageStorageHelper
import com.example.util.LocationHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun FuturusLogoBadge(
    logoSize: Dp = 84.dp,
    showTagline: Boolean = true,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_simbolo),
            contentDescription = stringResource(R.string.cd_app_logo),
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(logoSize)
                .testTag("futurus_brand_logo")
        )

        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = stringResource(R.string.app_name),
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .widthIn(max = 220.dp)
                .height(34.dp)
        )

        if (showTagline) {
            Text(
                text = "Cuidando de quem você mais ama",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun SplashScreen(
    onFinished: () -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(Unit) {
        delay(1300L)
        onFinished()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(OriginalCream)
            .clickable { onFinished() }
            .statusBarsPadding()
            .navigationBarsPadding()
            .testTag("splash_screen"),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_simbolo),
                contentDescription = stringResource(R.string.cd_app_logo),
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(width = 76.dp, height = 94.dp)
                    .testTag("futurus_brand_logo")
            )

            Spacer(modifier = Modifier.height(26.dp))

            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = stringResource(R.string.app_name),
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth(0.88f)
                    .height(52.dp)
            )

            Spacer(modifier = Modifier.height(26.dp))

            Text(
                text = "Cuidando de quem você mais ama",
                style = MaterialTheme.typography.titleMedium,
                fontSize = 19.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(84.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(3) { idx ->
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(if (idx == 0) OriginalViolet else IntroDot)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "v${BuildConfig.VERSION_NAME}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun IntroScreen(
    onStartRegister: () -> Unit,
    onGoToLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 2 })
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(OriginalCream)
            .statusBarsPadding()
            .navigationBarsPadding()
            .testTag("intro_screen")
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 540.dp)
                .fillMaxSize()
                .align(Alignment.Center)
                .padding(horizontal = 16.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = stringResource(R.string.app_name),
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth(0.86f)
                    .height(48.dp)
                    .testTag("futurus_brand_logo")
            )

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) { page ->
                if (page == 0) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Cuidando de quem você mais ama",
                            style = MaterialTheme.typography.bodyLarge,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(28.dp))

                        Text(
                            text = "Bem-vindo(a)!",
                            style = MaterialTheme.typography.headlineLarge,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold,
                            color = IntroTitle,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Image(
                            painter = painterResource(id = R.drawable.intro_dog),
                            contentDescription = "Cachorro usando fones de ouvido",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .padding(horizontal = 6.dp)
                        )
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 12.dp, start = 8.dp, end = 8.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = "Nossos valores",
                            style = MaterialTheme.typography.headlineLarge,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold,
                            color = OriginalViolet
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Segurança\nCuidado\nAmor",
                            style = MaterialTheme.typography.headlineSmall,
                            fontSize = 24.sp,
                            lineHeight = 30.sp,
                            color = IntroValues
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Image(
                            painter = painterResource(id = R.drawable.intro_pets),
                            contentDescription = "Gato ao lado de um cachorro",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(2) { idx ->
                    val active = pagerState.currentPage == idx
                    Box(
                        modifier = Modifier
                            .size(width = 31.dp, height = 10.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (active) OriginalViolet else IntroInactive)
                            .clickable {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(idx)
                                }
                            }
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Button(
                onClick = onStartRegister,
                shape = RoundedCornerShape(25.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = OriginalViolet,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 6.dp)
                    .height(60.dp)
                    .testTag("intro_start_button")
            ) {
                Text(
                    text = "Começar",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            TextButton(
                onClick = onGoToLogin,
                modifier = Modifier.testTag("intro_login_button")
            ) {
                Text(
                    text = "Já tem uma conta? Faça login",
                    color = OriginalViolet,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
fun LoginScreen(
    authError: String?,
    isLoading: Boolean,
    onLoginClick: (email: String, password: String, isCaregiver: Boolean) -> Unit,
    onNavigateToRegister: () -> Unit,
    onClearError: () -> Unit,
    modifier: Modifier = Modifier
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isCaregiver by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(OriginalCream)
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 520.dp)
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 14.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Original Border Collie hero banner aligned right, sitting cleanly right above card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(248.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = stringResource(R.string.cd_app_logo),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(start = 6.dp, top = 16.dp)
                        .width(148.dp)
                        .height(30.dp)
                        .testTag("futurus_brand_logo")
                )

                Image(
                    painter = painterResource(id = R.drawable.login_hero),
                    contentDescription = stringResource(R.string.cd_hero_illustration),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(width = 228.dp, height = 248.dp)
                        .offset(y = 6.dp)
                )
            }

            // Original #FFFDEE rounded card ("Acesse sua Conta")
            Surface(
                shape = RoundedCornerShape(26.dp),
                color = OriginalLoginCard,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.45f)),
                shadowElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 22.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Acesse sua Conta",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Original "Cliente" / "PetSitter" segmented buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        OriginalAccountToggleButton(
                            label = "Cliente",
                            selected = !isCaregiver,
                            onClick = {
                                isCaregiver = false
                                if (authError != null) onClearError()
                            },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("login_client_tab")
                        )
                        OriginalAccountToggleButton(
                            label = "PetSitter",
                            selected = isCaregiver,
                            onClick = {
                                isCaregiver = true
                                if (authError != null) onClearError()
                            },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("login_sitter_tab")
                        )
                    }

                    if (!authError.isNullOrBlank()) {
                        AuthErrorBanner(message = authError)
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "E-mail",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        OutlinedTextField(
                            value = email,
                            onValueChange = {
                                email = it
                                if (authError != null) onClearError()
                            },
                            placeholder = { Text("@ Seu e-mail") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            shape = RoundedCornerShape(13.dp),
                            colors = originalFieldColors(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("login_email_input")
                        )
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "Senha",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        OutlinedTextField(
                            value = password,
                            onValueChange = {
                                password = it
                                if (authError != null) onClearError()
                            },
                            placeholder = { Text("Sua senha") },
                            trailingIcon = {
                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(
                                        imageVector = if (passwordVisible) Icons.Rounded.VisibilityOff else Icons.Rounded.Visibility,
                                        contentDescription = if (passwordVisible) "Ocultar senha" else "Mostrar senha"
                                    )
                                }
                            },
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            shape = RoundedCornerShape(13.dp),
                            colors = originalFieldColors(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("login_password_input")
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Button(
                            onClick = { onLoginClick(email, password, isCaregiver) },
                            enabled = !isLoading,
                            shape = RoundedCornerShape(15.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = OriginalViolet,
                                contentColor = Color.White
                            ),
                            modifier = Modifier
                                .widthIn(min = 164.dp)
                                .height(48.dp)
                                .testTag("login_submit_button")
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = Color.White,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Text(
                                    text = "Entrar",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    TextButton(
                        onClick = {
                            onClearError()
                            onNavigateToRegister()
                        },
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .testTag("go_to_register_button")
                    ) {
                        Text(
                            text = "Não tem conta? Cadastre-se",
                            color = OriginalViolet,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Cuidar de quem você ama ficou fácil",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun RegisterScreen(
    authError: String?,
    isLoading: Boolean,
    onBackToLogin: () -> Unit,
    onRegisterClick: (
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
    onClearError: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler(onBack = onBackToLogin)

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val specialties = remember { listOf("Dog Walking", "Pet Sitting", "Hospedagem", "Banho & Tosa") }

    var isCaregiver by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var specialty by remember { mutableStateOf(specialties.first()) }
    var specialtyMenuOpen by remember { mutableStateOf(false) }
    var experienceYears by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var neighborhood by remember { mutableStateOf("") }
    var avatarPath by remember { mutableStateOf<String?>(null) }
    var termsAccepted by remember { mutableStateOf(false) }

    var localValidationError by remember { mutableStateOf<String?>(null) }
    var showTermsDialog by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }
    var isDetectingLocation by remember { mutableStateOf(false) }
    var locationFeedback by remember { mutableStateOf<String?>(null) }

    val avatarPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            coroutineScope.launch {
                val path = ImageStorageHelper.copyUriToInternalStorage(context, uri, "avatar")
                if (path != null) avatarPath = path
            }
        }
    }

    val runAutoLocation: () -> Unit = {
        coroutineScope.launch {
            isDetectingLocation = true
            locationFeedback = null
            val res = LocationHelper.detectCurrentLocation(context)
            res.fold(
                onSuccess = { detected ->
                    address = detected.address
                    neighborhood = detected.neighborhood
                    city = detected.city
                    locationFeedback = "Localização detectada: ${detected.neighborhood}, ${detected.city}"
                },
                onFailure = { err ->
                    locationFeedback = err.message ?: "Não foi possível obter o GPS agora."
                }
            )
            isDetectingLocation = false
        }
    }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { grants ->
        val granted = grants.values.any { it }
        if (granted) {
            runAutoLocation()
        } else {
            locationFeedback = "Permissão negada. Você pode preencher seu endereço manualmente."
        }
    }

    val effectiveError = localValidationError ?: authError

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(OriginalCream)
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 560.dp)
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Top bar with Back button + Original Logo
            Box(modifier = Modifier.fillMaxWidth()) {
                IconButton(
                    onClick = {
                        onClearError()
                        onBackToLogin()
                    },
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .testTag("register_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = stringResource(R.string.cd_back),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }

                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = stringResource(R.string.cd_app_logo),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .width(156.dp)
                        .height(26.dp)
                        .testTag("futurus_brand_logo")
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "CUIDANDO DE QUEM VOCÊ MAIS AMA",
                    fontSize = 12.sp,
                    letterSpacing = 0.6.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = if (isCaregiver) "Cadastro de Cuidador" else "Crie sua Conta",
                    fontSize = 23.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )
            }

            // Original White Form Card
            Surface(
                shape = RoundedCornerShape(25.dp),
                color = Color.White,
                shadowElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Original "Sou Cliente" / "Sou Cuidador" switcher
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color.White,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.7f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(4.dp),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            OriginalAccountToggleButton(
                                label = "Sou Cliente",
                                selected = !isCaregiver,
                                onClick = {
                                    isCaregiver = false
                                    localValidationError = null
                                    onClearError()
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("register_client_tab")
                            )
                            OriginalAccountToggleButton(
                                label = "Sou Cuidador",
                                selected = isCaregiver,
                                onClick = {
                                    isCaregiver = true
                                    localValidationError = null
                                    onClearError()
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("register_caregiver_tab")
                            )
                        }
                    }

                    // Optional avatar photo picker
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = OriginalCream,
                            border = BorderStroke(1.2.dp, OriginalViolet.copy(alpha = 0.35f)),
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .clickable {
                                    avatarPicker.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                    )
                                }
                                .testTag("register_photo_picker")
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                if (!avatarPath.isNullOrBlank() && File(avatarPath!!).exists()) {
                                    AsyncImage(
                                        model = File(avatarPath!!),
                                        contentDescription = stringResource(R.string.cd_profile_avatar),
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Rounded.AddAPhoto,
                                        contentDescription = stringResource(R.string.cd_camera_icon),
                                        tint = OriginalViolet,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            }
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (avatarPath == null) "Foto de perfil (opcional)" else "Foto selecionada",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Adicione agora ou depois no perfil para liberar o selo Verificado.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    if (!effectiveError.isNullOrBlank()) {
                        AuthErrorBanner(message = effectiveError)
                    }

                    OriginalFormField(
                        label = "Nome Completo",
                        value = name,
                        onValueChange = {
                            name = it
                            localValidationError = null
                            if (authError != null) onClearError()
                        },
                        placeholder = if (isCaregiver) "ex: João Silva" else "ex: Maria Silva",
                        leadingIcon = Icons.Rounded.Person,
                        testTag = "register_name_input"
                    )

                    OriginalFormField(
                        label = if (isCaregiver) "E-mail Profissional" else "E-mail",
                        value = email,
                        onValueChange = {
                            email = it
                            localValidationError = null
                            if (authError != null) onClearError()
                        },
                        placeholder = "maria@exemplo.com",
                        leadingIcon = Icons.Rounded.Email,
                        keyboardType = KeyboardType.Email,
                        testTag = "register_email_input"
                    )

                    OriginalFormField(
                        label = if (isCaregiver) "Telefone de Contato" else "Telefone",
                        value = phone,
                        onValueChange = {
                            phone = it
                            localValidationError = null
                        },
                        placeholder = "(11) 99999-9999",
                        leadingIcon = Icons.Rounded.Phone,
                        keyboardType = KeyboardType.Phone,
                        testTag = "register_phone_input"
                    )

                    if (isCaregiver) {
                        Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                            Text(
                                text = "Especialidade Principal",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Box(modifier = Modifier.fillMaxWidth()) {
                                Surface(
                                    shape = RoundedCornerShape(13.dp),
                                    color = Color.White,
                                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(13.dp))
                                        .clickable { specialtyMenuOpen = true }
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 14.dp, vertical = 14.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = specialty,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Icon(
                                            imageVector = Icons.Rounded.KeyboardArrowDown,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                                DropdownMenu(
                                    expanded = specialtyMenuOpen,
                                    onDismissRequest = { specialtyMenuOpen = false }
                                ) {
                                    specialties.forEach { option ->
                                        DropdownMenuItem(
                                            text = { Text(option) },
                                            onClick = {
                                                specialty = option
                                                specialtyMenuOpen = false
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        OriginalFormField(
                            label = "Experiência (Anos)",
                            value = experienceYears,
                            onValueChange = {
                                experienceYears = it.filter { ch -> ch.isDigit() }
                                localValidationError = null
                            },
                            placeholder = "ex: 3 anos de experiência",
                            leadingIcon = Icons.Rounded.CalendarMonth,
                            keyboardType = KeyboardType.Number
                        )
                    }

                    OriginalFormField(
                        label = if (isCaregiver) "Senha de Acesso" else "Senha",
                        value = password,
                        onValueChange = {
                            password = it
                            localValidationError = null
                            if (authError != null) onClearError()
                        },
                        placeholder = "Mínimo 8 caracteres",
                        leadingIcon = Icons.Rounded.Lock,
                        keyboardType = KeyboardType.Password,
                        isPassword = true,
                        passwordVisible = passwordVisible,
                        onTogglePasswordVisible = { passwordVisible = !passwordVisible },
                        testTag = "register_password_input"
                    )

                    if (!isCaregiver) {
                        OriginalFormField(
                            label = "Confirmar Senha",
                            value = confirmPassword,
                            onValueChange = {
                                confirmPassword = it
                                localValidationError = null
                                if (authError != null) onClearError()
                            },
                            placeholder = "Repita sua senha",
                            leadingIcon = Icons.Rounded.Lock,
                            keyboardType = KeyboardType.Password,
                            isPassword = true,
                            passwordVisible = passwordVisible,
                            onTogglePasswordVisible = { passwordVisible = !passwordVisible },
                            testTag = "register_confirm_password_input"
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Localização e Endereço",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = OriginalViolet
                        )
                        OutlinedButton(
                            onClick = {
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
                            },
                            enabled = !isDetectingLocation,
                            shape = RoundedCornerShape(50),
                            border = BorderStroke(1.dp, OriginalViolet.copy(alpha = 0.4f)),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                            modifier = Modifier.testTag("register_detect_location_button")
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
                                    tint = OriginalViolet,
                                    modifier = Modifier.size(15.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Usar GPS",
                                style = MaterialTheme.typography.labelMedium,
                                color = OriginalViolet
                            )
                        }
                    }

                    if (!locationFeedback.isNullOrBlank()) {
                        Text(
                            text = locationFeedback!!,
                            style = MaterialTheme.typography.bodySmall,
                            color = OriginalViolet
                        )
                    }

                    OriginalFormField(
                        label = "Endereço",
                        value = address,
                        onValueChange = {
                            address = it
                            localValidationError = null
                        },
                        placeholder = "Digite seu endereço",
                        leadingIcon = Icons.Rounded.LocationOn,
                        testTag = "register_address_input"
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OriginalFormField(
                            label = "Cidade",
                            value = city,
                            onValueChange = {
                                city = it
                                localValidationError = null
                            },
                            placeholder = "Digite sua cidade",
                            leadingIcon = Icons.Rounded.LocationOn,
                            modifier = Modifier.weight(1f),
                            testTag = "register_city_input"
                        )
                        OriginalFormField(
                            label = "Bairro",
                            value = neighborhood,
                            onValueChange = {
                                neighborhood = it
                                localValidationError = null
                            },
                            placeholder = "Digite seu bairro",
                            leadingIcon = Icons.Rounded.LocationOn,
                            modifier = Modifier.weight(1f),
                            testTag = "register_neighborhood_input"
                        )
                    }

                    // Original Terms & Privacy Policy section
                    val termsMissingError = effectiveError?.contains("termos", ignoreCase = true) == true
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = if (termsMissingError) {
                            MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.45f)
                        } else {
                            OriginalCream.copy(alpha = 0.55f)
                        },
                        border = BorderStroke(
                            width = 1.dp,
                            color = if (termsMissingError) {
                                MaterialTheme.colorScheme.error
                            } else {
                                OriginalViolet.copy(alpha = 0.28f)
                            }
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 10.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = termsAccepted,
                                    onCheckedChange = {
                                        termsAccepted = it
                                        localValidationError = null
                                    },
                                    colors = CheckboxDefaults.colors(checkedColor = OriginalViolet),
                                    modifier = Modifier.testTag("register_terms_checkbox")
                                )
                                Text(
                                    text = buildAnnotatedString {
                                        append("Eu aceito os ")
                                        withStyle(
                                            SpanStyle(
                                                color = OriginalViolet,
                                                fontWeight = FontWeight.Bold,
                                                textDecoration = TextDecoration.Underline
                                            )
                                        ) {
                                            append("Termos de Serviço e a Política de Privacidade")
                                        }
                                        append(".")
                                    },
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable { showTermsDialog = true }
                                        .padding(vertical = 4.dp)
                                )
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 12.dp, end = 6.dp),
                                horizontalArrangement = Arrangement.End
                            ) {
                                OutlinedButton(
                                    onClick = { showTermsDialog = true },
                                    shape = RoundedCornerShape(50),
                                    border = BorderStroke(1.dp, OriginalViolet.copy(alpha = 0.45f)),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                    modifier = Modifier.testTag("register_read_terms_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Description,
                                        contentDescription = null,
                                        tint = OriginalViolet,
                                        modifier = Modifier.size(15.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Ler Termos e Privacidade",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = OriginalViolet
                                    )
                                }
                            }
                        }
                    }
                }
            }

            if (!effectiveError.isNullOrBlank()) {
                AuthErrorBanner(message = effectiveError)
            }

            Spacer(modifier = Modifier.height(4.dp))

            Button(
                onClick = {
                    val errorMsg = when {
                        name.isBlank() -> "Informe seu nome."
                        !Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches() -> "Informe um e-mail válido."
                        phone.count { it.isDigit() } < 10 -> "Informe o telefone com DDD."
                        password.length < 8 -> "A senha precisa ter pelo menos 8 caracteres."
                        !isCaregiver && password != confirmPassword -> "As senhas precisam ser iguais."
                        address.isBlank() -> "Informe o endereço."
                        city.isBlank() -> "Informe sua cidade."
                        neighborhood.isBlank() -> "Informe seu bairro."
                        isCaregiver && experienceYears.isBlank() -> "Informe sua experiência."
                        !termsAccepted -> "Leia e aceite os termos para continuar."
                        else -> null
                    }
                    if (errorMsg != null) {
                        localValidationError = errorMsg
                    } else {
                        localValidationError = null
                        onRegisterClick(
                            name,
                            email,
                            password,
                            if (isCaregiver) password else confirmPassword,
                            phone,
                            address,
                            city,
                            neighborhood,
                            "",
                            avatarPath,
                            isCaregiver,
                            specialty,
                            experienceYears
                        )
                    }
                },
                enabled = !isLoading,
                shape = RoundedCornerShape(15.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = OriginalViolet,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .height(52.dp)
                    .testTag("register_submit_button")
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(22.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = if (isCaregiver) "Enviar Cadastro" else "Cadastrar",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            TextButton(
                onClick = {
                    onClearError()
                    onBackToLogin()
                },
                modifier = Modifier.testTag("go_to_login_button")
            ) {
                Text(
                    text = "Já tem uma conta? Faça login",
                    color = OriginalViolet,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    if (showTermsDialog) {
        AlertDialog(
            onDismissRequest = { showTermsDialog = false },
            title = {
                Text(
                    text = "Termos de Serviço e Privacidade",
                    fontWeight = FontWeight.Bold,
                    color = OriginalViolet
                )
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Sobre este aplicativo",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Projeto FuturusCare.\n\nSeus cadastros, pets, agendamentos e mensagens são salvos somente neste aparelho. Para testar solicitações e mensagens, alterne as contas neste aparelho. Não há sincronização entre celulares.\n\nO aplicativo não realiza cobranças.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "1. Termos de Serviço",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Ao criar uma conta como Cliente ou Cuidador (PetSitter), você se compromete a informar dados verdadeiros sobre você e seus pets, manter respeito nas mensagens e utilizar a agenda com responsabilidade.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "2. Política de Privacidade e Permissões",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Suas credenciais são protegidas por hash criptográfico PBKDF2 com sal individual no banco de dados local. A permissão de localização é usada apenas quando solicitada para preencher seu endereço, e as notificações servem para avisar sobre agendamentos, pets e mensagens.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        termsAccepted = true
                        localValidationError = null
                        showTermsDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = OriginalViolet)
                ) {
                    Text("Aceitar e continuar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showTermsDialog = false }) {
                    Text("Entendi")
                }
            }
        )
    }
}

@Composable
private fun OriginalAccountToggleButton(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(11.dp),
        color = if (selected) OriginalViolet else Color.Transparent,
        border = BorderStroke(1.dp, OriginalViolet),
        modifier = modifier
            .height(38.dp)
            .clip(RoundedCornerShape(11.dp))
            .clickable(onClick = onClick)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = label,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = if (selected) Color.White else OriginalViolet
            )
        }
    }
}

@Composable
private fun OriginalFormField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false,
    passwordVisible: Boolean = false,
    onTogglePasswordVisible: (() -> Unit)? = null,
    testTag: String? = null
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, fontSize = 14.sp) },
            leadingIcon = {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
            },
            trailingIcon = if (isPassword && onTogglePasswordVisible != null) {
                {
                    IconButton(onClick = onTogglePasswordVisible) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Rounded.VisibilityOff else Icons.Rounded.Visibility,
                            contentDescription = if (passwordVisible) "Ocultar senha" else "Mostrar senha"
                        )
                    }
                }
            } else null,
            visualTransformation = if (isPassword && !passwordVisible) {
                PasswordVisualTransformation()
            } else {
                VisualTransformation.None
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            shape = RoundedCornerShape(13.dp),
            colors = originalFieldColors(),
            modifier = Modifier
                .fillMaxWidth()
                .then(if (testTag != null) Modifier.testTag(testTag) else Modifier)
        )
    }
}

@Composable
private fun AuthErrorBanner(message: String) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.errorContainer,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.35f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Rounded.ErrorOutline,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = message,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onErrorContainer
            )
        }
    }
}

@Composable
private fun originalFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedContainerColor = Color.White,
    unfocusedContainerColor = Color.White,
    focusedBorderColor = OriginalViolet,
    unfocusedBorderColor = MaterialTheme.colorScheme.outline
)
