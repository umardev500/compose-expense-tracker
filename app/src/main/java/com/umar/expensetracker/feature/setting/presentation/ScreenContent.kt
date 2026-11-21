package com.umar.expensetracker.feature.setting.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.umar.expensetracker.R
import com.umar.expensetracker.ui.icon.HeroIcons

data class SettingUiState(
    val userName: String = "Umar",
    val userSubtitle: String = "Dompet Pribadi · IDR",
    val walletCount: Int = 3,
    val totalWalletBalanceText: String = "Rp12.500.000",
    val currencyCode: String = "IDR (Rp)",
    val isDarkTheme: Boolean = false,
    val isHideBalanceByDefault: Boolean = false,
    val isBudgetAlertEnabled: Boolean = true,
    val isBiometricLockEnabled: Boolean = false,
    val appVersion: String = "1.0.0"
)

@Composable
fun SettingContent(
    uiState: SettingUiState = SettingUiState(),
    modifier: Modifier = Modifier,
    onEditProfileClick: () -> Unit = {},
    onWalletsClick: () -> Unit = {},
    onCurrencyClick: () -> Unit = {},
    onToggleDarkTheme: (Boolean) -> Unit = {},
    onToggleHideBalanceByDefault: (Boolean) -> Unit = {},
    onToggleBudgetAlert: (Boolean) -> Unit = {},
    onToggleBiometricLock: (Boolean) -> Unit = {},
    onExportDataClick: () -> Unit = {},
    onResetDataClick: () -> Unit = {},
    onAboutAppClick: () -> Unit = {}
) {
    var isConfirmingReset by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        SettingPureHeader()

        SettingCardBlock(sectionTitle = "Akun & Dompet") {
            SettingProfileItem(
                userName = uiState.userName,
                userSubtitle = uiState.userSubtitle,
                onClick = onEditProfileClick
            )
            SettingDivider()
            SettingActionItem(
                title = "Dompet & Saldo",
                subtitle = if (uiState.isHideBalanceByDefault) {
                    "Total ${uiState.walletCount} dompet · Disembunyikan"
                } else {
                    "Total ${uiState.totalWalletBalanceText} · ${uiState.walletCount} dompet"
                },
                valueText = "${uiState.walletCount} dompet",
                icon = HeroIcons.Wallet,
                iconTint = MaterialTheme.colorScheme.primary,
                iconBg = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f),
                onClick = onWalletsClick
            )
            SettingDivider()
            SettingActionItem(
                title = "Mata Uang Utama",
                subtitle = "Format tampilan nominal transaksi",
                valueText = uiState.currencyCode,
                icon = HeroIcons.CurrencyDollar,
                iconTint = MaterialTheme.colorScheme.tertiary,
                iconBg = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.45f),
                onClick = onCurrencyClick
            )
        }

        SettingCardBlock(sectionTitle = "Tampilan & Keamanan") {
            SettingToggleItem(
                title = "Mode Gelap",
                subtitle = "Sesuaikan pencahayaan layar",
                checked = uiState.isDarkTheme,
                icon = if (uiState.isDarkTheme) HeroIcons.Moon else HeroIcons.Sun,
                iconTint = MaterialTheme.colorScheme.secondary,
                iconBg = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.45f),
                onCheckedChange = onToggleDarkTheme
            )
            SettingDivider()
            SettingToggleItem(
                title = "Sembunyikan Saldo Awal",
                subtitle = "Samarkan nominal saat membuka dasbor",
                checked = uiState.isHideBalanceByDefault,
                icon = HeroIcons.EyeSlash,
                iconTint = MaterialTheme.colorScheme.tertiary,
                iconBg = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.45f),
                onCheckedChange = onToggleHideBalanceByDefault
            )
            SettingDivider()
            SettingToggleItem(
                title = "Notifikasi Batas Anggaran",
                subtitle = "Ingatkan saat sisa pengeluaran menipis",
                checked = uiState.isBudgetAlertEnabled,
                icon = HeroIcons.Bell,
                iconTint = MaterialTheme.colorScheme.primary,
                iconBg = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f),
                onCheckedChange = onToggleBudgetAlert
            )
            SettingDivider()
            SettingToggleItem(
                title = "Kunci Biometrik",
                subtitle = "Gunakan sidik jari atau kunci layar",
                checked = uiState.isBiometricLockEnabled,
                icon = HeroIcons.ShieldCheck,
                iconTint = MaterialTheme.colorScheme.tertiary,
                iconBg = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.45f),
                onCheckedChange = onToggleBiometricLock
            )
        }

        SettingCardBlock(sectionTitle = "Data & Aplikasi") {
            SettingActionItem(
                title = "Ekspor Riwayat Transaksi",
                subtitle = "Simpan data transaksi ke berkas",
                valueText = "CSV",
                icon = HeroIcons.ArrowUpTray,
                iconTint = MaterialTheme.colorScheme.tertiary,
                iconBg = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.45f),
                onClick = {
                    isConfirmingReset = false
                    onExportDataClick()
                }
            )
            SettingDivider()
            SettingActionItem(
                title = "Tentang Aplikasi",
                subtitle = "Informasi versi dan catatan rilis",
                valueText = "v${uiState.appVersion}",
                icon = HeroIcons.InformationCircle,
                iconTint = MaterialTheme.colorScheme.onSurfaceVariant,
                iconBg = MaterialTheme.colorScheme.surfaceContainerLow,
                onClick = {
                    isConfirmingReset = false
                    onAboutAppClick()
                }
            )
            SettingDivider()
            SettingResetItem(
                isConfirming = isConfirmingReset,
                onRequestReset = { isConfirmingReset = true },
                onCancelReset = { isConfirmingReset = false },
                onConfirmReset = {
                    isConfirmingReset = false
                    onResetDataClick()
                }
            )
        }

        SettingFooter()
    }
}

