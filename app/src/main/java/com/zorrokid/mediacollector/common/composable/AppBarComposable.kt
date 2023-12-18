package com.zorrokid.mediacollector.common.composable

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.zorrokid.mediacollector.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicTopAppBar(
    titleResourceId: Int,
    popUp: () -> Unit,
) {
    TopAppBar(
        title = {
            Text(
                stringResource(
                    id = titleResourceId,
                )
            )
        },
        navigationIcon = {
            IconButton(onClick = popUp) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(id = R.string.back)
                )
            }
        },
    )}
