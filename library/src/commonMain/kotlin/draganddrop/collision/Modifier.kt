package draganddrop.collision

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned

@Composable
fun Modifier.collider(
    target: Offset,
    constraints: List<CollisionType> = emptyList(),
    onCollision: (CollisionType) -> Unit,
): Modifier {

    val (rootRect, setRootRect) = remember { mutableStateOf(Rect.Zero) }

    val (parentRect, setParentRect) = remember { mutableStateOf(Rect.Zero) }

    val (collisionOverlay, setCollisionOverlay) = remember { mutableStateOf(CollisionType.ZERO) }

    val specifyOverlay: (CollisionType) -> Unit = {
        if (it in constraints) setCollisionOverlay(it)
    }

    val boxCollision by remember(target, rootRect) {
        derivedStateOf {
            rootRect.contains(target)
        }
    }

    val leftCollision by remember(target, rootRect) {
        derivedStateOf {
            with(rootRect) {
                Rect(left,
                    top,
                    center.x,
                    bottom)
            }.contains(target)
        }
    }

    val topCollision by remember(target, rootRect) {
        derivedStateOf {
            with(rootRect) {
                Rect(left,
                    top,
                    right,
                    center.y)
            }.contains(target)
        }
    }

    val rightCollision by remember(target, rootRect) {
        derivedStateOf {
            with(rootRect) {
                Rect(center.x,
                    top,
                    right,
                    bottom)
            }.contains(target)
        }
    }

    val bottomCollision by remember(target, rootRect) {
        derivedStateOf {
            with(rootRect) {
                Rect(left,
                    center.y,
                    right,
                    bottom)
            }.contains(target)
        }
    }

    DisposableEffect(target, rootRect, leftCollision, rightCollision, bottomCollision, topCollision) {
        if (target != Offset.Zero && rootRect != Rect.Zero) {
            if (boxCollision) onCollision(CollisionType.BOX.also(specifyOverlay))
            if (leftCollision) onCollision(CollisionType.LEFT.also(specifyOverlay))
            if (topCollision) onCollision(CollisionType.TOP.also(specifyOverlay))
            if (bottomCollision) onCollision(CollisionType.BOTTOM.also(specifyOverlay))
            if (rightCollision) onCollision(CollisionType.RIGHT.also(specifyOverlay))
        } else onCollision(CollisionType.ZERO.also(specifyOverlay))
        onDispose { specifyOverlay(CollisionType.ZERO) }
    }

    val leftRect by remember(parentRect) {
        derivedStateOf {
            with(parentRect) {
                Rect(left,
                    top,
                    center.x,
                    bottom)
            }
        }
    }

    val topRect by remember(parentRect) {
        derivedStateOf {
            with(parentRect) {
                Rect(left,
                    top,
                    right,
                    center.y)
            }
        }
    }

    val rightRect by remember(parentRect) {
        derivedStateOf {
            with(parentRect) {
                Rect(center.x,
                    top,
                    right,
                    bottom)
            }
        }
    }

    val bottomRect by remember(parentRect) {
        derivedStateOf {
            with(parentRect) {
                Rect(left,
                    center.y,
                    right,
                    bottom)
            }
        }
    }

    return onGloballyPositioned {
        setRootRect(it.boundsInRoot())
        setParentRect(it.boundsInParent())
    }.drawWithContent {
        drawContent()
        when (collisionOverlay) {
            CollisionType.BOX -> drawRect(Color.LightGray.copy(.5f), parentRect.topLeft, parentRect.size)
            CollisionType.LEFT -> drawRect(Color.LightGray.copy(.5f), leftRect.topLeft, leftRect.size)
            CollisionType.TOP -> drawRect(Color.LightGray.copy(.5f), topRect.topLeft, topRect.size)
            CollisionType.BOTTOM -> drawRect(Color.LightGray.copy(.5f), bottomRect.topLeft, bottomRect.size)
            CollisionType.RIGHT -> drawRect(Color.LightGray.copy(.5f), rightRect.topLeft, rightRect.size)
            else -> Unit
        }
    }
}