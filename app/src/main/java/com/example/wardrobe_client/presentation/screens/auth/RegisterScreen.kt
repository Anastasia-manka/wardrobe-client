package com.example.wardrobe_client.presentation.screens.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.wardrobe_client.R
import com.example.wardrobe_client.presentation.navigation.Screen
import com.example.wardrobe_client.presentation.theme.InterFont
import com.example.wardrobe_client.presentation.theme.ShugaiBluePrimary
import com.example.wardrobe_client.presentation.theme.ShugaiInputBg
import com.example.wardrobe_client.presentation.theme.ShugaiPlaceholder
import com.example.wardrobe_client.presentation.theme.ShugaiScreenBackground
import com.example.wardrobe_client.presentation.theme.YauzaFont
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.IconButton
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.layout.widthIn

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var genderExpanded by remember { mutableStateOf(false) }
    var consentChecked by remember { mutableStateOf(false) }
    val genders = listOf("Мужской", "Женский")
    val isFormValid = uiState.name.isNotBlank() &&
            uiState.email.isNotBlank() &&
            uiState.gender.isNotEmpty() &&
            uiState.password.isNotBlank() &&
            uiState.passwordConfirm.isNotBlank() &&
            consentChecked
    var passwordVisible by remember { mutableStateOf(false) }
    var passwordConfirmVisible by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            navController.navigate(Screen.Onboarding.route) {
                popUpTo(Screen.Register.route) { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ShugaiScreenBackground)
    ) {
        Image(
            painter = painterResource(R.drawable.ic_flower_1_2),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(100.dp)
                .offset(x = 320.dp, y = (25).dp)
        )

        Image(
            painter = painterResource(R.drawable.ic_flower_1_1),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(326.dp)
                .offset(x = (-82).dp, y = 704.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(
                    top = 96.dp,
                    bottom = 48.dp,
                    start = 24.dp,
                    end = 24.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "РЕГИСТРАЦИЯ",
                fontFamily = YauzaFont,
                fontSize = 24.sp,
                fontWeight = FontWeight.W400,
                color = ShugaiBluePrimary
            )

            Spacer(modifier = Modifier.height(56.dp))

            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                TextField(
                    value = uiState.name,
                    onValueChange = viewModel::onNameChange,
                    placeholder = {
                        Text(
                            "Имя",
                            fontFamily = InterFont,
                            fontSize = 16.sp,
                            color = ShugaiPlaceholder
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(30.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = ShugaiInputBg,
                        unfocusedContainerColor = ShugaiInputBg,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(51.dp)
                )

                TextField(
                    value = uiState.email,
                    onValueChange = viewModel::onEmailChange,
                    placeholder = {
                        Text(
                            "Почта",
                            fontFamily = InterFont,
                            fontSize = 16.sp,
                            color = ShugaiPlaceholder
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    singleLine = true,
                    shape = RoundedCornerShape(30.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = ShugaiInputBg,
                        unfocusedContainerColor = ShugaiInputBg,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(51.dp)
                )

                ExposedDropdownMenuBox(
                    expanded = genderExpanded,
                    onExpandedChange = { genderExpanded = it }
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(51.dp)
                            .border(
                                BorderStroke(1.dp, ShugaiPlaceholder),
                                RoundedCornerShape(30.dp)
                            )
                            .background(Color.White, RoundedCornerShape(30.dp))
                            .menuAnchor()
                            .padding(horizontal = 16.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = if (uiState.gender.isEmpty()) "Пол" else uiState.gender,
                            fontFamily = InterFont,
                            fontSize = 16.sp,
                            color = if (uiState.gender.isEmpty()) ShugaiPlaceholder else Color.Black
                        )
                        Icon(
                            imageVector = if (genderExpanded)
                                Icons.Default.KeyboardArrowUp
                            else
                                Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                            tint = ShugaiPlaceholder,
                            modifier = Modifier
                                .size(20.dp)
                                .align(Alignment.CenterEnd)
                        )
                    }
                    DropdownMenu(
                        expanded = genderExpanded,
                        onDismissRequest = { genderExpanded = false },
                        modifier = Modifier
                            .background(Color.White)
                            .border(
                                BorderStroke(1.dp, Color(0xFFE7E4E4)),
                                RoundedCornerShape(4.dp)
                            )
                            .widthIn(min = 372.dp)
                    ) {
                        genders.forEachIndexed { index, gender ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        gender,
                                        fontFamily = InterFont,
                                        fontSize = 16.sp,
                                        color = Color.Black
                                    )
                                },
                                onClick = {
                                    viewModel.onGenderChange(gender)
                                    genderExpanded = false
                                },
                                modifier = Modifier.background(
                                    color = if (uiState.gender == gender)
                                        Color(0xFFEEF2FF)
                                    else
                                        Color.White
                                )
                            )
                            if (index < genders.size - 1) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(1.dp)
                                        .padding(horizontal = 16.dp)
                                        .background(Color(0xFFE7E4E4))
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                TextField(
                    value = uiState.password,
                    onValueChange = viewModel::onPasswordChange,
                    placeholder = {
                        Text(
                            "Пароль",
                            fontFamily = InterFont,
                            fontSize = 16.sp,
                            color = ShugaiPlaceholder
                        )
                    },
                    visualTransformation = if (passwordVisible)
                        VisualTransformation.None
                    else
                        PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible)
                                    Icons.Default.VisibilityOff
                                else
                                    Icons.Default.Visibility,
                                contentDescription = null,
                                tint = ShugaiPlaceholder
                            )
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(30.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = ShugaiInputBg,
                        unfocusedContainerColor = ShugaiInputBg,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(51.dp)
                )

                TextField(
                    value = uiState.passwordConfirm,
                    onValueChange = viewModel::onPasswordConfirmChange,
                    placeholder = {
                        Text(
                            "Повторите пароль",
                            fontFamily = InterFont,
                            fontSize = 16.sp,
                            color = ShugaiPlaceholder
                        )
                    },
                    visualTransformation = if (passwordConfirmVisible)
                        VisualTransformation.None
                    else
                        PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        IconButton(onClick = { passwordConfirmVisible = !passwordConfirmVisible }) {
                            Icon(
                                imageVector = if (passwordConfirmVisible)
                                    Icons.Default.VisibilityOff
                                else
                                    Icons.Default.Visibility,
                                contentDescription = null,
                                tint = ShugaiPlaceholder
                            )
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(30.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = ShugaiInputBg,
                        unfocusedContainerColor = ShugaiInputBg,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(51.dp)
                )
            }

            if (uiState.error != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = uiState.error!!,
                    color = MaterialTheme.colorScheme.error,
                    fontFamily = InterFont,
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = viewModel::register,
                enabled = !uiState.isLoading && isFormValid,
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ShugaiBluePrimary,
                    contentColor = Color(0xFFFFFEFC)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(51.dp)
            ) {
                Text(
                    text = "Зарегистрироваться",
                    fontFamily = InterFont,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W500
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .border(
                            BorderStroke(1.dp, Color.Black),
                            RoundedCornerShape(4.dp)
                        )
                        .background(ShugaiScreenBackground, RoundedCornerShape(4.dp))
                        .clickable { consentChecked = !consentChecked },
                    contentAlignment = Alignment.Center
                ) {
                    if (consentChecked) {
                        Text(
                            text = "✓",
                            color = ShugaiBluePrimary,
                            fontSize = 16.sp
                        )
                    }
                }
                Text(
                    text = "Нажимая на кнопку, Вы даете согласие на обработку персональных данных",
                    fontFamily = InterFont,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W400,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Уже есть аккаунт?",
                    fontFamily = InterFont,
                    fontSize = 14.sp,
                    color = Color.Black
                )
                Text(
                    text = "Войти",
                    fontFamily = InterFont,
                    fontSize = 14.sp,
                    color = ShugaiBluePrimary,
                    modifier = Modifier.clickable {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}