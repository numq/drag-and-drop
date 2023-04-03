package column

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Card
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import draganddrop.collision.Collision
import draganddrop.collision.CollisionType
import draganddrop.collision.collider
import draganddrop.collision.rememberCollisionState
import draganddrop.drag.DraggableItem
import draganddrop.drag.DraggableOverlay
import draganddrop.drag.dragTarget
import draganddrop.drag.rememberDraggableState
import image.ImageItem
import org.jetbrains.skia.Image
import upload.UploadDialog
import upload.UploadState
import java.util.*

@Composable
fun ColumnScreen() {

    val draggableState = rememberDraggableState<ImageItem>()

    val listCollisionState = rememberCollisionState<SnapshotStateList<ImageItem>>()

    val itemCollisionState = rememberCollisionState<ImageItem>()

    val leftColumn = remember { mutableStateListOf<ImageItem>() }
    val leftColumnListState = rememberLazyListState()

    val rightColumn = remember { mutableStateListOf<ImageItem>() }
    val rightColumnListState = rememberLazyListState()

    var collision by remember { mutableStateOf(Collision<ImageItem>()) }

    val onListCollision: (CollisionType, SnapshotStateList<ImageItem>) -> Unit = { type, list ->
        collision = when (type) {
            CollisionType.BOX -> Collision(type, list)
            CollisionType.ZERO -> Collision()
            else -> collision
        }
    }

    val onItemCollision: (CollisionType, ImageItem) -> Unit = { type, item ->
        collision = when (type) {
            CollisionType.TOP, CollisionType.BOTTOM -> collision.copy(type = type, item = item)
            CollisionType.ZERO -> collision.copy(type = type, item = null)
            else -> collision
        }
    }

    var draggableItem by remember { mutableStateOf<DraggableItem<ImageItem>?>(null) }

    val draggableCenter by remember(draggableItem) {
        derivedStateOf {
            draggableItem?.run {
                Rect(
                    Offset(initialOffset.x + draggingOffset.x, initialOffset.y + draggingOffset.y),
                    Size(size.width, size.height)
                ).center
            } ?: Offset.Zero
        }
    }

    val handleDragOffset: (Offset) -> Unit = {
        draggableItem = draggableItem?.copy(draggingOffset = it)
    }

    val handleDragStart: (SnapshotStateList<ImageItem>, ImageItem, Size, Offset) -> Unit =
        { list, item, size, offset ->
            draggableItem = DraggableItem(list, item, size, offset)
        }

    val handleDragCancel: () -> Unit = {
        draggableItem = null
    }

    val handleDragEnd: () -> Unit = {
        draggableItem?.runCatching {
            if (item != collision.item) {
                collision.list?.run {
                    list.indexOf(item).takeIf { it >= 0 }?.let { index ->
                        collision.item?.let {
                            when (collision.type) {
                                CollisionType.TOP -> {
                                    val itemToReplace = list.removeAt(index)
                                    indexOf(it).takeIf { idx -> idx >= 0 }?.coerceIn(0, size)?.let { idx ->
                                        listIterator(idx).add(itemToReplace)
                                    }
                                }
                                CollisionType.BOTTOM -> {
                                    val itemToReplace = list.removeAt(index)
                                    indexOf(it).takeIf { idx -> idx >= 0 }?.coerceIn(0, size)?.let { idx ->
                                        listIterator(idx + 1).add(itemToReplace)
                                    }
                                }
                                else -> Unit
                            }
                        } ?: collision.list?.add(list.removeAt(index))
                    }
                }
            }
        }?.onFailure { println(it.localizedMessage) }
        draggableItem = null
    }

    val (uploadState, setUploadState) = remember { mutableStateOf<UploadState<ImageItem>>(UploadState.Empty()) }

    (uploadState as? UploadState.Active)?.runCatching {
        UploadDialog(onClose = {
            setUploadState(UploadState.Empty())
        }) { files ->
            targetList.addAll(files.mapNotNull {
                runCatching {
                    ImageItem(
                        UUID.randomUUID().toString(),
                        Image.makeFromEncoded(it.bytes).toComposeImageBitmap()
                    )
                }.getOrNull()
            })
        }
    }

    Box(Modifier.fillMaxSize()) {
        Row(
            Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ImageColumn(Modifier.weight(1f).collider(draggableCenter, onCollision = {
                onListCollision(it, leftColumn)
            }), leftColumn, leftColumnListState, onUpload = {
                setUploadState(UploadState.Active(leftColumn))
            }) { idx, item ->
                Card(Modifier.aspectRatio(1f).size(64.dp)
                    .collider(draggableCenter, CollisionType.Vertical, onCollision = {
                        onItemCollision(it, item)
                    }).dragTarget(handleDragOffset, onDragStart = { size, offset ->
                        handleDragStart(leftColumn, item, size, offset)
                    }, handleDragCancel, handleDragEnd)
                ) {
                    Image(
                        item.bitmap,
                        "list item",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
            ImageColumn(Modifier.weight(1f).collider(draggableCenter, onCollision = {
                onListCollision(it, rightColumn)
            }), rightColumn, rightColumnListState, onUpload = {
                setUploadState(UploadState.Active(rightColumn))
            }) { _, item ->
                Card(Modifier.aspectRatio(1f).size(64.dp)
                    .collider(draggableCenter, CollisionType.Vertical, onCollision = {
                        onItemCollision(it, item)
                    }).dragTarget(handleDragOffset, onDragStart = { size, offset ->
                        handleDragStart(rightColumn, item, size, offset)
                    }, handleDragCancel, handleDragEnd)
                ) {
                    Image(
                        item.bitmap,
                        "list item",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
        DraggableOverlay(draggableItem) {
            Image(
                it.bitmap,
                "draggable list item",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
        draggableItem?.let {
            Canvas(Modifier.fillMaxSize()) {
                drawCircle(Color.Green, 4.dp.value, center = draggableCenter)
            }
        }
    }
}