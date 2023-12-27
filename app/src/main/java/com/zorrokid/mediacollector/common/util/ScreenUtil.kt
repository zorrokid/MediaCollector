package com.zorrokid.mediacollector.common.util

import androidx.compose.ui.geometry.Size

class MyPoint(val x: Float, val y: Float)

fun adjustPoint(imagePoint: MyPoint, imageSize: Size, screenSize: Size, imageRotation: Int): MyPoint {
    val scaleX = screenSize.width / imageSize.width
    val scaleY = screenSize.height / imageSize.height

    return if (imageRotation == 0 || imageRotation == 180) {
        MyPoint(scaleX * imagePoint.x, scaleY * imagePoint.y)
    } else {
        MyPoint(screenSize.width - scaleX * imagePoint.y, scaleY * imagePoint.x)
    }
}