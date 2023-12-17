package com.zorrokid.mediacollector.common.util

import androidx.compose.ui.geometry.Size

class MyPoint(val x: Float, val y: Float)

fun adjustPoint(imagePoint: MyPoint, imageSize: Size, screenSize: Size): MyPoint {
    val scaleX = screenSize.width / imageSize.width
    val scaleY = screenSize.height / imageSize.height
    return MyPoint(scaleX * imagePoint.x, scaleY * imagePoint.y)
}

fun adjustSize(boundingBoxSize: Size, imageSize: Size, screenSize: Size): Size {
    val scaleX = screenSize.width / imageSize.width
    val scaleY = screenSize.height / imageSize.height
    return Size(scaleX * boundingBoxSize.width, scaleY * boundingBoxSize.height)
}