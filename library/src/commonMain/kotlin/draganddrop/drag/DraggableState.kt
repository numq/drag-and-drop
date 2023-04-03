package draganddrop.drag

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size


@Composable
fun <T> rememberDraggableState() = remember { DraggableState<T>() }

class DraggableState<T> {
    var item: T? = null
    var size: Size = Size.Zero
    var position: Offset = Offset.Zero
    var offset: Offset = Offset.Zero
}