package com.umar.expensetracker.feature.dashboard.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.umar.expensetracker.R
import com.umar.expensetracker.feature.dashboard.domain.model.DashboardSummary
import com.umar.expensetracker.ui.icon.HeroIcons
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.PI
import kotlin.math.roundToInt
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val MASKED_BALANCE = "••••••••"
private const val MASKED_CASHFLOW = "••••"
private const val MAX_VISIBLE_CATEGORY_ROWS = 3

data class SpendingCategory(
    val id: String,
    val name: String,
    val emoji: String,
    val transactionCount: Int,
    val amount: Double,
    val accentColor: Color
)

private data class BudgetSnapshot(
    val safeBalance: Double,
    val safeExpense: Double,
    val remainingAmount: Double,
    val usedFraction: Float,
    val remainingPercent: Int,
    val isOverSpent: Boolean
) {
    companion object {
        fun from(totalBalance: Double, totalExpense: Double): BudgetSnapshot {
            val balance = totalBalance.coerceAtLeast(0.0)
            val expense = totalExpense.coerceAtLeast(0.0)
            val remaining = (balance - expense).coerceAtLeast(0.0)
            val used = if (balance > 0.0) {
                (expense / balance).toFloat().coerceIn(0f, 1f)
            } else {
                0f
            }
            val remainingPct = ((1f - used) * 100).roundToInt().coerceIn(0, 100)
            return BudgetSnapshot(
                safeBalance = balance,
                safeExpense = expense,
                remainingAmount = remaining,
                usedFraction = used,
                remainingPercent = remainingPct,
                isOverSpent = expense > balance && balance > 0.0
            )
        }
    }
}

@Composable
private fun rememberContentScrollModifier(
    modifier: Modifier = Modifier
): Modifier = modifier
    .fillMaxSize()
    .verticalScroll(rememberScrollState())
    .padding(horizontal = 20.dp, vertical = 20.dp)

@Composable
fun DashboardContent(
    uiState: DashboardUiState,
    modifier: Modifier = Modifier,
    spendingCategories: List<SpendingCategory> = defaultSpendingCategories(),
    onToggleBalanceVisibility: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onCategoryClick: (SpendingCategory) -> Unit = {},
    onSeeAllSpendingClick: () -> Unit = {},
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
                    spendingCategories = spendingCategories,
                    onToggleBalanceVisibility = onToggleBalanceVisibility,
                    onNotificationClick = onNotificationClick,
                    onCategoryClick = onCategoryClick,
                    onSeeAllSpendingClick = onSeeAllSpendingClick,
                    modifier = rememberContentScrollModifier()
                )
            }
        }
    }
}

@Composable
private fun PureDashboardContent(
    state: DashboardUiState.Success,
    spendingCategories: List<SpendingCategory>,
    onToggleBalanceVisibility: () -> Unit,
    onNotificationClick: () -> Unit,
    onCategoryClick: (SpendingCategory) -> Unit,
    onSeeAllSpendingClick: () -> Unit,
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

        SpendingBreakdownSection(
            categories = spendingCategories,
            totalBalance = state.summary.totalBalance,
            totalExpense = state.summary.totalExpense,
            isBalanceVisible = state.isBalanceVisible,
            onCategoryClick = onCategoryClick,
            onSeeAllClick = onSeeAllSpendingClick
        )
    }
}

// ==========================================
// Shared Containers & Progress Bar
// ==========================================
@Composable
private fun TitaniumCard(
    modifier: Modifier = Modifier,
    contentPadding: Dp = 22.dp,
    content: @Composable ColumnScope.() -> Unit
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
                .padding(contentPadding),
            verticalArrangement = Arrangement.spacedBy(18.dp),
            content = content
        )
    }
}

@Composable
private fun InsetSurface(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(18.dp),
    color: Color = MaterialTheme.colorScheme.surfaceContainerLow,
    borderAlpha: Float = 0.25f,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier,
        shape = shape,
        color = color,
        border = BorderStroke(
            width = 0.8.dp,
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = borderAlpha)
        ),
        content = content
    )
}

