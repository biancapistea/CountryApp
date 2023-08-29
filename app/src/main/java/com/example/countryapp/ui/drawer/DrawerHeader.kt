package com.example.countryapp.ui.drawer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.countryapp.ui.navigation.Destinations
//TODO: Solve the bug: - The menu should display also on Dashboard
@Composable
fun DrawerHeader(){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 64.dp)
        ,
        horizontalArrangement = Arrangement.Center,
    ) {
        Text(text = "GeoQuiz", fontSize = 60.sp)
    }
}

@Composable
fun DrawerBody(navController: NavHostController?, closeNavDrawer: () -> Unit) {
    Column {
        DrawerMenuItem(
            imageVector = Icons.Default.Home,
            text = Destinations.Home.name,
            onItemClick = {
                navController?.navigate(Destinations.Home.name)
                closeNavDrawer()
            }
        )
        DrawerMenuItem(
            imageVector = Icons.Default.Search,
            text = Destinations.Dashboard.name,
            onItemClick = {
                navController?.navigate(Destinations.Dashboard.name)
                closeNavDrawer()
            }
        )
    }
}

@Composable
private fun DrawerMenuItem(
    imageVector: ImageVector,
    text: String,
    onItemClick: () -> Unit){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {onItemClick()}
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ){
        Icon(
            imageVector = imageVector,
            contentDescription = null,
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = text, )
    }
}

@Preview(showBackground = true)
@Composable
private fun DrawerHeaderPreview(){
    DrawerHeader()
}

@Preview(showBackground = true)
@Composable
private fun DrawerBodyPreview(){
    DrawerBody(navController = null, closeNavDrawer = {})
}