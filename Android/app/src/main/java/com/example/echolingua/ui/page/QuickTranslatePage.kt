package com.example.echolingua.ui.page

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun QuickTranslatePage() {
    Card(
        modifier = Modifier
            .padding(vertical = 50.dp, horizontal = 20.dp)
            .height(400.dp)
    ) {
        Surface(modifier = Modifier.fillMaxSize()){

        }
    }
}