@Composable
private fun ProgressMeterBar(
    fraction: Float,
    fillColor: Color,
    trackColor: Color,
    height: Dp,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(999.dp))
            .background(trackColor)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(fraction = fraction.coerceIn(0f, 1f))
                .fillMaxHeight()
                .clip(RoundedCornerShape(999.dp))
                .background(fillColor)
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
    TitaniumCard(modifier = modifier, contentPadding = 24.dp) {
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
        InsetSurface(shape = RoundedCornerShape(12.dp)) {
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
        enabled = true
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
        enabled = true
    )
    val countedExpense = rememberCountAnimation(
        targetValue = totalExpense,
        enabled = true
    )

    InsetSurface(modifier = modifier.fillMaxWidth()) {
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
// 3. Spending Breakdown
// ==========================================
@Composable
private fun SpendingBreakdownSection(
    categories: List<SpendingCategory>,
    totalBalance: Double,
    totalExpense: Double,
    isBalanceVisible: Boolean,
    onCategoryClick: (SpendingCategory) -> Unit,
    onSeeAllClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val safeExpense = totalExpense.coerceAtLeast(0.0)
    val budgetSnapshot = remember(totalBalance, safeExpense) {
        BudgetSnapshot.from(totalBalance = totalBalance, totalExpense = safeExpense)
    }
    val sortedCategories = remember(categories, safeExpense) {
        val rawSum = categories.sumOf { it.amount.coerceAtLeast(0.0) }
        val scale = if (rawSum > 0.0 && safeExpense > 0.0) safeExpense / rawSum else 1.0
        categories
            .map { category ->
                category.copy(amount = category.amount.coerceAtLeast(0.0) * scale)
            }
            .sortedByDescending { it.amount }
    }
    val topRows = remember(sortedCategories) {
        sortedCategories.take(MAX_VISIBLE_CATEGORY_ROWS)
    }
    val totalBase = if (safeExpense > 0.0) safeExpense else 1.0

    var selectedCategoryId by remember { mutableStateOf<String?>(null) }
    val selectedCategory = remember(sortedCategories, selectedCategoryId) {
        sortedCategories.find { it.id == selectedCategoryId }
    }

    TitaniumCard(modifier = modifier, contentPadding = 22.dp) {
        SpendingBreakdownHeader(
            selectedCategory = selectedCategory,
            totalCategoryCount = sortedCategories.size,
            onSeeAllClick = onSeeAllClick
        )

        RemainingSpendingBanner(
            snapshot = budgetSnapshot,
            isBalanceVisible = isBalanceVisible
        )

        if (sortedCategories.isEmpty()) {
            EmptySpendingState()
        } else {
            SpendingRingOverview(
                categories = sortedCategories,
                selectedCategory = selectedCategory,
                totalBase = totalBase,
                totalExpense = safeExpense,
                remainingPercent = budgetSnapshot.remainingPercent,
                isBalanceVisible = isBalanceVisible
            )

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                topRows.forEachIndexed { index, category ->
                    val ratio = (category.amount / totalBase).toFloat().coerceIn(0f, 1f)
                    val isSelected = selectedCategoryId == category.id
                    val isDimmed = selectedCategoryId != null && !isSelected

                    SpendingCategoryRow(
                        category = category,
                        ratio = ratio,
                        isBalanceVisible = isBalanceVisible,
                        isSelected = isSelected,
                        isDimmed = isDimmed,
                        animationDelayMillis = index * 70,
                        onClick = {
                            selectedCategoryId = if (isSelected) null else category.id
                            onCategoryClick(category)
                        }
                    )

                    if (index < topRows.lastIndex) {
                        HorizontalDivider(
                            thickness = 0.6.dp,
                            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f),
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SpendingBreakdownHeader(
    selectedCategory: SpendingCategory?,
    totalCategoryCount: Int,
    onSeeAllClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = "Rincian Pengeluaran",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = if (selectedCategory != null) {
                    "Fokus: ${selectedCategory.emoji} ${selectedCategory.name}"
                } else {
                    "Top $MAX_VISIBLE_CATEGORY_ROWS kategori · $totalCategoryCount total"
                },
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )
        }

        Surface(
            shape = RoundedCornerShape(10.dp),
            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f),
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .clickable(onClick = onSeeAllClick)
        ) {
            Text(
                text = "Semua",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
            )
        }
    }
}

@Composable
private fun RemainingSpendingBanner(
    snapshot: BudgetSnapshot,
    isBalanceVisible: Boolean
) {
    val countedRemaining = rememberCountAnimation(
        targetValue = snapshot.remainingAmount,
        enabled = true,
        durationMillis = 500
    )
    val animatedUsedFraction by animateFloatAsState(
        targetValue = snapshot.usedFraction,
        animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing),
        label = "remainingUsedFraction"
    )

    val statusColor = if (snapshot.isOverSpent || snapshot.remainingPercent <= 20) {
        MaterialTheme.colorScheme.error
    } else {
        MaterialTheme.colorScheme.tertiary
    }

    InsetSurface(
        modifier = Modifier.fillMaxWidth(),
        borderAlpha = 0.28f
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        text = if (snapshot.isOverSpent) "Melebihi batas saldo" else "Sisa dana pengeluaran",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.78f)
                    )
                    Text(
                        text = if (isBalanceVisible) {
                            formatCurrency(countedRemaining, withCurrency = true)
                        } else {
                            MASKED_BALANCE
                        },
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontFeatureSettings = "tnum"
                        ),
                        fontWeight = FontWeight.Bold,
                        color = statusColor,
                        maxLines = 1,
                        softWrap = false,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = statusColor.copy(alpha = 0.12f)
                ) {
                    Text(
                        text = "${snapshot.remainingPercent}% tersisa",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontFeatureSettings = "tnum"
                        ),
                        fontWeight = FontWeight.SemiBold,
                        color = statusColor,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            ProgressMeterBar(
                fraction = animatedUsedFraction,
                fillColor = statusColor,
                trackColor = MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.65f),
                height = 6.dp
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isBalanceVisible) {
                        "Terpakai: ${formatCurrency(snapshot.safeExpense)}"
                    } else {
                        "Terpakai: $MASKED_CASHFLOW"
                    },
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontFeatureSettings = "tnum"
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.72f),
                    maxLines = 1,
                    softWrap = false,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = if (isBalanceVisible) {
                        "Saldo: ${formatCurrency(snapshot.safeBalance)}"
                    } else {
                        "Saldo: $MASKED_CASHFLOW"
                    },
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontFeatureSettings = "tnum"
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.72f),
                    maxLines = 1,
                    softWrap = false,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun SpendingRingOverview(
    categories: List<SpendingCategory>,
    selectedCategory: SpendingCategory?,
    totalBase: Double,
    totalExpense: Double,
    remainingPercent: Int,
    isBalanceVisible: Boolean
) {
    val trackColor = MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.7f)
    val displayAmount = selectedCategory?.amount ?: totalExpense
    val countedDisplayAmount = rememberCountAnimation(
        targetValue = displayAmount,
        enabled = true,
        durationMillis = 450
    )

    val segmentAnimatables = remember(categories.size) {
        List(categories.size) { Animatable(0f) }
    }

    LaunchedEffect(categories) {
        segmentAnimatables.forEachIndexed { index, anim ->
            anim.snapTo(0f)
            launch {
                delay(index * 85L)
                anim.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(
                        durationMillis = 600,
                        easing = FastOutSlowInEasing
                    )
                )
            }
        }
    }

    val selectionProgress by animateFloatAsState(
        targetValue = if (selectedCategory != null) 1f else 0f,
        animationSpec = tween(durationMillis = 260, easing = FastOutSlowInEasing),
        label = "ringSelectionProgress"
    )

    val categorySharePercent = remember(selectedCategory, totalBase) {
        val categoryAmount = selectedCategory?.amount ?: 0.0
        ((categoryAmount / totalBase) * 100.0).roundToInt().coerceIn(0, 100)
    }

    val centerPercent = if (selectedCategory != null) {
        categorySharePercent
    } else {
        remainingPercent.coerceIn(0, 100)
    }

    InsetSurface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surfaceContainerLow.copy(alpha = 0.65f),
        borderAlpha = 0.35f
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier.size(98.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawSpendingRing(
                        categories = categories,
                        selectedCategory = selectedCategory,
                        totalBase = totalBase,
                        selectionProgress = selectionProgress,
                        segmentProgress = { index -> segmentAnimatables.getOrNull(index)?.value ?: 1f },
                        trackColor = trackColor
                    )
                }

                RingCenterReadout(
                    selectedCategory = selectedCategory,
                    centerPercent = centerPercent,
                    isBalanceVisible = isBalanceVisible
                )
            }

            RingSummaryDetails(
                categories = categories,
                selectedCategory = selectedCategory,
                sharePercent = categorySharePercent,
                countedAmount = countedDisplayAmount,
                isBalanceVisible = isBalanceVisible,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

private fun DrawScope.drawSpendingRing(
    categories: List<SpendingCategory>,
    selectedCategory: SpendingCategory?,
    totalBase: Double,
    selectionProgress: Float,
    segmentProgress: (Int) -> Float,
    trackColor: Color
) {
    val baseStroke = 9.dp.toPx()
    val focusedStroke = 11.dp.toPx()
    val maxInset = focusedStroke / 2f

    drawArc(
        color = trackColor,
        startAngle = 0f,
        sweepAngle = 360f,
        useCenter = false,
        topLeft = Offset(maxInset, maxInset),
        size = Size(size.width - focusedStroke, size.height - focusedStroke),
        style = Stroke(width = baseStroke, cap = StrokeCap.Round)
    )

    var currentAngle = -90f
    val gapAngle = when {
        categories.size <= 1 -> 0f
        categories.size <= 4 -> 4f
        else -> 2.5f
    }

    categories.forEachIndexed { index, category ->
        val fullSweep = ((category.amount / totalBase).toFloat().coerceIn(0f, 1f)) * 360f
        val animatedSweep = fullSweep * segmentProgress(index)

        if (animatedSweep > 1f) {
            val isSelected = selectedCategory?.id == category.id
            val isDimmed = selectedCategory != null && !isSelected

            val activeStroke = when {
                selectedCategory == null -> baseStroke
                isSelected -> baseStroke + (focusedStroke - baseStroke) * selectionProgress
                else -> baseStroke - 1.5.dp.toPx() * selectionProgress
            }.coerceAtLeast(4.dp.toPx())

            val inset = activeStroke / 2f + ((focusedStroke - activeStroke) / 2f)
            val arcSize = Size(
                width = size.width - inset * 2f,
                height = size.height - inset * 2f
            )

            val circumference = (PI * arcSize.width).toFloat().coerceAtLeast(1f)
            val singleCapDegrees = ((activeStroke / 2f) / circumference) * 360f
            val innerSweep = (animatedSweep - singleCapDegrees * 2f - gapAngle).coerceAtLeast(0.5f)
            val startAngle = currentAngle + singleCapDegrees + (gapAngle / 2f)

            val targetAlpha = if (isDimmed) 0.28f else 1f
            val alpha = 1f - (1f - targetAlpha) * selectionProgress

            drawArc(
                color = category.accentColor.copy(alpha = alpha),
                startAngle = startAngle,
                sweepAngle = innerSweep,
                useCenter = false,
                topLeft = Offset(inset, inset),
                size = arcSize,
                style = Stroke(width = activeStroke, cap = StrokeCap.Round)
            )
        }

        currentAngle += fullSweep
    }
}

@Composable
private fun RingCenterReadout(
    selectedCategory: SpendingCategory?,
    centerPercent: Int,
    isBalanceVisible: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(1.dp)
    ) {
        if (selectedCategory != null) {
            Text(
                text = selectedCategory.emoji,
                fontSize = 15.sp,
                maxLines = 1,
                softWrap = false
            )
        }
        Text(
            text = if (isBalanceVisible) "$centerPercent%" else MASKED_CASHFLOW,
            style = MaterialTheme.typography.titleMedium.copy(
                fontFeatureSettings = "tnum"
            ),
            fontWeight = FontWeight.Bold,
            color = selectedCategory?.accentColor ?: MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            softWrap = false
        )
    }
}

