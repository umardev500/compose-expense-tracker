package com.umar.expensetracker.feature.plan.presentation

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearWavyProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.umar.expensetracker.ui.icon.HeroIcons

private const val DEFAULT_MAX_BUDGETS_SHOWN = 4
private const val HIGH_ALLOCATION_THRESHOLD = 0.85f
private const val FULL_ALLOCATION_THRESHOLD = 1.0f

@Immutable
data class GoalItemUi(
    val id: String,
    val title: String,
    val subtitle: String,
    val currentAmountText: String,
    val targetAmountText: String,
    val remainingAmountText: String,
    val progress: Float,
    val emoji: String = "🎯"
) {
    val safeProgress: Float = progress.coerceIn(0f, FULL_ALLOCATION_THRESHOLD)
    val percentInt: Int = (safeProgress * 100f).toInt()
}

@Immutable
data class BudgetItemUi(
    val id: String,
    val categoryName: String,
    val usedAmountText: String,
    val allocatedAmountText: String,
    val remainingAmountText: String,
    val progress: Float,
    val emoji: String = "💰"
) {
    val safeProgress: Float = progress.coerceIn(0f, FULL_ALLOCATION_THRESHOLD)
    val percentInt: Int = (safeProgress * 100f).toInt()
    val isFullyAllocatedUsed: Boolean = safeProgress >= FULL_ALLOCATION_THRESHOLD
}

@Immutable
data class PlanUiState(
    val periodLabel: String = "November 2026",
    val totalAllocatedSummaryText: String = "Rp5.500.000",
    val totalRemainingSummaryText: String = "Sisa Rp1.250.000",
    val highlightedGoal: GoalItemUi? = GoalItemUi(
        id = "goal_1",
        title = "Dana Darurat",
        subtitle = "Target tercapai · Mar 2027",
        currentAmountText = "Rp18.500.000",
        targetAmountText = "Rp25.000.000",
        remainingAmountText = "Kurang Rp6.500.000",
        progress = 0.74f,
        emoji = "🛡️"
    ),
    val budgets: List<BudgetItemUi> = listOf(
        BudgetItemUi(
            id = "budget_food",
            categoryName = "Makan & Minum",
            usedAmountText = "Rp1.650.000",
            allocatedAmountText = "Rp2.500.000",
            remainingAmountText = "Sisa Rp850.000",
            progress = 0.66f,
            emoji = "🍜"
        ),
        BudgetItemUi(
            id = "budget_transport",
            categoryName = "Transportasi",
            usedAmountText = "Rp720.000",
            allocatedAmountText = "Rp1.000.000",
            remainingAmountText = "Sisa Rp280.000",
            progress = 0.72f,
            emoji = "🛵"
        ),
        BudgetItemUi(
            id = "budget_bills",
            categoryName = "Tagihan & Listrik",
            usedAmountText = "Rp1.080.000",
            allocatedAmountText = "Rp1.200.000",
            remainingAmountText = "Sisa Rp120.000",
            progress = 0.90f,
            emoji = "⚡"
        ),
        BudgetItemUi(
            id = "budget_shopping",
            categoryName = "Belanja Harian",
            usedAmountText = "Rp800.000",
            allocatedAmountText = "Rp800.000",
            remainingAmountText = "Terpakai penuh",
            progress = 1.0f,
            emoji = "🛍️"
        )
    ),
    val totalBudgetCount: Int = 6,
    val maxBudgetsShown: Int = DEFAULT_MAX_BUDGETS_SHOWN
)

private data class BudgetVisualStyle(
    val accentColor: Color,
    val emojiContainerColor: Color
)

@Composable
private fun rememberBudgetVisualStyle(budget: BudgetItemUi): BudgetVisualStyle {
    val colorScheme = MaterialTheme.colorScheme
    return remember(budget.isFullyAllocatedUsed, budget.safeProgress, colorScheme) {
        when {
            budget.isFullyAllocatedUsed -> BudgetVisualStyle(
                accentColor = colorScheme.secondary,
                emojiContainerColor = colorScheme.secondaryContainer.copy(alpha = 0.5f)
            )
            budget.safeProgress >= HIGH_ALLOCATION_THRESHOLD -> BudgetVisualStyle(
                accentColor = colorScheme.tertiary,
                emojiContainerColor = colorScheme.tertiaryContainer.copy(alpha = 0.45f)
            )
            else -> BudgetVisualStyle(
                accentColor = colorScheme.primary,
                emojiContainerColor = colorScheme.primaryContainer.copy(alpha = 0.45f)
            )
        }
    }
}

