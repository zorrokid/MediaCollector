package com.zorrokid.mybasicjetpackcomposeapp.common.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zorrokid.mybasicjetpackcomposeapp.model.CollectionItem

@Composable
fun ItemList(collectionItems: List<CollectionItem>) {
    LazyColumn {
        items(collectionItems, key = { it.id }) { collectionItem ->
            Card(
                modifier = Modifier.padding(8.dp, 0.dp, 8.dp, 8.dp),
            ){
                Column {
                    Text(text = collectionItem.barcode)
                    Text(text = collectionItem.releaseAreaName)
                }
            }
        }
    }
}
