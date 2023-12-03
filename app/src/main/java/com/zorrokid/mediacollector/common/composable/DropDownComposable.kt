package com.zorrokid.mybasicjetpackcomposeapp.common.composable

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.zorrokid.mybasicjetpackcomposeapp.model.IdAndNameObject
import com.zorrokid.mybasicjetpackcomposeapp.model.ReleaseArea

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T: IdAndNameObject> DropDownWithTextField(
    onSelect: (T) -> Unit,
    selected: T,
    items: List<T>,
    label: String,
    modifier: Modifier = Modifier
) {
    var expanded by remember {
        mutableStateOf(false)
    }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded}
    ) {
        OutlinedTextField(
            value = selected.name,
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            label = { Text(label) },
            readOnly = false,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
        )
        DropdownMenu(
            onDismissRequest = { expanded = false },
            expanded = expanded,
            content = {
                items.forEach() { item ->
                    DropdownMenuItem(
                        text = { Text(item.name) },
                        onClick = {
                            onSelect(item)
                            expanded = false
                        }
                    )
                }},
        )
    }
}


@Composable
@Preview(showBackground = true)
fun DropDownWithTextFieldPreview(){
    val releaseArea = ReleaseArea(name = "Example")
    val releaseAreas: List<ReleaseArea> = listOf(releaseArea)
    DropDownWithTextField(onSelect = {}, selected =releaseArea, items = releaseAreas, label = "Example")
}
