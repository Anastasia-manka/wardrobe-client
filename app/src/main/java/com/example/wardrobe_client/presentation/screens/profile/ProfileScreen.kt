package com.example.wardrobe_client.presentation.screens.profile

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Male
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.os.LocaleListCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.wardrobe_client.R
import com.example.wardrobe_client.presentation.navigation.Screen
import com.example.wardrobe_client.presentation.theme.InterFont
import com.example.wardrobe_client.presentation.theme.ShugaiBluePrimary
import com.example.wardrobe_client.presentation.theme.ShugaiScreenBackground
import com.example.wardrobe_client.presentation.theme.YauzaFont
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.filled.Close
import androidx.compose.ui.res.stringResource

@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(uiState.isLoggedOut, uiState.isAccountDeleted) {
        if (uiState.isLoggedOut || uiState.isAccountDeleted) {
            navController.navigate(Screen.Login.route) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    if (uiState.showDeleteDialog) {
        Dialog(onDismissRequest = viewModel::hideDeleteDialog) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(20.dp))
                    .padding(24.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            modifier = Modifier
                                .size(24.dp)
                                .align(Alignment.CenterEnd)
                                .clickable { viewModel.hideDeleteDialog() }
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = stringResource(R.string.profile_delete_account_confirm),
                        fontFamily = InterFont,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W500,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                                .border(1.dp, Color(0xFF8B8B8B), RoundedCornerShape(30.dp))
                                .clickable {
                                    viewModel.hideDeleteDialog()
                                    viewModel.deleteAccount()
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(R.string.profile_delete_button),
                                fontFamily = InterFont,
                                fontSize = 16.sp,
                                color = Color(0xFFC33636)
                            )
                        }
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                                .background(ShugaiBluePrimary, RoundedCornerShape(30.dp))
                                .clickable { viewModel.hideDeleteDialog() },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(R.string.cancel_button),
                                fontFamily = InterFont,
                                fontSize = 16.sp,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ShugaiScreenBackground)
    ) {
        Image(
            painter = painterResource(R.drawable.ic_flower_1_1),
            contentDescription = null,
            modifier = Modifier
                .size(293.dp)
                .offset(x = (-160).dp, y = 500.dp)
                .clip(RoundedCornerShape(0.dp)),
            alpha = 0.1f
        )

        Image(
            painter = painterResource(R.drawable.ic_flower_1_1),
            contentDescription = null,
            modifier = Modifier
                .size(293.dp)
                .align(Alignment.TopEnd)
                .offset(x = 110.dp, y = 100.dp),
            alpha = 0.1f
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = stringResource(R.string.profile_title),
                fontFamily = YauzaFont,
                fontSize = 24.sp,
                fontWeight = FontWeight.W400,
                color = ShugaiBluePrimary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(158.dp)
                    .clip(RoundedCornerShape(30.dp))
            ) {
                Image(
                    painter = painterResource(R.drawable.bg_birch),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Box(
                modifier = Modifier
                    .size(120.dp)
                    .offset(y = (-50).dp)
                    .border(4.dp, Color(0xFF003A86).copy(alpha = 0.2f), CircleShape)
                    .clip(CircleShape)
                    .background(Color(0xFFE7E5E5))
            ) {
                val avatarRes = when (uiState.user?.gender) {
                    "Мужской" -> R.drawable.ic_avatar_male
                    "Женский" -> R.drawable.ic_avatar_female
                    else -> R.drawable.ic_avatar_female
                }
                Image(
                    painter = painterResource(avatarRes),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.offset(y = (-36).dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = when (uiState.user?.gender) {
                            "Мужской" -> Icons.Default.Male
                            else -> Icons.Default.Female
                        },
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = uiState.user?.name ?: "",
                        fontFamily = InterFont,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.W500,
                        color = Color.Black
                    )
                }

                Text(
                    text = uiState.user?.email ?: "",
                    fontFamily = InterFont,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W500,
                    color = Color(0xFF8B8B8B)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                ProfileButton(
                    icon = {
                        Icon(
                            imageVector = Icons.Default.HelpOutline,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    text = stringResource(R.string.profile_support),
                    textColor = Color.White,
                    backgroundColor = ShugaiBluePrimary,
                    onClick = {
                        val intent = Intent(Intent.ACTION_SENDTO).apply {
                            data = Uri.parse("mailto:9852055031@mai.ru")
                        }
                        context.startActivity(intent)
                    }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                ProfileButton(
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Logout,
                            contentDescription = null,
                            tint = Color(0xFFC33636),
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    text = stringResource(R.string.profile_logout),
                    textColor = Color(0xFFC33636),
                    backgroundColor = Color(0xFFE7E5E5),
                    onClick = viewModel::logout
                )

                ProfileButton(
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = null,
                            tint = Color(0xFFC33636),
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    text = stringResource(R.string.profile_delete_account),
                    textColor = Color(0xFFC33636),
                    backgroundColor = Color(0xFFE7E5E5),
                    onClick = viewModel::showDeleteDialog
                )
            }
        }
    }
}

@Composable
private fun ProfileButton(
    icon: @Composable () -> Unit,
    text: String,
    textColor: Color,
    backgroundColor: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(30.dp))
            .background(backgroundColor)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            icon()
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = text,
                fontFamily = InterFont,
                fontSize = 16.sp,
                fontWeight = FontWeight.W400,
                color = textColor
            )
        }
    }
}