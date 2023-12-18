package com.zorrokid.mediacollector.common.composable

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun BarcodeInput(
    onBarcodeChange: (String) -> Unit,
    onScanBarcodeClick: () -> Unit,
    barcode: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BarcodeField(barcode, onBarcodeChange, modifier)
        BarcodeScanButton(onScanBarcodeClick, modifier)
    }
}

@Composable
fun TextRecognitionInput(
    onTextChange: (String) -> Unit,
    onScanText: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    @StringRes placeHolder: Int,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        NameField(
            text = text,
            onTextChange = onTextChange,
            placeHolder = placeHolder,
            modifier = modifier
        )
        TextRecognitionButton(onScanText, modifier)
    }
}