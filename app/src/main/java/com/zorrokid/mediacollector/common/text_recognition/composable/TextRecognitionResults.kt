package com.zorrokid.mediacollector.common.text_recognition.composable

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zorrokid.mediacollector.model.TextBlock
import com.zorrokid.mediacollector.model.TextLine

@Composable
fun TextRecognitionResultSelector(
    modifier: Modifier = Modifier,
    recognizedText: List<TextBlock>,
    onTextSelected: (List<String>) -> Unit,
) {
    val selectedTexts = remember { mutableStateOf(emptyList<String>()) }

    val showSingleWordSelection = remember {
        mutableStateOf(false)
    }

    Scaffold (
       floatingActionButton = {
            FloatingActionButton(
                onClick = { onTextSelected(selectedTexts.value) },
            ) {
                Icon(Icons.Filled.Done, "Add")
            }
        },
        content = { padding ->
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(8.dp)
            ) {
                SwitchWithLabel(
                    label = "Show single word selection",
                    checked = showSingleWordSelection.value,
                    onCheckedChange = {
                        showSingleWordSelection.value = it
                    },
                    modifier = modifier
                )
                LazyColumn (
                    modifier = modifier
                        .fillMaxSize()
                ) {
                    itemsIndexed(recognizedText) { index, textBlock ->
                        TextScanResultCard(
                            modifier = modifier,
                            textBlock,
                            onTextSelected =  {
                                Log.d("TextRecognitionResultSelector", "Selected text: $it")
                                selectedTexts.value = selectedTexts.value + it
                                Log.d("TextRecognitionResultSelector", "SelectedTexts: $selectedTexts")
                            },
                            showSingleWordSelection.value,
                        )
                    }
                }
            }
        },
    )
}

@Composable
fun SwitchWithLabel(
    modifier: Modifier = Modifier,
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Switch(checked = checked, onCheckedChange = onCheckedChange)
        Text(text = label,
            modifier = modifier.padding(horizontal = 8.dp))
    }
}

@Preview
@Composable
fun SwitchWithLabelPreview() {
    SwitchWithLabel(
        label = "Example label",
        checked = false,
        onCheckedChange = {},
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TextScanResultCard(
    modifier: Modifier = Modifier,
    textBlock: TextBlock,
    onTextSelected: (String) -> Unit,
    showSingleWordSelection: Boolean,
) {
    fun filterText(textBlock: TextBlock): String {
        return textBlock.text.lines().filter { it.isNotBlank() }.joinToString(" ")
    }
    Card(modifier = modifier) {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            if (showSingleWordSelection){
                FlowRow(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    textBlock.lines.forEach { line ->
                        line.words.forEach {
                            SingleWorldSelection(
                                text = it,
                                onSelected = onTextSelected,
                            )
                        }
                    }
                }
            } else {
                TextBlockSelection(
                    text= filterText(textBlock),
                    onSelected = onTextSelected,
                    modifier = modifier
                )
            }
        }
    }
}

@Preview
@Composable
fun TextScanResultCardPreview() {
    TextScanResultCard(
        textBlock = TextBlock("Example selection", emptyList(), emptyList()),
        onTextSelected = {},
        showSingleWordSelection = false,
    )
}
@Preview
@Composable
fun TextScanResultCardSingleWordSelectionPreview() {
    TextScanResultCard(
        textBlock = TextBlock("Example selection", listOf(TextLine(words = listOf("Hello", "World"))), emptyList()),
        onTextSelected = {},
        showSingleWordSelection = true,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SingleWorldSelection(
    text: String,
    onSelected: (String) -> Unit,
) {
    val isSelected = remember { mutableStateOf(false) }
    InputChip(
        selected = isSelected.value,
        onClick = {
            isSelected.value = !isSelected.value
            onSelected(text)
        },
        label = { Text(text) }
    )
}

@Preview
@Composable
fun SingleWorldSelectionPreview() {
    SingleWorldSelection(
        text = "Example selection",
        onSelected = {},
    )
}

@Composable
fun TextBlockSelection(
    text: String,
    onSelected: (String) -> Unit,
    modifier: Modifier,
) {
    val isSelected = remember { mutableStateOf(false) }

    fun onClicked() {
        isSelected.value = !isSelected.value
        if (isSelected.value) {
            onSelected(text)
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                onClick = { onClicked() }
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(
            checked = isSelected.value,
            onCheckedChange = {
                onClicked()
            }
        )
        Text(
            text = text,
            modifier = modifier.weight(1.0f)
        )
    }
}

@Composable
@Preview
fun TextBlockSelectionPreview() {
    TextBlockSelection(
        text= "Example selection",
        onSelected = {},
        modifier = Modifier,
    )
}
