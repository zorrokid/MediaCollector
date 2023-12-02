package com.zorrokid.mybasicjetpackcomposeapp.common.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zorrokid.mybasicjetpackcomposeapp.model.CollectionItem

@Composable
fun ItemList(collectionItems: List<CollectionItem>) {
    LazyColumn {
        items(collectionItems, key = { it.id }) { collectionItem ->
            ItemListCard(collectionItem)
        }
    }
}

@Composable
fun ItemListCard(collectionItem: CollectionItem, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .padding(8.dp, 0.dp, 8.dp, 8.dp)
            .fillMaxWidth(),
    ){
        Column {
            Text(text = collectionItem.barcode, modifier = modifier.padding(8.dp))
            Text(text = collectionItem.releaseAreaName, modifier = modifier.padding(8.dp))
        }
    }}

@Composable
@Preview
fun ItemListCardPreview() {
    ItemListCard(collectionItem = CollectionItem(
        id = "1234",
        barcode = "123456789",
        releaseAreaName = "Nordic countries"
    ))
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
        )
    )
}

