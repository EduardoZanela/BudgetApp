import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Header() {
    TopAppBar(
        title = { Text("Budget App") },
        actions = {
            IconButton(onClick = { /* Expand mobile menu in the future */ }) {
                Text("☰")
            }
        }
    )
}
