package draganddrop.collision

import androidx.compose.runtime.snapshots.SnapshotStateList

data class Collision<T>(
    val type: CollisionType = CollisionType.ZERO,
    val list: SnapshotStateList<T>? = null,
    val item: T? = null,
)