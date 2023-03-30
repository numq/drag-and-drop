package navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import column.ColumnScreen
import row.RowScreen

@Composable
fun Navigation() {
    val (destination, setDestination) = remember { mutableStateOf(Destination.COLUMN) }
    Scaffold(bottomBar = {
        BottomNavigation {
            Destination.values().forEach {
                BottomNavigationItem(destination == it, onClick = { setDestination(it) }, icon = {
                    Text(it.name)
                })
            }
        }
    }) {
        Box(Modifier.fillMaxSize().padding(it), contentAlignment = Alignment.Center) {
            when (destination) {
                Destination.COLUMN -> ColumnScreen()
                Destination.ROW -> RowScreen()
            }
        }
    }
}