package com.example.echolingua.util

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember

class DirectionalLazyListState(
    private val lazyListState: LazyListState
) {
    private var positionY = lazyListState.firstVisibleItemScrollOffset
    private var visibleItem = lazyListState.firstVisibleItemIndex
    private var direction = ScrollDirection.Up

    val scrollDirection by derivedStateOf {
        val firstVisibleItemIndex = lazyListState.firstVisibleItemIndex
        val firstVisibleItemScrollOffset = lazyListState.firstVisibleItemScrollOffset

        // We are scrolling while first visible item hasn't changed yet
        if (firstVisibleItemIndex == visibleItem) {
            direction = if (firstVisibleItemScrollOffset > positionY + 10) {
                ScrollDirection.Down
            } else if (firstVisibleItemScrollOffset < positionY - 10) {
                ScrollDirection.Up
            } else {
                direction
            }
            positionY = firstVisibleItemScrollOffset
            direction
        } else {
            direction = if (firstVisibleItemScrollOffset > positionY + 10) {
                ScrollDirection.Down
            } else if (firstVisibleItemScrollOffset < positionY - 10) {
                ScrollDirection.Up
            } else {
                direction
            }
            positionY = firstVisibleItemScrollOffset
            visibleItem = firstVisibleItemIndex
            direction
        }
    }
}

enum class ScrollDirection {
    Up, Down
}

@Composable
fun rememberDirectionalLazyListState(
    lazyListState: LazyListState,
): DirectionalLazyListState {
    return remember {
        DirectionalLazyListState(lazyListState)
    }
}