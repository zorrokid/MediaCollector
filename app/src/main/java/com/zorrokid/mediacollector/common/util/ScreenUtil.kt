package com.zorrokid.mediacollector.common.util

import androidx.compose.ui.geometry.Size

class MyPoint(val x: Float, val y: Float)

fun getRotatedImageSize(imageSize: Size, rotation: Int): Size {
    return if (rotation == 0 || rotation == 180) {
        imageSize
    } else {
        Size(imageSize.height, imageSize.width)
    }
}

fun adjustPoint(imagePoint: MyPoint, imageSize: Size, screenSize: Size, imageRotation: Int): MyPoint {
    val rotatedImageSize = getRotatedImageSize(imageSize, imageRotation)
    val scaleX = screenSize.width / rotatedImageSize.width
    val scaleY = screenSize.height / rotatedImageSize.height
    return MyPoint(scaleX * imagePoint.x, scaleY * imagePoint.y)
}

fun adjustSize(boundingBoxSize: Size, imageSize: Size, screenSize: Size, imageRotation: Int): Size {
    val rotatedImageSize = getRotatedImageSize(imageSize, imageRotation)
    val scaleX = screenSize.width / rotatedImageSize.width
    val scaleY = screenSize.height / rotatedImageSize.height
    return Size(scaleX * boundingBoxSize.width, scaleY * boundingBoxSize.height)
}