import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class Record(var date: String, var description: String, var amount: String)

@Composable
fun RecordTable(records: List<Record>, onUpdate: (List<Record>) -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        items(records.size) { index ->
            val record = records[index]
            Row(Modifier.fillMaxWidth(), verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                EditableField(value = record.date) { newValue ->
                    val updated = records.toMutableList()
                    updated[index] = record.copy(date = newValue)
                    onUpdate(updated)
                }
                Spacer(Modifier.width(8.dp))
                EditableField(value = record.description) { newValue ->
                    val updated = records.toMutableList()
                    updated[index] = record.copy(description = newValue)
                    onUpdate(updated)
                }
                Spacer(Modifier.width(8.dp))
                EditableField(value = record.amount) { newValue ->
                    val updated = records.toMutableList()
                    updated[index] = record.copy(amount = newValue)
                    onUpdate(updated)
                }
            }
            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
fun EditableField(value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.weight(1f)
    )
}
