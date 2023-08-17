package nz.ac.uclive.daa59.topom

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp

@Composable
fun SettingsScreen(colorViewModel: ColorViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) {
        ThemeComponent(colorViewModel = colorViewModel)
    }
}

@Composable
fun ThemeComponent (colorViewModel: ColorViewModel) {
    val selectedValue = rememberSaveable { mutableStateOf("green") }
    val contextForToast = LocalContext.current.applicationContext
    val toastText = stringResource(id = R.string.color_updated);

    val isSelectedItem: (String) -> Boolean = { selectedValue.value == it }
    val onChangeState: (String) -> Unit = { selectedValue.value = it }
    val items = listOf("gray", "green", "orange", "pink", "yellow")

    Column(
        modifier = Modifier.padding(8.dp)
    ) {
        Text(text = stringResource(id = R.string.theme_color))
        items.forEach { item ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .selectable(
                        selected = isSelectedItem(item),
                        onClick = {
                            onChangeState(item)
                            colorViewModel.selectedOption.value = item
                            Toast.makeText(contextForToast, toastText, Toast.LENGTH_SHORT).show()
                        },
                        role = Role.RadioButton
                    )
                    .padding(8.dp)
            ) {
                RadioButton(
                    selected = isSelectedItem(item),
                    onClick = null
                )
                Text(
                    text = when (item) {
                        "gray" -> stringResource(id = R.string.gray)
                        "orange" -> stringResource(id = R.string.orange)
                        "yellow" -> stringResource(id = R.string.yellow)
                        "pink" -> stringResource(id = R.string.pink)
                        else -> { stringResource(id = R.string.green)}
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
