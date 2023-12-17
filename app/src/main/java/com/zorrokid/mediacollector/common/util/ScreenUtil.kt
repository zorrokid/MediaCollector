package com.zorrokid.mediacollector.common.util

import androidx.compose.ui.geometry.Size

class MyPoint(val x: Float, val y: Float)

fun adjustPoint(imagePoint: MyPoint, imageSize: Size, screenSize: Size): MyPoint {
    val x1 = (screenSize.width / imageSize.width) * imagePoint.x
    val y1 = (screenSize.height / imageSize.height) * imagePoint.y
    return MyPoint(x1, y1)
}

fun adjustSize(boundingBoxsize: Size, imageSize: Size, screenSize: Size): Size {
    val x1 = (screenSize.width / imageSize.width) * boundingBoxsize.width
    val y1 = (screenSize.height / imageSize.height) * boundingBoxsize.height

    return Size(x1, y1)
}