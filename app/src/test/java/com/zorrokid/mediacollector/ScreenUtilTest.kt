package com.zorrokid.mediacollector

import androidx.compose.ui.geometry.Size
import com.zorrokid.mediacollector.common.util.MyPoint
import com.zorrokid.mediacollector.common.util.adjustPoint
import com.zorrokid.mediacollector.common.util.adjustSize
import junit.framework.TestCase.assertEquals
import org.junit.Test

class ScreenUtilTest {
    @Test
    fun adjustPoint_imageAndScreenSizeEqual_pointValuesAreNotChanged() {
        val imagePoint = MyPoint(1.0f, 1.0f)
        val imageWidth = 1
        val imageHeight = 1
        val screenWidth = 1
        val screenHeight = 1
        val result = adjustPoint(imagePoint, imageWidth, imageHeight, screenWidth, screenHeight)
        assertEquals(1.0f, result.x)
        assertEquals(1.0f, result.y)
    }
    @Test
    fun adjustPoint_screenSizeIsDoubleToImageSize_pointIsScaledAccordingly() {
        val imagePoint = MyPoint(1.0f, 1.0f)
        val imageWidth = 1
        val imageHeight = 1
        val screenWidth = 2
        val screenHeight = 2
        val result = adjustPoint(imagePoint, imageWidth, imageHeight, screenWidth, screenHeight)
        assertEquals(2.0f, result.x)
        assertEquals(2.0f, result.y)
    }
    @Test
    fun adjustPoint_imageSizeIsDoubleToscreenSize_pointIsScaledAccordingly() {
        val imagePoint = MyPoint(2.0f, 2.0f)
        val imageWidth = 2
        val imageHeight = 2
        val screenWidth = 1
        val screenHeight = 1
        val result = adjustPoint(imagePoint, imageWidth, imageHeight, screenWidth, screenHeight)
        assertEquals(1.0f, result.x)
        assertEquals(1.0f, result.y)
    }

    @Test
    fun adjustSize_imageWidthAndScreenWidthEqual_sizeWidthIsNotChanged() {
        val size = Size(1.0f, 1.0f)
        val imageWidth = 1
        val imageHeight = 1
        val screenWidth = 1
        val screenHeight = 1
        val result = adjustSize(size, imageWidth, imageHeight, screenWidth, screenHeight)
        assertEquals(1.0f, result.width)
        assertEquals(1.0f, result.height)
    }

    @Test
    fun adjustSize_screenWidthDoubleToImageWidth_sizeIsScaledAccordingly() {
        val size = Size(1.0f, 1.0f)
        val imageWidth = 1
        val imageHeight = 1
        val screenWidth = 2
        val screenHeight = 2
        val result = adjustSize(size, imageWidth, imageHeight, screenWidth, screenHeight)
        assertEquals(2.0f, result.width)
        assertEquals(2.0f, result.height)
    }

    @Test
    fun adjustSize_imageWidthDoubleToScreenWidth_sizeIsScaledAccordingly() {
        val size = Size(2.0f, 2.0f)
        val imageWidth = 2
        val imageHeight = 2
        val screenWidth = 1
        val screenHeight = 1
        val result = adjustSize(size, imageWidth, imageHeight, screenWidth, screenHeight)
        assertEquals(1.0f, result.width)
        assertEquals(1.0f, result.height)
    }
}