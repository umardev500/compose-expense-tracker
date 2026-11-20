package com.umar.expensetracker.feature.dashboard.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.umar.expensetracker.R
import com.umar.expensetracker.feature.dashboard.domain.model.DashboardSummary
import com.umar.expensetracker.ui.icon.HeroIcons
import java.text.NumberFormat
import java.util.Locale

private const val MASKED_BALANCE = "••••••••"
private const val MASKED_CASHFLOW = "••••"

@Composable
private fun rememberContentScrollModifier(
    modifier: Modifier = Modifier
): Modifier = modifier
    .fillMaxSize()
    .verticalScroll(rememberScrollState())
    .padding(horizontal = 22.dp, vertical = 20.dp)

@Composable
fun DashboardContent(
    uiState: DashboardUiState,
    modifier: Modifier = Modifier,
    onToggleBalanceVisibility: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onRefresh: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        when (uiState) {
            is DashboardUiState.Loading -> {
                PureLoadingSkeleton(modifier = rememberContentScrollModifier())
            }

            is DashboardUiState.Error -> {
                PureErrorState(
                    message = uiState.message,
                    onRetry = onRefresh,
                    modifier = Modifier.fillMaxSize()
                )
            }

            is DashboardUiState.Success -> {
                PureDashboardContent(
                    state = uiState,
                    onToggleBalanceVisibility = onToggleBalanceVisibility,
                    onNotificationClick = onNotificationClick,
                    modifier = rememberContentScrollModifier()
                )
            }
        }
    }
}

@Composable
private fun PureDashboardContent(
    state: DashboardUiState.Success,
    onToggleBalanceVisibility: () -> Unit,
    onNotificationClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        PureTopBar(
            userName = state.userName,
            onNotificationClick = onNotificationClick
        )

        TitaniumHeroBalance(
            summary = state.summary,
            isBalanceVisible = state.isBalanceVisible,
            percentageChange = state.percentageChange,
            onToggleVisibility = onToggleBalanceVisibility
        )
    }
}

// ==========================================
// 1. Header (Top Bar)
// ==========================================
@Composable
private fun PureTopBar(
    userName: String,
    onNotificationClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ProfileAvatar(userName = userName)

            Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
                Text(
                    text = "Selamat datang kembali,",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    letterSpacing = 0.2.sp
                )
                Text(
                    text = userName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        NotificationButton(onClick = onNotificationClick)
    }
}

@Composable
private fun ProfileAvatar(
    userName: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.size(40.dp),
        shape = CircleShape,
        color = MaterialTheme.colorScheme.primaryContainer,
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f)
        )
    ) {
        Image(
            painter = painterResource(id = R.drawable.pp),
            contentDescription = "Foto profil $userName",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
private fun NotificationButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        Surface(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .clickable(onClick = onClick),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surfaceContainerLow,
            border = BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f)
            )
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = HeroIcons.Bell,
                    contentDescription = "Notifikasi",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        Box(
            modifier = Modifier
                .size(7.dp)
                .align(Alignment.TopEnd)
                .padding(top = 2.dp, end = 2.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.error)
        )
    }
}

// ==========================================
// 2. Hero Balance
// ==========================================
@Composable
fun TitaniumHeroBalance(
    summary: DashboardSummary,
    isBalanceVisible: Boolean,
    percentageChange: Double,
    onToggleVisibility: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
        ),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            HeroBalanceHeader(
                onToggleVisibility = onToggleVisibility,
                isBalanceVisible = isBalanceVisible
            )

            HeroBalanceAmount(
                totalBalance = summary.totalBalance,
                isBalanceVisible = isBalanceVisible,
                percentageChange = percentageChange
            )

            CashflowSplitBar(
                totalIncome = summary.totalIncome,
                totalExpense = summary.totalExpense,
                isBalanceVisible = isBalanceVisible
            )
        }
    }
}

@Composable
private fun HeroBalanceHeader(
    isBalanceVisible: Boolean,
    onToggleVisibility: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surfaceContainerLow,
            border = BorderStroke(
                width = 0.8.dp,
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.25f)
            )
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.tertiary)
                )
                Text(
                    text = "Total Saldo · IDR",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.85f),
                    letterSpacing = 0.3.sp
                )
            }
        }

        IconButton(
            onClick = onToggleVisibility,
            modifier = Modifier.size(32.dp)
        ) {
            Icon(
                imageVector = if (isBalanceVisible) HeroIcons.Eye else HeroIcons.EyeSlash,
                contentDescription = "Tampilkan atau sembunyikan saldo",
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                modifier = Modifier.size(19.dp)
            )
        }
    }
}

@Composable
private fun HeroBalanceAmount(
    totalBalance: Double,
    isBalanceVisible: Boolean,
    percentageChange: Double
) {
    val countedBalance = rememberCountAnimation(
        targetValue = totalBalance,
        enabled = isBalanceVisible
    )

    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        AnimatedContent(
            targetState = isBalanceVisible,
            transitionSpec = { fadeIn(tween(180)) togetherWith fadeOut(tween(180)) },
            label = "titaniumBalanceAnim"
        ) { visible ->
            Text(
                text = if (visible) {
                    formatCurrency(countedBalance, withCurrency = false)
                } else {
                    MASKED_BALANCE
                },
                style = MaterialTheme.typography.displaySmall.copy(
                    fontSize = 38.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = (-1.2).sp,
                    fontFeatureSettings = "tnum"
                ),
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                softWrap = false
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Icon(
                imageVector = HeroIcons.ArrowTrendingUp,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.size(15.dp)
            )
            Text(
                text = "+${percentageChange}% bulan ini",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.tertiary
            )
        }
    }
}

