package nz.ac.uclive.daa59.topom

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

@Composable
fun ToDoScreen(colorViewModel: ColorViewModel) {
    var newTask by remember { mutableStateOf(TextFieldValue()) }
    val toDoList = rememberSaveable { mutableStateOf(listOf<String>()) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                colors = TextFieldDefaults.textFieldColors(cursorColor = getColor(colorViewModel = colorViewModel), focusedIndicatorColor = getColor(
                    colorViewModel = colorViewModel)),
                value = newTask,
                onValueChange = { newTask = it },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 16.dp),
                singleLine = true,
                placeholder = { Text(text = stringResource(R.string.enter_task))},
            )
            Button(
                onClick = {
                    if (newTask.text.isNotEmpty()) {
                        toDoList.value = toDoList.value + newTask.text
                        newTask = TextFieldValue()
                    }
                },
                colors = ButtonDefaults.buttonColors( getColor(colorViewModel = colorViewModel) )
            ) {
                Text(text = stringResource(id = R.string.add))
            }
        }
        LazyColumn (verticalArrangement = Arrangement.SpaceBetween) {
            items(toDoList.value) { task ->
                TaskItem(task = task)
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
        Text(text = task)
    }
}