@Composable
fun PlanContent(
    uiState: PlanUiState = PlanUiState(),
    modifier: Modifier = Modifier,
    onAddPlanClick: () -> Unit = {},
    onGoalClick: (GoalItemUi) -> Unit = {},
    onSeeAllGoalsClick: () -> Unit = {},
    onBudgetClick: (BudgetItemUi) -> Unit = {},
    onSeeAllBudgetsClick: () -> Unit = {}
) {
    val visibleBudgets = remember(uiState.budgets, uiState.maxBudgetsShown) {
        uiState.budgets.take(uiState.maxBudgetsShown.coerceAtLeast(0))
    }
    val extraBudgetsCount = (uiState.totalBudgetCount - visibleBudgets.size).coerceAtLeast(0)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        PlanHeader(
            periodLabel = uiState.periodLabel,
            onAddClick = onAddPlanClick
        )

        HighlightedGoalSection(
            highlightedGoal = uiState.highlightedGoal,
            onSeeAllClick = onSeeAllGoalsClick,
            onGoalClick = onGoalClick,
            onCreateGoalClick = onAddPlanClick
        )

        BudgetAllocationSection(
            totalAllocatedText = uiState.totalAllocatedSummaryText,
            totalRemainingText = uiState.totalRemainingSummaryText,
            visibleBudgets = visibleBudgets,
            extraBudgetsCount = extraBudgetsCount,
            onSeeAllClick = onSeeAllBudgetsClick,
            onBudgetClick = onBudgetClick
        )

        DeveloperInfoSection()
    }
}

@Composable
private fun PlanHeader(
    periodLabel: String,
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            SingleLineText(
                text = "Rencana",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            SingleLineText(
                text = "Target tabungan & alokasi anggaran · $periodLabel",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )
        }

        Surface(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .clickable(role = Role.Button, onClick = onAddClick),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surfaceContainerLow,
            border = BorderStroke(
                width = 0.8.dp,
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f)
            )
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = HeroIcons.Plus,
                    contentDescription = "Tambah rencana",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(17.dp)
                )
            }
        }
    }
}

@Composable
private fun HighlightedGoalSection(
    highlightedGoal: GoalItemUi?,
    onSeeAllClick: () -> Unit,
    onGoalClick: (GoalItemUi) -> Unit,
    onCreateGoalClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        PlanSectionHeader(
            title = "Target Utama",
            actionLabel = "Semua",
            onActionClick = onSeeAllClick
        )

        PlanSurfaceCard {
            if (highlightedGoal != null) {
                HighlightedGoalItem(
                    goal = highlightedGoal,
                    onClick = { onGoalClick(highlightedGoal) }
                )
            } else {
                EmptyGoalItem(onCreateClick = onCreateGoalClick)
            }
        }
    }
}

@Composable
private fun BudgetAllocationSection(
    totalAllocatedText: String,
    totalRemainingText: String,
    visibleBudgets: List<BudgetItemUi>,
    extraBudgetsCount: Int,
    onSeeAllClick: () -> Unit,
    onBudgetClick: (BudgetItemUi) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        PlanSectionHeader(
            title = "Anggaran Bulan Ini",
            actionLabel = "Semua",
            onActionClick = onSeeAllClick
        )

        PlanSurfaceCard {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                BudgetAllocationSummaryStrip(
                    totalAllocatedText = totalAllocatedText,
                    totalRemainingText = totalRemainingText
                )

                if (visibleBudgets.isNotEmpty() || extraBudgetsCount > 0) {
                    PlanHairlineDivider(modifier = Modifier.padding(horizontal = 4.dp))
                }

                visibleBudgets.forEachIndexed { index, budget ->
                    BudgetCircleItem(
                        budget = budget,
                        onClick = { onBudgetClick(budget) }
                    )
                    if (index < visibleBudgets.lastIndex || extraBudgetsCount > 0) {
                        PlanHairlineDivider(modifier = Modifier.padding(start = 46.dp, end = 4.dp))
                    }
                }

                if (extraBudgetsCount > 0) {
                    SeeAllFooterRow(
                        label = "Lihat $extraBudgetsCount anggaran lainnya",
                        onClick = onSeeAllClick
                    )
                }
            }
        }
    }
}

@Composable
private fun PlanSurfaceCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
        ),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        content = { content() }
    )
}

@Composable
private fun PlanHairlineDivider(modifier: Modifier = Modifier) {
    HorizontalDivider(
        thickness = 0.6.dp,
        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.25f),
        modifier = modifier
    )
}

@Composable
private fun PlanSectionHeader(
    title: String,
    actionLabel: String,
    onActionClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        SingleLineText(
            text = title,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.85f)
        )

        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .clickable(role = Role.Button, onClick = onActionClick)
                .padding(horizontal = 6.dp, vertical = 3.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            SingleLineText(
                text = actionLabel,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )
            Icon(
                imageVector = HeroIcons.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(14.dp)
            )
        }
    }
}

