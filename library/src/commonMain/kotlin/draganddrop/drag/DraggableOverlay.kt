package draganddrop.drag

import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp

@Composable
fun <T> DraggableOverlay(
    item: DraggableItem<T>?,
    content: @Composable (T) -> Unit,
) {
    item?.run {
        Card(
            Modifier.size(size.run { DpSize(width.dp, height.dp) })
                .offset((initialOffset.x + draggingOffset.x).dp, (initialOffset.y + draggingOffset.y).dp)
        ) {
            content(item.item)
        }
    }
}