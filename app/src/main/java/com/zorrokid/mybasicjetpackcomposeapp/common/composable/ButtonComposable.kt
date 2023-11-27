package com.zorrokid.mybasicjetpackcomposeapp.common.composable

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.zorrokid.mybasicjetpackcomposeapp.R

@Composable
fun SubmitButton(onSubmitClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(onClick = onSubmitClick, modifier = modifier) {
        Text(stringResource(id = R.string.submit))
    }
}