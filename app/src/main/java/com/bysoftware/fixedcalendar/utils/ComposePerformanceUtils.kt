package com.bysoftware.fixedcalendar.utils

import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.IntSize
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import android.view.ViewTreeObserver
import androidx.compose.ui.platform.LocalConfiguration

/**
 * Utility functions to improve Compose performance
 */

/**
 * Detect if the LazyGrid is currently being scrolled
 */
@Composable
fun LazyGridState.isScrollingFlow(): State<Boolean> {
    val scrollingState = remember { mutableStateOf(false) }
    LaunchedEffect(this) {
        snapshotFlow { isScrollInProgress }
            .distinctUntilChanged()
            .collect { isScrolling ->
                scrollingState.value = isScrolling
            }
    }
    return scrollingState
}

/**
 * Detect if the view is visible
 */
@Composable
fun rememberIsVisible(): State<Boolean> {
    val view = LocalView.current
    val isVisible = remember { mutableStateOf(view.isShown) }
    
    DisposableEffect(view) {
        val listener = ViewTreeObserver.OnPreDrawListener {
            isVisible.value = view.isShown
            true
        }
        view.viewTreeObserver.addOnPreDrawListener(listener)
        onDispose {
            view.viewTreeObserver.removeOnPreDrawListener(listener)
        }
    }
    
    return isVisible
}

/**
 * Detect window size changes
 */
@Composable
fun rememberWindowSize(): State<IntSize> {
    val configuration = LocalConfiguration.current
    return remember(configuration) {
        mutableStateOf(
            IntSize(
                configuration.screenWidthDp,
                configuration.screenHeightDp
            )
        )
    }
}

/**
 * Import for ViewTreeObserver
 */

