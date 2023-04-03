package draganddrop.drag

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size

data class DraggableItem<T>(
    val list: SnapshotStateList<T>,
    val item: T,
    val size: Size = Size.Zero,
    val initialOffset: Offset = Offset.Zero,
    val draggingOffset: Offset = Offset.Zero,
)