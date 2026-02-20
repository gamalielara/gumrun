package com.example.run.presentation.active_run

import android.Manifest
import android.content.Context
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.presentation.designsystem.GumrunTheme
import com.example.core.presentation.designsystem.StartIcon
import com.example.core.presentation.designsystem.StopIcon
import com.example.core.presentation.designsystem.components.GumrunActionButton
import com.example.core.presentation.designsystem.components.GumrunDialog
import com.example.core.presentation.designsystem.components.GumrunFloatingActionButton
import com.example.core.presentation.designsystem.components.GumrunOutlinedActionButton
import com.example.core.presentation.designsystem.components.GumrunScaffold
import com.example.core.presentation.designsystem.components.GumrunToolbar
import com.example.run.presentation.R
import com.example.run.presentation.active_run.components.RunDataCard
import com.example.run.presentation.active_run.maps.TrackerMap
import com.example.run.presentation.active_run.service.ActiveRunService
import com.example.run.presentation.util.hasLocationPermission
import com.example.run.presentation.util.hasNotiPermission
import com.example.run.presentation.util.shouldShowLocationPermissionRationale
import com.example.run.presentation.util.shouldShowNotificationPermissionRationale
import com.google.maps.android.compose.GoogleMapComposable
import org.koin.androidx.compose.koinViewModel

@Composable

