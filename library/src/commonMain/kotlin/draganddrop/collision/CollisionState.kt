package draganddrop.collision

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
fun <T> rememberCollisionState() = remember { CollisionState<T>() }

class CollisionState<T> {
    var item: T? = null
    val type: CollisionType = CollisionType.ZERO
    val overlay: List<CollisionType> = emptyList()
}