@Composable
private fun CashflowSplitBar(
    totalIncome: Double,
    totalExpense: Double,
    isBalanceVisible: Boolean,
    modifier: Modifier = Modifier
) {
    val countedIncome = rememberCountAnimation(
        targetValue = totalIncome,
        enabled = isBalanceVisible
    )
    val countedExpense = rememberCountAnimation(
        targetValue = totalExpense,
        enabled = isBalanceVisible
    )

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        border = BorderStroke(
            width = 0.8.dp,
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.25f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CashflowMetricItem(
                label = "Pemasukan",
                amountText = if (isBalanceVisible) "+${formatCurrency(countedIncome)}" else MASKED_CASHFLOW,
                icon = HeroIcons.ArrowDown,
                iconTint = MaterialTheme.colorScheme.tertiary,
                iconContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
                valueColor = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.weight(1f)
            )

            Box(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .height(26.dp)
                    .width(0.8.dp)
                    .background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f))
            )

            CashflowMetricItem(
                label = "Pengeluaran",
                amountText = if (isBalanceVisible) "-${formatCurrency(countedExpense)}" else MASKED_CASHFLOW,
                icon = HeroIcons.ArrowUp,
                iconTint = MaterialTheme.colorScheme.error,
                iconContainerColor = MaterialTheme.colorScheme.errorContainer,
                valueColor = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun CashflowMetricItem(
    label: String,
    amountText: String,
    icon: ImageVector,
    iconTint: Color,
    iconContainerColor: Color,
    valueColor: Color,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(iconContainerColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = iconTint,
                modifier = Modifier.size(15.dp)
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                maxLines = 1,
                softWrap = false,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = amountText,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontFeatureSettings = "tnum"
                ),
                fontWeight = FontWeight.SemiBold,
                color = valueColor,
                maxLines = 1,
                softWrap = false,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

// ==========================================
// 3. Loading & Error States
// ==========================================
@Composable
private fun PureLoadingSkeleton(
    modifier: Modifier = Modifier
) {
    val transition = rememberInfiniteTransition(label = "dashboardSkeletonPulse")
    val alpha by transition.animateFloat(
        initialValue = 0.35f,
        targetValue = 0.75f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "skeletonAlpha"
    )
    val placeholderColor = MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = alpha)

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        TopBarSkeleton(placeholderColor = placeholderColor)
        HeroBalanceSkeleton(placeholderColor = placeholderColor)
    }
}

@Composable
private fun TopBarSkeleton(placeholderColor: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(placeholderColor)
            )
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Box(
                    modifier = Modifier
                        .width(80.dp)
                        .height(12.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(placeholderColor)
                )
                Box(
                    modifier = Modifier
                        .width(130.dp)
                        .height(16.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(placeholderColor)
                )
            }
        }
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(placeholderColor)
        )
    }
}

@Composable
private fun HeroBalanceSkeleton(placeholderColor: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
        ),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .width(105.dp)
                        .height(26.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(placeholderColor)
                )
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(placeholderColor)
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(
                    modifier = Modifier
                        .width(190.dp)
                        .height(42.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(placeholderColor)
                )
                Box(
                    modifier = Modifier
                        .width(125.dp)
                        .height(14.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(placeholderColor)
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(placeholderColor)
            )
        }
    }
}

@Composable
private fun PureErrorState(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(14.dp, Alignment.CenterVertically)
    ) {
        Text(
            text = "Gagal memuat dasbor",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
        Text(
            text = message.ifBlank { "Terjadi kesalahan tak terduga. Silakan coba lagi." },
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        Button(
            onClick = onRetry,
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text(
                text = "Coba Lagi",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun formatCurrency(
    amount: Number,
    withCurrency: Boolean = false,
    locale: Locale = Locale("id", "ID")
): String {
    val formatter = remember(withCurrency, locale) {
        (if (withCurrency) {
            NumberFormat.getCurrencyInstance(locale)
        } else {
            NumberFormat.getNumberInstance(locale)
        }).apply {
            maximumFractionDigits = 0
        }
    }
    return formatter.format(amount)
}

@Composable
fun rememberCountAnimation(
    targetValue: Double,
    enabled: Boolean = true,
    durationMillis: Int = 650,
    startFromZeroOnEnable: Boolean = true
): Float {
    val animatable = remember { Animatable(0f) }

    LaunchedEffect(targetValue, enabled) {
        if (startFromZeroOnEnable) {
            animatable.snapTo(0f)
        }
        if (enabled) {
            animatable.animateTo(
                targetValue = targetValue.toFloat(),
                animationSpec = tween(
                    durationMillis = durationMillis,
                    easing = FastOutSlowInEasing
                )
            )
        }
    }

    return animatable.value
}