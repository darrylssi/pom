package nz.ac.uclive.daa59.topom

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SettingsScreen(colorViewModel: ColorViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.TopStart)
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

    val height = LocalConfiguration.current.screenWidthDp.dp

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Wrap the Column with verticalScroll to enable scrolling
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = stringResource(id = R.string.theme_color),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            )
            items.forEach { item ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .selectable(
                            selected = isSelectedItem(item),
                            onClick = {
                                onChangeState(item)
                                colorViewModel.selectedOption.value = item
                                Toast.makeText(contextForToast, toastText, Toast.LENGTH_SHORT)
                                    .show()
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
                            else -> {
                                stringResource(id = R.string.green)
                            }
                        },
                        modifier = Modifier.fillMaxWidth().padding(8.dp)
                    )
                }
            }
        }
    }
}
