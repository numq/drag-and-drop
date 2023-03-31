package com.numq.draganddrop.collision

enum class CollisionType {
    ZERO, BOX, LEFT, TOP, BOTTOM, RIGHT;

    companion object {
        val Container = listOf(ZERO, BOX)
        val Horizontal = listOf(ZERO, LEFT, RIGHT)
        val Vertical = listOf(ZERO, TOP, BOTTOM)
    }
}