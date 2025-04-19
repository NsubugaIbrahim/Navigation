package com.example.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.CircleShape



@Composable
@Preview
fun Things(modifier: Modifier = Modifier){
    Column (modifier = Modifier.fillMaxSize()
        .padding(0.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally){

        Column (modifier = Modifier.fillMaxSize().background(Color.White).padding(40.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally){

            ThingsIcon()
            Text(text = "No Things!", style = TextStyle(color = Color.Gray, fontSize = 30.sp))
            Text(text = "It looks like we didn't discover any devices \n", style = TextStyle(color = Color.Gray, fontSize = 20.sp), textAlign = TextAlign.Center)
            Text(text = "Try an option below ", style = TextStyle(color = Color.Gray, fontSize = 20.sp), textAlign = TextAlign.Center)

            OptionsList()

        }
    }
}

@Composable
fun OptionItem(icon: ImageVector, text: String, tint: Color = Color.White) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(50.dp)
                .background(Color(0xFF03A9F4), shape = CircleShape)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                modifier = Modifier.size(30.dp),
                tint = tint
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = text,
            style = TextStyle(color = Color(0xFF03A9F4)),
            fontSize = 20.sp
        )
    }
}

@Composable
fun OptionsList() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OptionItem(icon = Icons.Default.Search, text = "Run discovery")
        OptionItem(icon = Icons.Default.AddCircle, text = "Add a cloud account")
        OptionItem(icon = Icons.Default.DateRange, text = "View our supported devices")
        OptionItem(icon = Icons.Default.Email, text = "Contact support")
    }
}

@Composable
fun ThingsIcon() {
    Image(
        painter = painterResource(id = R.drawable.baseline_format_list_bulleted_24),
        contentDescription = "Things",
        modifier = Modifier.size(200.dp),
    )
}