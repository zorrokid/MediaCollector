package com.zorrokid.mediacollector.common.util

import androidx.compose.ui.geometry.Size

class MyPoint(val x: Float, val y: Float)

fun adjustPoint(imagePoint: MyPoint, imageWidth: Int, imageHeight: Int, screenWidth: Int, screenHeight: Int): MyPoint {
    val x1 = (screenWidth.toFloat() / imageWidth.toFloat()) * imagePoint.x
    val y1 = (screenHeight.toFloat() / imageHeight.toFloat()) * imagePoint.y
    return MyPoint(x1, y1)
}

fun adjustSize(size: Size, imageWidth: Int, imageHeight: Int, screenWidth: Int, screenHeight: Int): Size {
    val x1 = (screenWidth.toFloat() / imageWidth.toFloat()) * size.width
    val y1 = (screenHeight.toFloat() / imageHeight.toFloat()) * size.height

    return Size(x1, y1)
}