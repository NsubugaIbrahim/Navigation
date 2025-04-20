package com.example.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Favorites(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        StarImage()
        Text(text = "No Favorites!", style = TextStyle(color = Color.Gray, fontSize = 30.sp))
        Text(text = "\n Add your favorite routines for easy access here \n", style = TextStyle(color = Color.Gray, fontSize = 20.sp), textAlign = TextAlign.Center)
        Text(text = "Tap the '+' button below to add your favorite routines.", style = TextStyle(color = Color.Gray, fontSize = 20.sp), textAlign = TextAlign.Center)
        AddCircle(icon = Icons.Default.AddCircle, tint = Color(0xFF03A9F4))
    }
}

@Composable
fun StarImage() {
    Image(
        painter = painterResource(id = R.drawable.star_mark_svgrepo_com),
        contentDescription = "Star",
        modifier = Modifier.size(80.dp),
        colorFilter = ColorFilter.tint(Color.Gray)
    )
}

@Composable
fun AddCircle(icon: ImageVector, tint: Color = Color.Black) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.6f),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.End
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "Add Circle",
            modifier = Modifier.size(70.dp),
            tint = tint
        )
    }
}
