package com.example.core.presentation.designsystem.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.presentation.designsystem.AnalyticsIcon
import com.example.core.presentation.designsystem.AppFont
import com.example.core.presentation.designsystem.ArrowLeftIcon
import com.example.core.presentation.designsystem.GumrunGreen
import com.example.core.presentation.designsystem.GumrunTheme
import com.example.core.presentation.designsystem.LogoIcon
import com.example.core.presentation.designsystem.R
import com.example.core.presentation.designsystem.components.util.DropdownMenuItem


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GumrunToolbar(
    showBackButton: Boolean,
    modifier: Modifier = Modifier,
    title: String,
    menuItems: List<DropdownMenuItem> = emptyList<DropdownMenuItem>(),
    onMenuItemClick: (index: Int) -> Unit = {},
    onBackClick: () -> Unit = {},
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
    startContent: (@Composable () -> Unit)? = null,
) {
    var isDropdownOpen by rememberSaveable {
        mutableStateOf(false)
    }

    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                startContent?.invoke()
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontFamily = AppFont
                )
            }
        },
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
        ),
        navigationIcon = {
            if (showBackButton) {
                IconButton(
                    onClick = onBackClick
                ) {
                    Icon(
                        imageVector = ArrowLeftIcon,
                        contentDescription = stringResource(R.string.go_back),
                    )

                }
            }
        },
        actions = {
            if (menuItems.isNotEmpty()) {
                // Anchor for dropdown menu
                Box {
                    DropdownMenu(
                        expanded = isDropdownOpen,
                        onDismissRequest = { isDropdownOpen = false },
                    ) {
                        menuItems.forEachIndexed { index, item ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .clickable {
                                        onMenuItemClick.invoke(index)
                                    }
                                    .padding(16.dp)
                            ) {
                                Icon(imageVector = item.icon, contentDescription = item.title)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = item.title)
                            }
                        }
                    }
                    IconButton(
                        onClick = { isDropdownOpen = true }
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = stringResource(R.string.open_menu),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun Preview() {
    GumrunTheme {
        GumrunToolbar(
            showBackButton = true,
            modifier = Modifier.fillMaxWidth(),
            title = "Gumrun",
            onBackClick = {},
            startContent = {
                Icon(
                    imageVector = LogoIcon, contentDescription = null, tint = GumrunGreen
                )
            },
            menuItems = listOf(
                DropdownMenuItem(
                    icon = AnalyticsIcon,
                    title = "Analytics"
                ),
                DropdownMenuItem(
                    icon = AnalyticsIcon,
                    title = "Analytics"
                )
            )
        )
    }
}