@Composable
private fun RingSummaryDetails(
    categories: List<SpendingCategory>,
    selectedCategory: SpendingCategory?,
    sharePercent: Int,
    countedAmount: Float,
    isBalanceVisible: Boolean,
    modifier: Modifier = Modifier
) {
    val titleText = selectedCategory?.let { "${it.emoji} ${it.name}" } ?: "Total Pengeluaran"
    val subtitleText = selectedCategory?.let {
        "${it.transactionCount} transaksi · $sharePercent% dari total"
    } ?: "${categories.sumOf { it.transactionCount }} transaksi · ${categories.size} kategori"

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = titleText,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            softWrap = false,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = if (isBalanceVisible) {
                formatCurrency(countedAmount, withCurrency = true)
            } else {
                MASKED_BALANCE
            },
            style = MaterialTheme.typography.titleMedium.copy(
                fontFeatureSettings = "tnum"
            ),
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            softWrap = false,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = subtitleText,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.75f),
            maxLines = 1,
            softWrap = false,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun SpendingCategoryRow(
    category: SpendingCategory,
    ratio: Float,
    isBalanceVisible: Boolean,
    isSelected: Boolean,
    isDimmed: Boolean,
    animationDelayMillis: Int,
    onClick: () -> Unit
) {
    val countedAmount = rememberCountAnimation(
        targetValue = category.amount,
        enabled = true,
        durationMillis = 550 + animationDelayMillis
    )
    val animatedRatio by animateFloatAsState(
        targetValue = ratio,
        animationSpec = tween(
            durationMillis = 600 + animationDelayMillis,
            easing = FastOutSlowInEasing
        ),
        label = "categoryBarRatio"
    )
    val rowAlpha by animateFloatAsState(
        targetValue = if (isDimmed) 0.45f else 1f,
        animationSpec = tween(200),
        label = "rowAlpha"
    )
    val rowBackground by animateColorAsState(
        targetValue = if (isSelected) {
            MaterialTheme.colorScheme.surfaceContainerLow
        } else {
            Color.Transparent
        },
        animationSpec = tween(200),
        label = "rowBg"
    )

    val percentage = (ratio * 100).roundToInt()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(rowAlpha)
            .clip(RoundedCornerShape(14.dp))
            .background(rowBackground)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 8.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Surface(
                modifier = Modifier.size(38.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.surfaceContainerLow,
                border = BorderStroke(
                    width = 1.5.dp,
                    color = category.accentColor.copy(alpha = 0.5f)
                )
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = category.emoji,
                        fontSize = 18.sp,
                        maxLines = 1,
                        softWrap = false
                    )
                }
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = category.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    softWrap = false,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${category.transactionCount} transaksi · $percentage%",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    maxLines = 1,
                    softWrap = false,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = if (isBalanceVisible) formatCurrency(countedAmount) else MASKED_CASHFLOW,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontFeatureSettings = "tnum"
                    ),
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    softWrap = false,
                    overflow = TextOverflow.Ellipsis
                )
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = category.accentColor.copy(alpha = 0.12f)
                ) {
                    Text(
                        text = "$percentage%",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 10.sp,
                            fontFeatureSettings = "tnum"
                        ),
                        fontWeight = FontWeight.SemiBold,
                        color = category.accentColor,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 1.dp)
                    )
                }
            }
        }

        ProgressMeterBar(
            fraction = animatedRatio,
            fillColor = category.accentColor,
            trackColor = MaterialTheme.colorScheme.surfaceContainerLow,
            height = 5.dp
        )
    }
}

