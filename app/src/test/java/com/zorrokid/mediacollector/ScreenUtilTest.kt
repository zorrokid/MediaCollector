package com.zorrokid.mediacollector

import androidx.compose.ui.geometry.Size
import com.zorrokid.mediacollector.common.util.MyPoint
import com.zorrokid.mediacollector.common.util.adjustPoint
import junit.framework.TestCase.assertEquals
import org.junit.Test

class ScreenUtilTest {
    @Test
    fun adjustPoint_imageAndScreenSizeEqual_pointValuesAreNotChanged() {
        val imagePoint = MyPoint(1.0f, 1.0f)
        val imageSize = Size(1.0f, 1.0f)
        val screenSize = Size(1.0f, 1.0f)
        val imageRotation = 0
        val result = adjustPoint(imagePoint, imageSize, screenSize, imageRotation)
        assertEquals(1.0f, result.x)
        assertEquals(1.0f, result.y)
    }
    @Test
    fun adjustPoint_screenSizeIsDoubleToImageSize_pointIsScaledAccordingly() {
        val imagePoint = MyPoint(1.0f, 1.0f)
        val imageSize = Size(1.0f, 1.0f)
        val screenSize = Size(2.0f, 2.0f)
        val imageRotation = 0
        val result = adjustPoint(imagePoint, imageSize, screenSize, imageRotation)
        assertEquals(2.0f, result.x)
        assertEquals(2.0f, result.y)
    }
    @Test
    fun adjustPoint_imageSizeIsDoubleToScreenSize_pointIsScaledAccordingly() {
        val imagePoint = MyPoint(2.0f, 2.0f)
        val imageSize = Size(2.0f, 2.0f)
        val screenSize = Size(1.0f, 1.0f)
        val imageRotation = 0
        val result = adjustPoint(imagePoint, imageSize, screenSize, imageRotation)
        assertEquals(1.0f, result.x)
        assertEquals(1.0f, result.y)
    }

    @Test
    fun adjustPoint_screenWidthIsDoubleToImageWidthAndImageIsRotated90deg_YShouldSwitchToXAndScaledAccordingly() {
        val imagePoint = MyPoint(0.0f, 0.5f)
        val imageSize = Size(1.0f, 1.0f)
        val screenSize = Size(2.0f, 1.0f)
        val imageRotation = 90
        val result = adjustPoint(imagePoint, imageSize, screenSize, imageRotation)
        assertEquals(1.0f, result.x)
        assertEquals(0.0f, result.y)
    }
}