@Composable
private fun SettingPureHeader(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(
            text = "Pengaturan",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            softWrap = false,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = "Kelola akun, tampilan, keamanan, dan data",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
            maxLines = 1,
            softWrap = false,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun SettingCardBlock(
    sectionTitle: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = sectionTitle,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.85f),
            modifier = Modifier.padding(horizontal = 4.dp),
            maxLines = 1,
            softWrap = false,
            overflow = TextOverflow.Ellipsis
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
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
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                content = content
            )
        }
    }
}

@Composable
private fun SettingProfileItem(
    userName: String,
    userSubtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 4.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Surface(
            modifier = Modifier.size(38.dp),
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

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = userName,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                softWrap = false,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = userSubtitle,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                maxLines = 1,
                softWrap = false,
                overflow = TextOverflow.Ellipsis
            )
        }

        Icon(
            imageVector = HeroIcons.ChevronRight,
            contentDescription = "Edit profil",
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.55f),
            modifier = Modifier.size(16.dp)
        )
    }
}

@Composable
private fun SettingActionItem(
    title: String,
    subtitle: String,
    valueText: String?,
    icon: ImageVector,
    iconTint: Color,
    iconBg: Color,
    onClick: () -> Unit,
    titleColor: Color = MaterialTheme.colorScheme.onSurface
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 4.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SettingIconBox(
            icon = icon,
            tint = iconTint,
            containerColor = iconBg
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = titleColor,
                maxLines = 1,
                softWrap = false,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                maxLines = 1,
                softWrap = false,
                overflow = TextOverflow.Ellipsis
            )
        }

        if (valueText != null) {
            Text(
                text = valueText,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                maxLines = 1,
                softWrap = false
            )
        }

        Icon(
            imageVector = HeroIcons.ChevronRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
            modifier = Modifier.size(16.dp)
        )
    }
}

@Composable
private fun SettingToggleItem(
    title: String,
    subtitle: String,
    checked: Boolean,
    icon: ImageVector,
    iconTint: Color,
    iconBg: Color,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .clickable(role = Role.Switch) { onCheckedChange(!checked) }
            .padding(horizontal = 4.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SettingIconBox(
            icon = icon,
            tint = iconTint,
            containerColor = iconBg
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                softWrap = false,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                maxLines = 1,
                softWrap = false,
                overflow = TextOverflow.Ellipsis
            )
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                checkedTrackColor = MaterialTheme.colorScheme.primary,
                uncheckedThumbColor = MaterialTheme.colorScheme.outline,
                uncheckedTrackColor = MaterialTheme.colorScheme.surfaceContainerLow
            )
        )
    }
}

@Composable
private fun SettingResetItem(
    isConfirming: Boolean,
    onRequestReset: () -> Unit,
    onCancelReset: () -> Unit,
    onConfirmReset: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        SettingActionItem(
            title = "Hapus Semua Data",
            subtitle = if (isConfirming) {
                "Tindakan ini tidak dapat dibatalkan"
            } else {
                "Bersihkan seluruh catatan transaksi lokal"
            },
            valueText = null,
            icon = HeroIcons.Trash,
            iconTint = MaterialTheme.colorScheme.error,
            iconBg = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.7f),
            titleColor = MaterialTheme.colorScheme.error,
            onClick = {
                if (isConfirming) onCancelReset() else onRequestReset()
            }
        )

        AnimatedVisibility(
            visible = isConfirming,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 50.dp, end = 4.dp, top = 2.dp, bottom = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Batal",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable(onClick = onCancelReset)
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                )
                Text(
                    text = "Ya, Hapus",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable(onClick = onConfirmReset)
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                )
            }
        }
    }
}

@Composable
private fun SettingIconBox(
    icon: ImageVector,
    tint: Color,
    containerColor: Color
) {
    Box(
        modifier = Modifier
            .size(34.dp)
            .clip(CircleShape)
            .background(containerColor),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(17.dp)
        )
    }
}

@Composable
private fun SettingDivider() {
    HorizontalDivider(
        thickness = 0.6.dp,
        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.25f),
        modifier = Modifier.padding(start = 50.dp, end = 4.dp)
    )
}

@Composable
private fun SettingFooter(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 4.dp, bottom = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp)
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