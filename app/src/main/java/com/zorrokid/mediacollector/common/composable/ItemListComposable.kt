package com.zorrokid.mediacollector.common.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zorrokid.mediacollector.R
import com.zorrokid.mediacollector.common.snackbar.SnackbarManager
import com.zorrokid.mediacollector.model.CollectionItem

@Composable
fun ItemList(
    collectionItems: List<CollectionItem>,
    onEdit: ((String) -> Unit, id: String) -> Unit,
    onDelete:  (id: String) -> Unit,
    openScreen: (String) -> Unit,
) {
    val openAlertDialog = remember { mutableStateOf(false) }
    val deleteId = remember { mutableStateOf("") }


    LazyColumn {
        items(collectionItems, key = { it.id }) { collectionItem ->
            ItemListCard(
                collectionItem,
                onEdit = onEdit,
                onDelete = {
                   deleteId.value = it
                   openAlertDialog.value = true
                },
                openScreen = openScreen,
           )
        }
    }
    when {
        openAlertDialog.value -> {
            ConfirmDialog(
                message =  stringResource(R.string.confirm_delete),
                onDismiss = {
                    openAlertDialog.value = false
                    deleteId.value = ""
                },
                onConfirm = {
                    openAlertDialog.value = false
                    onDelete(deleteId.value)
                    SnackbarManager.showMessage(R.string.item_deleted)
                    deleteId.value = ""
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemListCard(
    collectionItem: CollectionItem,
    modifier: Modifier = Modifier,
    onEdit: ((String) -> Unit, id: String) -> Unit,
    onDelete: (id: String) -> Unit,
    openScreen: (String) -> Unit
) {
    Card(
        modifier = modifier
            .padding(8.dp, 0.dp, 8.dp, 8.dp)
            .fillMaxWidth(),
    ){
        var expanded by remember {
            mutableStateOf(false)
        }
        Column {
            Text(text = collectionItem.barcode, modifier = modifier.padding(8.dp))
            Text(text = collectionItem.releaseAreaName, modifier = modifier.padding(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.TopStart)
            ) {
                IconButton(onClick = {expanded = !expanded}) {
                    Icon(
                        Icons.Filled.MoreVert,
                        stringResource(id = R.string.collection_item_menu_icon_description)
                    )
                }
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    DropdownMenuItem(
                        text = { Text(stringResource(id = R.string.edit_item)) },
                        onClick = {
                            expanded = false
                            onEdit(openScreen, collectionItem.id)
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(stringResource(id = R.string.delete_item)) },
                        onClick = {
                            expanded = false
                            onDelete(collectionItem.id)
                        }
                    )
                }
            }
        }
    }}

@Composable
@Preview
fun ItemListCardPreview() {


    ItemListCard(collectionItem = CollectionItem(
        id = "1234",
        barcode = "123456789",
        releaseAreaName = "Nordic countries",
        ), onEdit = {
            openScreen, id -> openScreen("EditItemScreen/$id")
    }, onDelete = {}, openScreen = {})
}

@Composable
@Preview
fun ItemListPreview() {
    ItemList(
        listOf(
            CollectionItem(
                id = "1234",
                barcode = "123456789",
                releaseAreaName = "Test"
            )
        ), onDelete = {}, onEdit = {
            openScreen, id -> openScreen("EditItemScreen/$id")
        }, openScreen = {}
    )
}

