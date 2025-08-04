import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FileUploader(onFileSelected: () -> Unit) {
    Column(Modifier.padding(16.dp)) {
        Text("Upload PDF (not functional yet in Compose WASM)")
        Spacer(Modifier.height(8.dp))
        Button(onClick = onFileSelected) {
            Text("Select PDF")
        }
    }
}
