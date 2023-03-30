package com.numq.draganddrop.drag

import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot

@Composable
fun Modifier.dragTarget(
    onOffset: (Offset) -> Unit,
    onDragStart: (Size, Offset) -> Unit,
    onDragCancel: () -> Unit,
    onDragEnd: () -> Unit,
): Modifier {

    val translation = remember { mutableStateOf(Offset.Zero) }
    val size = remember { mutableStateOf(Size.Zero) }
    val offset = remember { mutableStateOf(Offset.Zero) }

    return alpha(if (offset.value != Offset.Zero) 0f else 1f)
        .onGloballyPositioned {
            size.value = it.boundsInRoot().run { Size(width, height) }
            translation.value = it.positionInRoot().run { Offset(x, y) }
        }
        .pointerInput(Unit) {
            detectDragGesturesAfterLongPress(onDragStart = {
                onDragStart(size.value, translation.value)
            }, onDragCancel = {
                onOffset(Offset.Zero)
                offset.value = Offset.Zero
                onDragCancel()
            }, onDragEnd = {
                onDragEnd()
                onOffset(Offset.Zero)
                offset.value = Offset.Zero
            }) { _, (x, y) ->
                offset.value += Offset(x, y)
                onOffset(offset.value)
            }
        }
}