package draganddrop.drag

import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot

@Composable
fun Modifier.dragTarget(
    onOffset: (Offset) -> Unit,
    onDragStart: (Size, Offset) -> Unit,
    onDragCancel: () -> Unit,
    onDragEnd: () -> Unit,
): Modifier {

    val (dragging, setDragging) = remember { mutableStateOf(false) }
    val translation = remember { mutableStateOf(Offset.Zero) }
    val size = remember { mutableStateOf(Size.Zero) }
    val offset = remember { mutableStateOf(Offset.Zero) }

    return alpha(if (dragging) 0f else 1f)
        .layout { measurable, constraints ->
            size.value = constraints.run { Size(maxWidth.toFloat(), maxHeight.toFloat()) }
            measurable.measure(constraints).run {
                layout(width, height) { place(0, 0) }
            }
        }
        .onGloballyPositioned {
            translation.value = it.positionInRoot().run { Offset(x, y) }
        }
        .pointerInput(Unit) {
            detectDragGesturesAfterLongPress(onDragStart = {
                setDragging(true)
                onDragStart(size.value, translation.value)
            }, onDragCancel = {
                onOffset(Offset.Zero)
                offset.value = Offset.Zero
                onDragCancel()
                setDragging(false)
            }, onDragEnd = {
                onDragEnd()
                onOffset(Offset.Zero)
                offset.value = Offset.Zero
                setDragging(false)
            }) { _, (x, y) ->
                offset.value += Offset(x, y)
                onOffset(offset.value)
            }
        }
}