@Composable
private fun BudgetAllocationSummaryStrip(
    totalAllocatedText: String,
    totalRemainingText: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
            SingleLineText(
                text = "Total Alokasi",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.68f)
            )
            SingleLineText(
                text = totalAllocatedText,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        SingleLineText(
            text = totalRemainingText,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun HighlightedGoalItem(
    goal: GoalItemUi,
    onClick: () -> Unit
) {
    val animatedProgress by animateFloatAsState(
        targetValue = goal.safeProgress,
        animationSpec = tween(durationMillis = 350),
        label = "goalProgress"
    )
    val density = LocalDensity.current
    val strokePx = with(density) { 3.dp.toPx() }
    val stroke = remember(strokePx) {
        Stroke(width = strokePx, cap = StrokeCap.Round)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .clickable(role = Role.Button, onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            EmojiBadge(
                emoji = goal.emoji,
                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f)
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                SingleLineText(
                    text = goal.title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                SingleLineText(
                    text = goal.subtitle,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }

            SingleLineText(
                text = "${goal.percentInt}%",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SingleLineText(
                    text = goal.currentAmountText,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                SingleLineText(
                    text = "/ ${goal.targetAmountText}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }

            LinearWavyProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f),
                stroke = stroke,
                trackStroke = stroke,
                amplitude = { FULL_ALLOCATION_THRESHOLD },
                wavelength = 45.dp,
                waveSpeed = 0.dp
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SingleLineText(
                    text = goal.remainingAmountText,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.72f)
                )
                SingleLineText(
                    text = "Detail",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.65f)
                )
            }
        }
    }
}

@Composable
private fun EmptyGoalItem(
    onCreateClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .clickable(role = Role.Button, onClick = onCreateClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        EmojiBadge(
            emoji = "🎯",
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f)
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            SingleLineText(
                text = "Belum ada target dipilih",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
            SingleLineText(
                text = "Ketuk untuk memilih atau membuat target tabungan",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )
        }

        Icon(
            imageVector = HeroIcons.ChevronRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.55f),
            modifier = Modifier.size(16.dp)
        )
    }
}

@Composable
private fun BudgetCircleItem(
    budget: BudgetItemUi,
    onClick: () -> Unit
) {
    val animatedProgress by animateFloatAsState(
        targetValue = budget.safeProgress,
        animationSpec = tween(durationMillis = 300),
        label = "budgetCircleProgress"
    )
    val style = rememberBudgetVisualStyle(budget)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .clickable(role = Role.Button, onClick = onClick)
            .padding(horizontal = 4.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        EmojiBadge(
            emoji = budget.emoji,
            containerColor = style.emojiContainerColor
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            SingleLineText(
                text = budget.categoryName,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
            SingleLineText(
                text = "${budget.usedAmountText} / ${budget.allocatedAmountText} · ${budget.remainingAmountText}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.72f)
            )
        }

        BudgetCircleProgress(
            progress = animatedProgress,
            percentText = "${budget.percentInt}%",
            indicatorColor = style.accentColor,
            trackColor = MaterialTheme.colorScheme.surfaceContainerLow
        )
    }
}

@Composable
private fun SeeAllFooterRow(
    label: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .clickable(role = Role.Button, onClick = onClick)
            .padding(horizontal = 4.dp, vertical = 11.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        SingleLineText(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
        )
        Icon(
            imageVector = HeroIcons.ChevronRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.55f),
            modifier = Modifier.size(16.dp)
        )
    }
}

@Composable
private fun EmojiBadge(
    emoji: String,
    containerColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(34.dp)
            .clip(CircleShape)
            .background(containerColor),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = emoji,
            fontSize = 16.sp,
            maxLines = 1,
            softWrap = false
        )
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun BudgetCircleProgress(
    progress: Float,
    percentText: String,
    indicatorColor: Color,
    trackColor: Color,
    modifier: Modifier = Modifier,
    size: Dp = 52.dp,
    strokeWidth: Dp = 3.dp
) {
    val density = LocalDensity.current
    val strokePx = with(density) { strokeWidth.toPx() }
    val stroke = remember(strokePx) {
        Stroke(width = strokePx, cap = StrokeCap.Round)
    }

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        CircularWavyProgressIndicator(
            progress = { progress.coerceIn(0f, FULL_ALLOCATION_THRESHOLD) },
            modifier = Modifier.fillMaxSize(),
            color = indicatorColor,
            trackColor = trackColor,
            stroke = stroke,
            trackStroke = stroke,
            amplitude = { FULL_ALLOCATION_THRESHOLD },
            wavelength = 15.dp,
            waveSpeed = 0.dp
        )

        SingleLineText(
            text = percentText,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            color = indicatorColor
        )
    }
}

@Composable
private fun SingleLineText(
    text: String,
    style: androidx.compose.ui.text.TextStyle,
    color: Color,
    modifier: Modifier = Modifier,
    fontWeight: FontWeight? = null
) {
    Text(
        text = text,
        style = style,
        fontWeight = fontWeight,
        color = color,
        maxLines = 1,
        softWrap = false,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier
    )
}

// ==========================================
// 3c. Developer Info
// ==========================================
@Composable
private fun DeveloperInfoSection(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        Text(
            text = "Umar",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        Text(
            text = "Software Engineer  |  Tech Enthusiast",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.72f),
            textAlign = TextAlign.Center
        )
        Text(
            text = "I love you Ririn ❤",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.error.copy(alpha = 0.85f),
            textAlign = TextAlign.Center
        )
    }
}