@Composable
private fun EmptySpendingState() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceContainerLow
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp, horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "Belum ada pengeluaran",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Catat transaksi pertama untuk melihat rincian.",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun defaultSpendingCategories(): List<SpendingCategory> = listOf(
    SpendingCategory(
        id = "food",
        name = "Makanan & Minuman",
        emoji = "🍜",
        transactionCount = 18,
        amount = 1_850_000.0,
        accentColor = MaterialTheme.colorScheme.error
    ),
    SpendingCategory(
        id = "bills",
        name = "Tagihan & Utilitas",
        emoji = "⚡",
        transactionCount = 5,
        amount = 1_550_000.0,
        accentColor = MaterialTheme.colorScheme.tertiary
    ),
    SpendingCategory(
        id = "transport",
        name = "Transportasi",
        emoji = "🚗",
        transactionCount = 12,
        amount = 1_450_000.0,
        accentColor = MaterialTheme.colorScheme.primary
    ),
    SpendingCategory(
        id = "shopping",
        name = "Belanja & Gaya Hidup",
        emoji = "🛍️",
        transactionCount = 7,
        amount = 920_000.0,
        accentColor = MaterialTheme.colorScheme.secondary
    ),
    SpendingCategory(
        id = "health",
        name = "Kesehatan",
        emoji = "🩺",
        transactionCount = 3,
        amount = 540_000.0,
        accentColor = Color(0xFF0D9488)
    ),
    SpendingCategory(
        id = "entertainment",
        name = "Hiburan & Langganan",
        emoji = "🎬",
        transactionCount = 4,
        amount = 360_000.0,
        accentColor = Color(0xFFD97706)
    )
)

// ==========================================
// 4. Loading & Error States
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
        SpendingBreakdownSkeleton(placeholderColor = placeholderColor)
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
    TitaniumCard(contentPadding = 24.dp) {
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

@Composable
private fun SpendingBreakdownSkeleton(placeholderColor: Color) {
    TitaniumCard(contentPadding = 22.dp) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(150.dp)
                    .height(18.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(placeholderColor)
            )
            Box(
                modifier = Modifier
                    .width(56.dp)
                    .height(22.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(placeholderColor)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(84.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(placeholderColor)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(116.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(placeholderColor)
        )

        repeat(MAX_VISIBLE_CATEGORY_ROWS) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(placeholderColor)
                        )
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Box(
                                modifier = Modifier
                                    .width(115.dp)
                                    .height(14.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(placeholderColor)
                            )
                            Box(
                                modifier = Modifier
                                    .width(80.dp)
                                    .height(10.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(placeholderColor)
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .width(75.dp)
                            .height(14.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(placeholderColor)
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(5.dp)
                        .clip(RoundedCornerShape(999.dp))
                        .background(placeholderColor)
                )
            }
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

// ==========================================
// 5. Formatting & Animation Helpers
// ==========================================
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