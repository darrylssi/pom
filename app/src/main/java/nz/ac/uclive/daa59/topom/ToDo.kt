package nz.ac.uclive.daa59.topom

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun ToDoScreen() {
    var newTask by remember { mutableStateOf(TextFieldValue()) }
    val toDoList = remember { MutableStateFlow(listOf<String>()) }
    val taskList by remember { toDoList }.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn {
            items(taskList) { task ->
                TaskItem(task = task)
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = newTask,
                onValueChange = { newTask = it },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 16.dp),
                textStyle = MaterialTheme.typography.body1,
                singleLine = true,
                placeholder = { Text(text = "Enter a new task") }
            )
            Button(
                onClick = {
                    if (newTask.text.isNotEmpty()) {
                        val newList = ArrayList(taskList)
                        newList.add(newTask.text)
                        toDoList.value = newList
                        newTask = TextFieldValue()
                    }
                },
            ) {
                Text(text = "Add")
            }
        }
    }
}

@Composable
fun TaskItem(task: String) {
    var isCompleted by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isCompleted,
            onCheckedChange = { isCompleted = it }
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = task, style = MaterialTheme.typography.body1)
    }
}
