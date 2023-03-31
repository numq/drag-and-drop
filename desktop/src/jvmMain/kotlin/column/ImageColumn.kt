package column

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import image.ImageItem
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageColumn(
    modifier: Modifier = Modifier,
    items: SnapshotStateList<ImageItem>,
    listState: LazyListState,
    onUpload: () -> Unit,
    itemContent: @Composable (Int, ImageItem) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    Column(
        modifier.padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.Top)
    ) {
        Card(Modifier.fillMaxWidth().clip(CircleShape).clickable { onUpload() }) {
            Icon(Icons.Rounded.Add, "upload")
        }
        LazyColumn(
            Modifier.weight(1f).draggable(rememberDraggableState { delta ->
                coroutineScope.launch {
                    listState.scrollBy(-delta)
                }
            }, Orientation.Vertical),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp),
            state = listState
        ) {
            itemsIndexed(items, key = { _, item -> item.id }) { idx, item ->
                Box(Modifier.animateItemPlacement()) {
                    itemContent(idx, item)
                }
            }
        }
    }
}