fun ActiveRunScreenRoot(
    viewModel: ActiveRunViewModel = koinViewModel(),
    onServiceToggle: (isServiceRunning: Boolean) -> Unit,
) {
    ActiveRunScreen(
        state = viewModel.state,
        onServiceToggle = onServiceToggle,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@GoogleMapComposable
private fun ActiveRunScreen(
    state: ActiveRunState,
    onServiceToggle: (isServiceRunning: Boolean) -> Unit,
    onAction: (ActiveRunAction) -> Unit
) {
    val context = LocalContext.current

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { perms ->
        val hasCourseLocationPermission = perms[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        val hasFineLocationPermission = perms[Manifest.permission.ACCESS_FINE_LOCATION] == true
        val hasNotiPermission =
            if (Build.VERSION.SDK_INT >= 33) perms[Manifest.permission.POST_NOTIFICATIONS] == true else true

        val activity = context as ComponentActivity
        val showLocationRationale = activity.shouldShowLocationPermissionRationale()
        val showNotiRationale = activity.shouldShowNotificationPermissionRationale()

        onAction(
            ActiveRunAction.SubmitLocationInfo(
                acceptedLocationPermission = hasCourseLocationPermission && hasFineLocationPermission,
                showLocationRationale = showLocationRationale
            )
        )

        onAction(
            ActiveRunAction.SubmitNotificationPermissionInfo(
                acceptedNotiPermission = hasNotiPermission, showNotiRationale = showNotiRationale
            )
        )
    }

    LaunchedEffect(key1 = true) {
        val activity = context as ComponentActivity
        val showLocationRationale = activity.shouldShowLocationPermissionRationale()
        val showNotiRationale = activity.shouldShowNotificationPermissionRationale()

        onAction(
            ActiveRunAction.SubmitLocationInfo(
                acceptedLocationPermission = context.hasLocationPermission(),
                showLocationRationale = showLocationRationale
            )
        )

        onAction(
            ActiveRunAction.SubmitNotificationPermissionInfo(
                acceptedNotiPermission = context.hasNotiPermission(),
                showNotiRationale = showNotiRationale
            )
        )


        // If no need to show rationale, request permissions immediately
        /*
        *   Rationale will only shown once, when the user reject permission.
        *   Once rejected twice, rationale will not be shown again.
        */
        if (!showLocationRationale && !showNotiRationale) {
            permissionLauncher.requestGumrunPermissions(context)
        }
    }

    LaunchedEffect(key1 = state.isRunFinished) {
        if (state.isRunFinished) {
            onServiceToggle(false)
        }
    }

    LaunchedEffect(key1 = state.shouldTrack) {
        if (context.hasLocationPermission() && state.shouldTrack && !ActiveRunService.isServiceActive) {
            onServiceToggle(true)
        }
    }

    GumrunScaffold(withGradient = false, topAppBar = {
        GumrunToolbar(
            showBackButton = true,
            title = stringResource(R.string.active_run),
            onBackClick = {
                onAction(ActiveRunAction.OnBackClick)
            },
        )
    }, floatingActionButton = {
        GumrunFloatingActionButton(
            icon = if (state.shouldTrack) StopIcon else StartIcon,
            onClick = {
                onAction(ActiveRunAction.OnToggleRunClick)
            },
            iconSize = 20.dp,
            contentDescription = if (state.shouldTrack) stringResource(R.string.pause_run)
            else stringResource(R.string.start_run)
        )
    }) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            TrackerMap(
                isRunFinished = state.isRunFinished,
                currentLocation = state.currentLocation,
                locations = state.runData.locations,
                onSnapshot = {},
                modifier = Modifier.fillMaxSize()
            )
            RunDataCard(
                elapsedTime = state.elapsedTime,
                runData = state.runData,
                modifier = Modifier
                    .padding(16.dp)
                    .padding(padding)
                    .fillMaxWidth()
            )
        }
    }

    // Showing run result
    if (!state.shouldTrack && state.hasStartedRunning) {
        GumrunDialog(
            title = stringResource(R.string.run_paused),
            onDismiss = { onAction(ActiveRunAction.OnResumeRunClick) },
            description = stringResource(R.string.run_paused_desc),
            primaryButton = {
                GumrunActionButton(
                    text = stringResource(R.string.resume),
                    isLoading = false,
                    onClick = { onAction(ActiveRunAction.OnResumeRunClick) },
                    modifier = Modifier.weight(1f)
                )

            },
            secondaryButton = {
                GumrunOutlinedActionButton(
                    text = stringResource(R.string.finish),
                    isLoading = state.isRunSaving,
                    onClick = {
                        onAction(ActiveRunAction.OnFinishRunClick)
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        )
    }

    if (state.showLocationRationale || state.showNotificationRationale) {
        GumrunDialog(
            title = stringResource(R.string.permission_required),
            onDismiss = { /* Normal dismissing not allowed for permission */ },
            description = when {
                state.showLocationRationale && state.showNotificationRationale -> {
                    stringResource(id = R.string.location_notification_rationale)
                }

                state.showLocationRationale -> {
                    stringResource(id = R.string.location_rationale)
                }

                else -> {
                    stringResource(id = R.string.notification_rationale)
                }
            },
            primaryButton = {
                GumrunOutlinedActionButton(
                    text = stringResource(id = R.string.okay),
                    isLoading = false,
                    modifier = Modifier,
                    onClick = {
                        onAction(ActiveRunAction.DismissRationaleDialog)
                        permissionLauncher.requestGumrunPermissions(context)
                    })
            },
            modifier = Modifier,
        )
    }
}

private fun ActivityResultLauncher<Array<String>>.requestGumrunPermissions(
    context: Context,
) {
    val hasLocationPermission = context.hasLocationPermission()
    val hasNotiPermission = context.hasNotiPermission()

    val locationPermissions = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION
    )
    val notiPermission =
        if (Build.VERSION.SDK_INT >= 33) arrayOf(Manifest.permission.POST_NOTIFICATIONS) else emptyArray()

    when {
        !hasLocationPermission && !hasNotiPermission -> {
            launch(locationPermissions + notiPermission)
        }

        !hasLocationPermission -> launch(locationPermissions)
        !hasNotiPermission -> launch(notiPermission)
    }
}

@Preview
@Composable
private fun ActiveRunScreenRootPreview() {
    GumrunTheme() {
        ActiveRunScreen(
            state = ActiveRunState(), onAction = {}, onServiceToggle = {})
    }
}